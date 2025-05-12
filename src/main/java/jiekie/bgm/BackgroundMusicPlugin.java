package jiekie.bgm;

import jiekie.bgm.command.BackgroundMusicCommand;
import jiekie.bgm.completer.BackgroundMusicTabCompleter;
import jiekie.bgm.event.PlayerEvent;
import jiekie.bgm.manager.PlayingManager;
import jiekie.bgm.manager.WorldManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class BackgroundMusicPlugin extends JavaPlugin {

    private PlayingManager playingManager;
    private WorldManager worldManager;

    @Override
    public void onEnable() {
        // config
        saveDefaultConfig();
        reloadConfig();

        // manager
        playingManager = new PlayingManager(this);
        worldManager = new WorldManager(this);
        worldManager.load();

        // command
        getCommand("브금").setExecutor(new BackgroundMusicCommand(this));

        // tab completer
        getCommand("브금").setTabCompleter(new BackgroundMusicTabCompleter(this));

        // event
        getServer().getPluginManager().registerEvents(new PlayerEvent(this), this);

        getLogger().info("브금 설정 플러그인 by Jiekie");
        getLogger().info("Copyright © 2025 Jiekie. All rights reserved.");
    }

    public PlayingManager getPlayingManager() {
        return playingManager;
    }

    public WorldManager getWorldManager() {
        return worldManager;
    }

    @Override
    public void onDisable() {
        worldManager.save();
    }
}
