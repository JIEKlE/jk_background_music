package jiekie.completer;

import jiekie.BackgroundMusicPlugin;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class BackgroundMusicTabCompleter implements TabCompleter {
    private final BackgroundMusicPlugin plugin;

    public BackgroundMusicTabCompleter(BackgroundMusicPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if(!sender.hasPermission("jk.background_music.command")) return Collections.emptyList();
        if(!(sender instanceof Player player)) return Collections.emptyList();
        Block targetBlock = player.getTargetBlockExact(10);

        int length = args.length;
        String commandType = args[0];

        if(length == 1)
            return Arrays.asList("구역설정", "구역제거", "음악설정", "음악제거", "정보", "도움말");

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

        if(length == 2) {
            switch (commandType) {
                case "구역설정" -> { return List.of("구역명"); }
                case "구역제거" -> { return plugin.getWorldManager().getRegionNames(false); }
                case "음악설정", "음악제거", "정보" -> { return plugin.getWorldManager().getRegionNames(true); }
            }
        }

        if(length == 3 && commandType.equals("음악설정"))
            return List.of("사운드");

        if(length == 4 && commandType.equals("음악설정"))
            return List.of("0.5", "1");

        if(length == 5 && commandType.equals("음악설정"))
            return List.of("음악길이(초)");

        return Collections.emptyList();
    }
}
