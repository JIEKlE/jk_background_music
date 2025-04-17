package jiekie.util;

import jiekie.BackgroundMusicPlugin;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayingManager {
    private final BackgroundMusicPlugin plugin;
    private final Map<UUID, BukkitTask> taskMap = new HashMap<>();

    public PlayingManager(BackgroundMusicPlugin plugin) {
        this.plugin = plugin;
    }

    public void stop(Player player) {
        player.stopSound(SoundCategory.MASTER);

        UUID uuid = player.getUniqueId();
        if(taskMap.containsKey(uuid)) {
            taskMap.get(uuid).cancel();
            taskMap.remove(uuid);
        }
    }

    public void play(Player player, SoundData soundData) {
        stop(player);

        if(soundData.getSound() == null) return;
        if(soundData.getDuration() <= 0) return;

        UUID uuid = player.getUniqueId();
        BukkitTask task = new BukkitRunnable() {
            @Override
            public void run() {
                if(!player.isOnline()) {
                    stop(player);
                }

                player.playSound(player.getLocation(), soundData.getSound(), SoundCategory.MASTER, soundData.getVolume(), 1.0f);
            }
        }.runTaskTimer(plugin, 0L, soundData.getDuration() * 20L);

        taskMap.put(uuid, task);
    }
}
