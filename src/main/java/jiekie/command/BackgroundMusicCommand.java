package jiekie.command;

import jiekie.BackgroundMusicPlugin;
import jiekie.util.ChatUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class BackgroundMusicCommand implements CommandExecutor {
    private final BackgroundMusicPlugin plugin;

    public BackgroundMusicCommand(BackgroundMusicPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!(sender instanceof Player)) {
            ChatUtil.notPlayer(sender);
            return true;
        }

        Player player = (Player) sender;
        if(!player.isOp()) {
            ChatUtil.notOp(player);
            return true;
        }

        if(args == null || args.length == 0) {
            ChatUtil.commandHelper(player);
            return true;
        }

        switch(args[0]) {
            case "구역설정":
                setRegion(player, args);
                break;

            case "구역제거":
                removeRegion(player, args);
                break;

            case "음악설정":
                setMusic(player, args);
                break;

            case "음악제거":
                removeMusic(player, args);
                break;

            case "정보":
                showMusicInfo(player, args);
                break;

            case "도움말":
                ChatUtil.commandList(player);
                break;

            default:
                ChatUtil.commandHelper(player);
                break;
        }

        return true;
    }

    private void setRegion(Player player, String[] args) {
        if(args.length != 8) {
            player.sendMessage(ChatUtil.wrongCommand() + " (/브금 구역설정 구역명 x y z x y z)");
            return;
        }

        try {
            String name = args[1];
            double minX = Math.min(Double.parseDouble(args[2]), Double.parseDouble(args[5]));
            double minY = Math.min(Double.parseDouble(args[3]), Double.parseDouble(args[6]));
            double minZ = Math.min(Double.parseDouble(args[4]), Double.parseDouble(args[7]));

            double maxX = Math.max(Double.parseDouble(args[2]), Double.parseDouble(args[5]));
            double maxY = Math.max(Double.parseDouble(args[3]), Double.parseDouble(args[6]));
            double maxZ = Math.max(Double.parseDouble(args[4]), Double.parseDouble(args[7]));

            plugin.getWorldManager().setRegion(player, name, minX, minY, minZ, maxX, maxY, maxZ);

        } catch(NumberFormatException e) {
            ChatUtil.coordinatesNotNumber(player);
        }
    }

    private void removeRegion(Player player, String[] args) {
        if(args.length < 2) {
            player.sendMessage(ChatUtil.wrongCommand() + " (/브금 구역제거 구역명)");
            return;
        }

        String name = args[1];
        plugin.getWorldManager().removeRegion(player, name);
    }

    private void setMusic(Player player, String[] args) {
        if(args.length != 5) {
            player.sendMessage(ChatUtil.wrongCommand() + " (/브금 음악설정 전체|구역명 사운드 볼륨 초)");
            return;
        }

        float volume = 0.5f;
        try {
            volume = Float.parseFloat(args[3]);
        } catch(NumberFormatException e) {
            ChatUtil.volumeNotNumber(player);
        }

        int duration = 0;
        try {
            duration = Integer.parseInt(args[4]);
        } catch(NumberFormatException e) {
            ChatUtil.secondsNotNumber(player);
            return;
        }

        if(duration <= 0) {
            ChatUtil.secondsUnderZero(player);
            return;
        }

        String name = args[1];
        String sound = args[2];
        if(name.equals("전체"))
            plugin.getWorldManager().setGlobalMusic(player, sound, volume, duration);
        else
            plugin.getWorldManager().setRegionMusic(player, name, sound, volume, duration);
    }

    private void removeMusic(Player player, String[] args) {
        if(args.length < 2) {
            player.sendMessage(ChatUtil.wrongCommand() + " (/브금 음악제거 전체|구역명)");
            return;
        }

        String name = args[1];
        if(name.equals("전체"))
            plugin.getWorldManager().removeGlobalMusic(player);
        else
            plugin.getWorldManager().removeRegionMusic(player, name);
    }

    private void showMusicInfo(Player player, String[] args) {
        if(args.length < 2) {
            player.sendMessage(ChatUtil.wrongCommand() + " (/브금 정보 전체|구역명)");
            return;
        }

        String name = args[1];
        if(name.equals("전체"))
            plugin.getWorldManager().showGlobalMusicInfo(player);
        else
            plugin.getWorldManager().showRegionMusicInfo(player, name);

    }
}
