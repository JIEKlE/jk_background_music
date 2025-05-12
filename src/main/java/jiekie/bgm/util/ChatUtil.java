package jiekie.bgm.util;

import jiekie.bgm.model.SoundData;
import jiekie.bgm.model.WorldData;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Objects;

public class ChatUtil {
    /* error */
    public static final String COORDINATES_NOT_NUMBER = getXPrefix() + "좌표 값은 숫자여야 합니다.";
    public static final String SECONDS_NOT_NUMBER = getXPrefix() + "시간(초) 값은 숫자여야 합니다.";
    public static final String SECONDS_UNDER_ZERO = getXPrefix() + "시간(초)은 0 이하로 설정할 수 없습니다.";
    public static final String VOLUME_NOT_NUMBER = getXPrefix() + "볼륨 값은 숫자여야 합니다.";
    public static final String REGION_DOES_NOT_EXIST = getXPrefix() + "입력한 구역은 존재하지 않습니다.";
    public static final String NO_MUSIC = getXPrefix() + "지정된 음악이 없습니다.";

    /* feedback */
    public static final String SET_REGION = getCheckPrefix() + "구역을 지정했습니다.";
    public static final String CHANGE_REGION = getCheckPrefix() + "구역을 변경했습니다.";
    public static final String REMOVE_REGION = getCheckPrefix() + "구역을 제거했습니다.";
    public static final String SET_GLOBAL_BACKGROUND_MUSIC = getCheckPrefix() + "전체 브금을 설정했습니다.";
    public static final String SET_REGION_BACKGROUND_MUSIC = getCheckPrefix() + "구역 브금을 설정했습니다.";
    public static final String REMOVE_GLOBAL_BACKGROUND_MUSIC = getCheckPrefix() + "전체 브금을 제거했습니다.";
    public static final String REMOVE_REGION_BACKGROUND_MUSIC = getCheckPrefix() + "구역 브금을 제거했습니다.";

    /* prefix */
    public static String getCheckPrefix() {
        return "\uA001 ";
    }

    public static String getXPrefix() {
        return "\uA002 ";
    }

    public static String getWarnPrefix() {
        return "\uA003 ";
    }

    public static void showMessage(Player player, String message) {
        player.sendMessage(message);
    }

    /* validate */
    public static void notPlayer(CommandSender sender) {
        sender.sendMessage(getWarnPrefix() + "플레이어가 아닙니다.");
    }

    public static void notOp(Player player) {
        player.sendMessage(getWarnPrefix() + "권한이 없습니다.");
    }

    public static String wrongCommand() {
        return getWarnPrefix() + "명령어 사용법이 잘못되었습니다.";
    }

    /* info */
    public static void musicInfoPrefix(CommandSender sender) {
        sender.sendMessage("");
        sender.sendMessage("─────────── 브금정보 ───────────");
        sender.sendMessage("");
    }

    public static void musicInfo(CommandSender sender, WorldData worldData) {
        if(Objects.equals(worldData.getType(), "global")) {
            sender.sendMessage("　월드 : " + ChatColor.GREEN + ChatColor.BOLD + worldData.getWorld());
            sender.sendMessage("　유형 : 전체");
            
        } else if(Objects.equals(worldData.getType(), "region")) {
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
        player.sendMessage("");
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
        player.sendMessage("");
    }
}
