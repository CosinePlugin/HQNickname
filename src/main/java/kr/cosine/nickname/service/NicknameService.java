package kr.cosine.nickname.service;

import kr.cosine.nickname.config.impl.SettingConfig;
import kr.cosine.nickname.enums.Sync;
import kr.cosine.nickname.json.NicknameJson;
import kr.cosine.nickname.registry.NicknameRegistry;
import org.bukkit.Server;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class NicknameService {
    private final Plugin plugin;
    private final Server server;

    private final NicknameRegistry nicknameRegistry;

    private final SettingConfig settingConfig;
    private final NicknameJson nicknameJson;

    public NicknameService(
        Plugin plugin,
        NicknameRegistry nicknameRegistry,
        SettingConfig settingConfig,
        NicknameJson nicknameJson
    ) {
        this.plugin = plugin;
        this.server = plugin.getServer();
        this.nicknameRegistry = nicknameRegistry;
        this.settingConfig = settingConfig;
        this.nicknameJson = nicknameJson;
    }

    public boolean isNickname(String nickname) {
        return nicknameRegistry.isNickname(nickname);
    }

    public boolean hasNickname(Player player) {
        return nicknameRegistry.hasNickname(player.getUniqueId());
    }

    public void setNickname(Player player, String nickname) {
        nicknameRegistry.setNickname(player.getUniqueId(), nickname);
        applyNickname(player);
    }

    public void removeNickname(Player player) {
        nicknameRegistry.removeNickname(player.getUniqueId());
    }

    public void save() {
        server.getScheduler().runTaskAsynchronously(plugin, nicknameJson::save);
    }

    public void reload() {
        settingConfig.reload();
    }

    public String applyNickname(Player player) {
        String nickname = nicknameRegistry.findNickname(player.getUniqueId());
        if (nickname == null) return null;
        hideAll(player);
        Sync.syncAll(player, nickname);
        setGameProfileNickname(player, nickname);
        showAll(player);
        return nickname;
    }

    public String getNickname(Player player) {
        String nickname = nicknameRegistry.findNickname(player.getUniqueId());
        return nickname == null ? player.getName() : nickname;
    }

    private void setGameProfileNickname(Player player, String nickname) {
        try {
            var gameProfile = ((CraftPlayer) player).getProfile();
            var field = gameProfile.getClass().getDeclaredField("name");
            field.setAccessible(true);
            field.set(gameProfile, nickname);
        } catch (IllegalAccessException | NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
    }

    private void hideAll(Player player) {
        for (Player loopPlayer : server.getOnlinePlayers()) {
            loopPlayer.hidePlayer(plugin, player);
        }
    }

    private void showAll(Player player) {
        for (Player loopPlayer : server.getOnlinePlayers()) {
            loopPlayer.showPlayer(plugin, player);
        }
    }
}
