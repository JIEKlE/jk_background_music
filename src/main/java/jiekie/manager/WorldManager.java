package jiekie.manager;

import jiekie.BackgroundMusicPlugin;
import jiekie.util.ChatUtil;
import jiekie.model.SoundData;
import jiekie.util.SoundEffectUtil;
import jiekie.model.WorldData;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.*;

public class WorldManager {
    private final BackgroundMusicPlugin plugin;
    private final List<WorldData> globalList = new ArrayList<>();
    private final List<WorldData> regionList = new ArrayList<>();
    private final Map<UUID, WorldData> playerWorld = new HashMap<>();
    private final String GLOBAL_PREFIX = "global";
    private final String REGIONS_PREFIX = "regions";
    private final String REGION_PREFIX = "region";

    public WorldManager(BackgroundMusicPlugin plugin) {
        this.plugin = plugin;
    }

    public void load() {
        initGlobalSection();
        initRegionSection();
    }

    private void initGlobalSection() {
        globalList.clear();
        FileConfiguration config = plugin.getConfig();
        ConfigurationSection globalSection = config.getConfigurationSection(GLOBAL_PREFIX);

        if(globalSection == null) return;
        for(String world : globalSection.getKeys(false)) {
            String path = GLOBAL_PREFIX + "." + world;

            String sound = config.getString(path + ".sound", null);
            float volume = (float) config.getDouble(path + ".volume", 0.5f);
            int duration = config.getInt(path + ".duration", 0);

            SoundData soundData = new SoundData(sound, volume, duration);
            globalList.add(new WorldData(null, world, GLOBAL_PREFIX, null, null, soundData));
        }
    }

    private void initRegionSection() {
        regionList.clear();
        FileConfiguration config = plugin.getConfig();
        ConfigurationSection regionSection = config.getConfigurationSection(REGIONS_PREFIX);

        if(regionSection == null) return;
        for(String name : regionSection.getKeys(false)) {
            String path = REGIONS_PREFIX + "." + name;
            String worldName = config.getString(path + ".world");

            double minX = config.getDouble(path + ".min_x");
            double minY = config.getDouble(path + ".min_y");
            double minZ = config.getDouble(path + ".min_z");

            double maxX = config.getDouble(path + ".max_x");
            double maxY = config.getDouble(path + ".max_y");
            double maxZ = config.getDouble(path + ".max_z");

            Location minLocation = new Location(Bukkit.getWorld(worldName), minX, minY, minZ);
            Location maxLocation = new Location(Bukkit.getWorld(worldName), maxX, maxY, maxZ);

            String sound = config.getString(path + ".sound", null);
            float volume = (float) config.getDouble(path + ".volume", 0.5f);
            int duration = config.getInt(path + ".duration", 0);

            SoundData soundData = new SoundData(sound, volume, duration);
            regionList.add(new WorldData(name, worldName, REGION_PREFIX, minLocation, maxLocation, soundData));
        }
    }

    public void setRegion(Player player, String name, double minX, double minY, double minZ, double maxX, double maxY, double maxZ) {
        if(regionExists(name))
            updateRegion(player, name, minX, minY, minZ, maxX, maxY, maxZ);
        else
            addRegion(player, name, minX, minY, minZ, maxX, maxY, maxZ);
    }

    private void addRegion(Player player, String name, double minX, double minY, double minZ, double maxX, double maxY, double maxZ) {
        World world = player.getWorld();
        String worldName = world.getName();
        Location minLocation = new Location(world, minX, minY, minZ);
        Location maxLocation = new Location(world, maxX, maxY, maxZ);
        SoundData soundData = new SoundData(null, 0.5f, 0);

        WorldData worldData = new WorldData(name, worldName, REGION_PREFIX, minLocation, maxLocation, soundData);
        regionList.add(worldData);

        ChatUtil.setRegion(player);
        SoundEffectUtil.playNoteBlockBell(player);
    }

    private void updateRegion(Player player, String name, double minX, double minY, double minZ, double maxX, double maxY, double maxZ) {
        WorldData region = getRegionWorldData(name);
        World world = player.getWorld();
        String worldName = world.getName();
        Location minLocation = new Location(world, minX, minY, minZ);
        Location maxLocation = new Location(world, maxX, maxY, maxZ);

        region.setWorld(worldName);
        region.setMinLocation(minLocation);
        region.setMaxLocation(maxLocation);

        for(Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            WorldData playerWorldData = getPlayerWorldData(onlinePlayer);
            if(!equalWorld(playerWorldData, region)) continue;
            if(region.contains(onlinePlayer.getLocation())) continue;

            playerWorld.remove(onlinePlayer.getName());
            plugin.getPlayingManager().stop(onlinePlayer);

            WorldData global = getCuurentPlayerWorldData(onlinePlayer);
            if(global == null) continue;

            playerWorld.put(onlinePlayer.getUniqueId(), global);
            plugin.getPlayingManager().play(onlinePlayer, global.getSoundData());
        }

        ChatUtil.changeRegion(player);
        SoundEffectUtil.playNoteBlockBell(player);
    }

