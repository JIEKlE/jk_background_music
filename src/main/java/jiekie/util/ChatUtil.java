package jiekie.util;

import jiekie.model.SoundData;
import jiekie.model.WorldData;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ChatUtil {
    public static String getWarnPrefix() {
        return "[ " + ChatColor.YELLOW + "❗" + ChatColor.WHITE + " ] ";
    }

    public static void notPlayer(CommandSender sender) {
        sender.sendMessage(getWarnPrefix() + "플레이어가 아닙니다.");
    }

    public static void notOp(Player player) {
        player.sendMessage(getWarnPrefix() + "권한이 없습니다.");
    }

    public static String wrongCommand() {
        return getWarnPrefix() + "명령어 사용법이 잘못되었습니다.";
    }

    public static void coordinatesNotNumber(Player player) {
        player.sendMessage(getWarnPrefix() + "좌표 값은 숫자여야 합니다.");
    }

    public static void secondsNotNumber(Player player) {
        player.sendMessage(getWarnPrefix() + "시간(초) 값은 숫자여야 합니다.");
    }

    public static void secondsUnderZero(Player player) {
        player.sendMessage(getWarnPrefix() + "시간(초)은 0 이하로 설정할 수 없습니다.");
    }

    public static void volumeNotNumber(Player player) {
        player.sendMessage(getWarnPrefix() + "볼륨 값은 숫자여야 합니다.");
    }

    public static void setRegion(Player player) {
        player.sendMessage(getWarnPrefix() + "구역을 지정했습니다.");
    }

    public static void regionDoesNotExist(Player player) {
        player.sendMessage(getWarnPrefix() + "입력한 구역은 존재하지 않습니다.");
    }

    public static void changeRegion(Player player) {
        player.sendMessage(getWarnPrefix() + "구역을 변경했습니다.");
    }

    public static void removeRegion(Player player) {
        player.sendMessage(getWarnPrefix() + "구역을 제거했습니다.");
    }

    public static void setGlobalBackgroundMusic(Player player) {
        player.sendMessage(getWarnPrefix() + "전체 브금을 설정했습니다.");
    }

    public static void setRegionBackgroundMusic(Player player) {
        player.sendMessage(getWarnPrefix() + "구역 브금을 설정했습니다.");
    }

    public static void removeGlobalBackgroundMusic(Player player) {
        player.sendMessage(getWarnPrefix() + "전체 브금을 제거했습니다.");
    }

    public static void removeRegionBackgroundMusic(Player player) {
        player.sendMessage(getWarnPrefix() + "구역 브금을 제거했습니다.");
    }

    public static void noMusic(Player player) {
        player.sendMessage(getWarnPrefix() + "지정된 음악이 없습니다.");
    }

    public static void musicInfoPrefix(CommandSender sender) {
        sender.sendMessage("");
        sender.sendMessage("─────────── 브금정보 ───────────");
        sender.sendMessage("");
    }

    public static void musicInfo(CommandSender sender, WorldData worldData) {
        if(worldData.getType() == "global") {
            sender.sendMessage("　월드 : " + ChatColor.GREEN + ChatColor.BOLD + worldData.getWorld());
            sender.sendMessage("　유형 : 전체");
            
        } else if(worldData.getType() == "region") {
            sender.sendMessage("　구역명 : " + ChatColor.GREEN + ChatColor.BOLD + worldData.getName());
            sender.sendMessage("　월드 : " + worldData.getWorld());
            sender.sendMessage("　유형 : 구역");

            Location minLoc = worldData.getMinLocation();
            Location maxLoc = worldData.getMaxLocation();
            sender.sendMessage("　구역 좌표 : [" + minLoc.getBlockX() + ", " + minLoc.getBlockY() + ", " + minLoc.getBlockZ() + "] - [" + maxLoc.getBlockX() + ", " + maxLoc.getBlockY() + ", " + maxLoc.getBlockZ() + "]");

        }

        SoundData soundData = worldData.getSoundData();
        if(soundData == null || soundData.getSound() == null || soundData.getSound().isBlank()) {
            sender.sendMessage("　사운드 : 없음");

        } else {
            sender.sendMessage("　사운드 : " + soundData.getSound());
            sender.sendMessage("　볼륨 : " + soundData.getVolume());
            sender.sendMessage("　음악 길이 : " + soundData.getDuration() + "초");
        }
    }

    public static void horizontalLineSuffix(CommandSender sender) {
        sender.sendMessage("");
        sender.sendMessage("──────────────────────────");
        sender.sendMessage("");
    }

    public static void commandHelper(Player player) {
        player.sendMessage(getWarnPrefix() + "/브금 도움말" + ChatColor.GRAY + " : 사용 가능한 명령어를 확인할 수 있습니다.");
    }

    public static void commandList(Player player) {
        player.sendMessage(getWarnPrefix() + "브금 명령어 목록");
        player.sendMessage("　　　① /브금 구역설정 구역명 x y z x y z");
        player.sendMessage(ChatColor.GRAY + "　　　　　: 구역을 지정합니다.");
        player.sendMessage("　　　② /브금 구역제거 구역명");
        player.sendMessage(ChatColor.GRAY + "　　　　　: 구역을 제거합니다.");
        player.sendMessage("　　　③ /브금 음악설정 전체|구역명 사운드 볼륨 음악길이(초)");
        player.sendMessage(ChatColor.GRAY + "　　　　　: 전체 브금 또는 특정 구역의 브금을 설정합니다.");
        player.sendMessage("　　　④ /브금 음악제거 전체|구역명");
        player.sendMessage(ChatColor.GRAY + "　　　　　: 전체 브금 또는 특정 구역의 브금을 제거합니다.");
        player.sendMessage("　　　⑤ /브금 정보 전체|구역명");
        player.sendMessage(ChatColor.GRAY + "　　　　　: 브금 정보를 조회합니다.");
        player.sendMessage("　　　⑥ /브금 도움말");
        player.sendMessage(ChatColor.GRAY + "　　　　　: 사용 가능한 명령어를 확인할 수 있습니다.");
    }
}
