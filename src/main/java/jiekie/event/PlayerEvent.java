package jiekie.event;

import jiekie.BackgroundMusicPlugin;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class PlayerEvent implements Listener {
    private final BackgroundMusicPlugin plugin;

    public PlayerEvent(BackgroundMusicPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();
        new BukkitRunnable() {
            @Override
            public void run() {
                plugin.getWorldManager().playMusicToPlayerWhenPlayerJoin(player);
            }
        }.runTaskLater(plugin, 60L);
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e) {
        Player player = e.getPlayer();
        plugin.getPlayingManager().stop(player);
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent e) {
        Player player = e.getPlayer();
        plugin.getWorldManager().playMusicToPlayer(player);
    }
}
