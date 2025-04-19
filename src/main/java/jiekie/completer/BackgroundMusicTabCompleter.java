package jiekie.completer;

import jiekie.BackgroundMusicPlugin;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class BackgroundMusicTabCompleter implements TabCompleter {
    private final BackgroundMusicPlugin plugin;

    public BackgroundMusicTabCompleter(BackgroundMusicPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        if(!sender.hasPermission("jk.background_music.command")) return Collections.emptyList();
        if(!(sender instanceof Player)) return Collections.emptyList();
        Player player = (Player) sender;
        Block targetBlock = player.getTargetBlockExact(10);

        int length = args.length;
        String commandType = args[0];

        if(length == 1)
            return Arrays.asList("구역설정", "구역제거", "음악설정", "음악제거", "도움말");

        if((length == 3 || length == 6) && commandType.equals("구역설정")) {
            if(targetBlock != null)
                return List.of(String.valueOf(targetBlock.getLocation().getX()));
        }

        if((length == 4 || length == 7) && commandType.equals("구역설정")) {
            if(targetBlock != null)
                return List.of(String.valueOf(targetBlock.getLocation().getY()));
        }

        if((length == 5 || length == 8) && commandType.equals("구역설정")) {
            if(targetBlock != null)
                return List.of(String.valueOf(targetBlock.getLocation().getZ()));
        }

        if(length == 2 && commandType.equals("구역제거"))
            return plugin.getWorldManager().getRegionNames(false);

        if(length == 2 && (commandType.equals("음악설정") || commandType.equals("음악제거")))
            return plugin.getWorldManager().getRegionNames(true);

        return Collections.emptyList();
    }
}
