package kr.cosine.nickname.command;

import kr.cosine.nickname.registry.NicknameSettingRegistry;
import kr.cosine.nickname.service.NicknameService;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class NicknameAdminCommand implements CommandExecutor, TabCompleter {
    private final NicknameSettingRegistry nicknameSettingRegistry;
    private final NicknameService nicknameService;

    public NicknameAdminCommand(NicknameSettingRegistry nicknameSettingRegistry, NicknameService nicknameService) {
        this.nicknameSettingRegistry = nicknameSettingRegistry;
        this.nicknameService = nicknameService;
    }

    private final String[] commands = {
        "§6[닉네임] §f명령어 도움말",
        "§f/닉네임관리",
        " §7┣━§f설정 [유저] [닉네임] §7해당 유저의 닉네임을 설정합니다.",
        " §7┣━§f저장 §7닉네임의 변경된 사항을 저장합니다.",
        " §7┗━§f리로드 §7config.yml을 리로드합니다."
    };

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            printHelp(sender);
            return true;
        }
        checker(sender, args);
        return true;
    }

    private void printHelp(CommandSender sender) {
        for (String command : commands) {
            sender.sendMessage(command);
        }
    }

    private void checker(CommandSender sender, String[] args) {
        switch (args[0]) {
            case "설정": {
                setNickname(sender, args);
                break;
            }

            case "초기화": {
                resetNickname(sender, args);
                break;
            }

            case "저장": {
                nicknameService.save();
                sender.sendMessage("§a닉네임의 변경된 사항을 저장하였습니다.");
                break;
            }

            case "리로드": {
                nicknameService.reload();
                sender.sendMessage("§aconfig.yml을 리로드하였습니다.");
                break;
            }

            default: printHelp(sender);
        }
    }

    private void setNickname(CommandSender sender, String[] args) {
        getNickname(sender, args, (target) -> {
            if (args.length == 2) {
                sender.sendMessage("§c닉네임을 입력해주세요.");
                return;
            }
            String nickname = args[2];
            if (nicknameService.isNickname(nickname)) {
                sender.sendMessage("§c이미 존재하는 닉네임입니다.");
                return;
            }
            if (!nicknameSettingRegistry.isValidNickname(nickname)) {
                sender.sendMessage("§c닉네임에 들어갈 수 없는 문자열이 포함되어 있습니다.");
                return;
            }
            if (!nicknameSettingRegistry.isValidNicknameLength(nickname)) {
                sender.sendMessage("§c닉네임을 입력할 수 있는 범위를 넘어섰습니다.");
                return;
            }
            sender.sendMessage("§a" + target.getName() + "님의 닉네임을 " + nickname + "(으)로 설정하였습니다.");
            nicknameService.setNickname(target, nickname);
        });
    }

    private void resetNickname(CommandSender sender, String[] args) {
        getNickname(sender, args, (target) -> {
            if (!nicknameService.hasNickname(target)) {
                sender.sendMessage("§c닉네임이 설정되어 있지 않은 유저입니다.");
                return;
            }
            nicknameService.removeNickname(target);
            sender.sendMessage("§a" + target.getName() + "님의 닉네임을 초기화하였습니다.");
            sender.sendMessage("§7└ 해당 유저는 재접속을 한번 진행해야 합니다.");
        });
    }

    private void getNickname(CommandSender sender, String[] args, Consumer<Player> consumer) {
        if (args.length == 1) {
            sender.sendMessage("§c유저를 입력해주세요.");
            return;
        }
        String targetName = args[1];
        Player target = sender.getServer().getPlayer(targetName);
        if (target == null) {
            sender.sendMessage("§c해당 유저가 오프라인입니다.");
            return;
        }
        consumer.accept(target);
    }

    private final List<String> tabs = Arrays.asList("설정", "저장", "리로드");

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length <= 1) {
            return StringUtil.copyPartialMatches(args[0], tabs, new ArrayList<>());
        }
        if (args[0].equals("설정") && args.length == 2) {
            return getPlayersWithIgnoreCase(sender.getServer(), args[1]);
        }
        return Collections.emptyList();
    }

    private List<String> getPlayersWithIgnoreCase(Server server, String input) {
        return server.getOnlinePlayers()
            .stream()
            .map(Player::getName)
            .filter(name -> StringUtil.startsWithIgnoreCase(name, input))
            .collect(Collectors.toList());
    }
}