    public void removeRegion(Player player, String name) {
        if(!regionExists(name)) {
            ChatUtil.regionDoesNotExist(player);
            return;
        }

        WorldData region = getRegionWorldData(name);
        List<Player> targetPlayerList = new ArrayList<>();
        for(Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            WorldData playerWorldData = getPlayerWorldData(onlinePlayer);
            if(!equalWorld(playerWorldData, region)) continue;

            playerWorld.remove(onlinePlayer.getName());
            plugin.getPlayingManager().stop(onlinePlayer);
            targetPlayerList.add(onlinePlayer);
        }

        regionList.remove(region);

        for(Player targetPlayer : targetPlayerList) {
            WorldData global = getCuurentPlayerWorldData(targetPlayer);
            if(global == null) continue;

            playerWorld.put(targetPlayer.getUniqueId(), global);
            plugin.getPlayingManager().play(targetPlayer, global.getSoundData());
        }

        ChatUtil.removeRegion(player);
        SoundEffectUtil.playNoteBlockBell(player);
    }

    private WorldData getCuurentPlayerWorldData(Player player) {
        Location location = player.getLocation();
        for(WorldData region : regionList) {
            if(region.contains(location)) return region;
        }

        String world = player.getWorld().getName();
        return getGlobalWorldData(world);
    }

    private boolean equalWorld(WorldData playerWorldData, WorldData worldData) {
        if(playerWorldData == null) return false;
        if(worldData == null) return false;

        String playerType = playerWorldData.getType();
        String playerWorld = playerWorldData.getWorld();
        String playerName = playerWorldData.getName();

        String type = worldData.getType();
        String world = worldData.getWorld();
        String name = worldData.getName();

        if(!playerType.equals(type)) return false;

        if(type.equals(GLOBAL_PREFIX) && !playerWorld.equals(world))
            return false;

        if(type.equals(REGION_PREFIX) && !playerName.equals(name))
            return false;

        return true;
    }

    public void setGlobalMusic(Player player, String sound, float volume, int duration) {
        World world = player.getWorld();
        String worldName = world.getName();
        WorldData global = getGlobalWorldData(worldName);
        SoundData soundData = new SoundData(sound, volume, duration);

        if(global == null) {
            global = new WorldData(null, worldName, GLOBAL_PREFIX, null, null, soundData);
            globalList.add(global);

        } else {
            global.setSoundData(soundData);
        }

        for(Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            WorldData playerWorldData = getCuurentPlayerWorldData(onlinePlayer);
            if(!equalWorld(playerWorldData, global)) continue;

            playerWorld.put(onlinePlayer.getUniqueId(), global);
            plugin.getPlayingManager().play(onlinePlayer, soundData);
        }

        ChatUtil.setGlobalBackgroundMusic(player);
        SoundEffectUtil.playNoteBlockBell(player);
    }

    public void setRegionMusic(Player player, String name, String sound, float volume, int duration) {
        if(!regionExists(name)) {
            ChatUtil.regionDoesNotExist(player);
            return;
        }

        WorldData region = getRegionWorldData(name);

        SoundData soundData = new SoundData(sound, volume, duration);
        region.setSoundData(soundData);

        for(Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            WorldData playerWorldData = getPlayerWorldData(onlinePlayer);
            if(!equalWorld(playerWorldData, region)) continue;

            plugin.getPlayingManager().play(onlinePlayer, soundData);
        }

        ChatUtil.setRegionBackgroundMusic(player);
        SoundEffectUtil.playNoteBlockBell(player);
    }

    public void removeGlobalMusic(Player player) {
        World world = player.getWorld();
        String worldName = world.getName();
        WorldData global = getGlobalWorldData(worldName);
        if(global == null) {
            ChatUtil.noMusic(player);
            return;
        }

        globalList.remove(global);

        for(Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            WorldData playerWorldData = getPlayerWorldData(onlinePlayer);
            if(!equalWorld(playerWorldData, global)) continue;

            playerWorld.remove(onlinePlayer.getUniqueId(), global);
            plugin.getPlayingManager().stop(onlinePlayer);
        }

        ChatUtil.removeGlobalBackgroundMusic(player);
        SoundEffectUtil.playNoteBlockBell(player);
    }

