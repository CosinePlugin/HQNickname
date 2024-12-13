package kr.cosine.nickname.listener;

import kr.cosine.nickname.enums.Format;
import kr.cosine.nickname.service.NicknameService;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class NicknameListener implements Listener {
    private final NicknameService nicknameService;

    public NicknameListener(NicknameService nicknameService) {
        this.nicknameService = nicknameService;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        String nickname = nicknameService.applyNickname(player);
        if (nickname == null) {
            nickname = player.getName();
        }
        String format = Format.JOIN.getMessage(nickname);
        if (format.isEmpty()) return;
        event.setJoinMessage(format);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        String nickname = nicknameService.getNickname(player);
        String format = Format.QUIT.getMessage(nickname);
        if (format.isEmpty()) return;
        event.setQuitMessage(format);
    }
}
