package jiekie;

import jiekie.command.BackgroundMusicCommand;
import jiekie.completer.BackgroundMusicTabCompleter;
import jiekie.event.PlayerEvent;
import jiekie.manager.PlayingManager;
import jiekie.manager.WorldManager;
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