    public void removeRegionMusic(Player player, String name) {
        if(!regionExists(name)) {
            ChatUtil.regionDoesNotExist(player);
            return;
        }

        WorldData region = getRegionWorldData(name);
        if(!soundExist(region)) {
            ChatUtil.noMusic(player);
            return;
        }

        SoundData soundData = new SoundData(null, 0.5f, 0);
        region.setSoundData(soundData);

        for(Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            WorldData playerWorldData = getPlayerWorldData(onlinePlayer);
            if(!equalWorld(playerWorldData, region)) continue;

            plugin.getPlayingManager().stop(onlinePlayer);
        }

        ChatUtil.removeRegionBackgroundMusic(player);
        SoundEffectUtil.playNoteBlockBell(player);
    }

    private boolean regionExists(String name) {
        for(WorldData data : regionList) {
            if(data.getName().equals(name)) return true;
        }

        return false;
    }

    private boolean soundExist(WorldData worldData) {
        SoundData soundData = worldData.getSoundData();

        if(soundData == null) return false;
        if(soundData.getSound() == null) return false;
        return true;
    }

    public List<String> getRegionNames(boolean withAll) {
        List<String> regionNameList = new ArrayList<>();
        for(WorldData worldData : regionList) {
            regionNameList.add(worldData.getName());
        }

        if(withAll)
            regionNameList.add(0, "전체");

        return regionNameList;
    }

    private WorldData getPlayerWorldData(Player player) {
        return playerWorld.get(player.getUniqueId());
    }

    private WorldData getGlobalWorldData(String world) {
        for(WorldData worldData : globalList) {
            if(worldData.getWorld().equals(world)) return worldData;
        }

        return null;
    }

    private WorldData getRegionWorldData(String name) {
        for(WorldData worldData : regionList) {
            if(worldData.getName().equals(name)) return worldData;
        }

        return null;
    }

    public void playMusicToPlayerWhenPlayerJoin(Player player) {
        WorldData currentWorldData = getCuurentPlayerWorldData(player);
        if(currentWorldData == null) return;

        playerWorld.put(player.getUniqueId(), currentWorldData);
        plugin.getPlayingManager().play(player, currentWorldData.getSoundData());
    }

    public void playMusicToPlayer(Player player) {
        WorldData previousWorldData = getPlayerWorldData(player);
        WorldData currentWorldData = getCuurentPlayerWorldData(player);

        if(currentWorldData != null && !equalWorld(previousWorldData, currentWorldData)) {
            plugin.getPlayingManager().play(player, currentWorldData.getSoundData());
            playerWorld.put(player.getUniqueId(), currentWorldData);

        } else if(currentWorldData == null && previousWorldData != null) {
            plugin.getPlayingManager().stop(player);
            playerWorld.remove(player.getUniqueId());
        }
    }

    public void showGlobalMusicInfo(Player player) {
        World world = player.getWorld();
        String worldName = world.getName();
        WorldData global = getGlobalWorldData(worldName);
        if(global == null) {
            ChatUtil.noMusic(player);
            return;
        }

        ChatUtil.musicInfoPrefix(player);
        ChatUtil.musicInfo(player, global);
        ChatUtil.horizontalLineSuffix(player);
        SoundEffectUtil.playNoteBlockBell(player);
    }

    public void showRegionMusicInfo(Player player, String name) {
        if(!regionExists(name)) {
            ChatUtil.regionDoesNotExist(player);
            return;
        }

        WorldData region = getRegionWorldData(name);

        ChatUtil.musicInfoPrefix(player);
        ChatUtil.musicInfo(player, region);
        ChatUtil.horizontalLineSuffix(player);
        SoundEffectUtil.playNoteBlockBell(player);
    }

    public void save() {
        saveGlobalSection();
        saveRegionSection();
        plugin.saveConfig();
    }

    public void saveGlobalSection() {
        FileConfiguration config = plugin.getConfig();
        config.set(GLOBAL_PREFIX, null);

        for(WorldData world : globalList) {
            String path = GLOBAL_PREFIX + "." + world.getWorld();
            SoundData soundData = world.getSoundData();
            config.set(path + ".sound", soundData.getSound());
            config.set(path + ".volume", soundData.getVolume());
            config.set(path + ".duration", soundData.getDuration());
        }
    }

    private void saveRegionSection() {
        FileConfiguration config = plugin.getConfig();
        config.set(REGIONS_PREFIX, null);

        for(WorldData world : regionList) {
            String path = REGIONS_PREFIX + "." + world.getName();
            config.set(path + ".world", world.getWorld());
            config.set(path + ".min_x", world.getMinLocation().getX());
            config.set(path + ".min_y", world.getMinLocation().getY());
            config.set(path + ".min_z", world.getMinLocation().getZ());

            config.set(path + ".max_x", world.getMaxLocation().getX());
            config.set(path + ".max_y", world.getMaxLocation().getY());
            config.set(path + ".max_z", world.getMaxLocation().getZ());

            SoundData soundData = world.getSoundData();
            config.set(path + ".sound", soundData.getSound());
            config.set(path + ".volume", soundData.getVolume());
            config.set(path + ".duration", soundData.getDuration());
        }
    }
}
