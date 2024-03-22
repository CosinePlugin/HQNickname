package kr.cosine.nickname.service;

import kr.cosine.nickname.config.impl.NicknameConfig;
import kr.cosine.nickname.config.impl.SettingConfig;
import kr.cosine.nickname.enums.Sync;
import kr.cosine.nickname.registry.NicknameRegistry;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class NicknameService {

    private final Plugin plugin;
    private final Server server;

    private final NicknameRegistry nicknameRegistry;

    private final SettingConfig settingConfig;
    private final NicknameConfig nicknameConfig;

    private final Class<?> craftPlayerClass;

    public NicknameService(
        Plugin plugin,
        NicknameRegistry nicknameRegistry,
        SettingConfig settingConfig,
        NicknameConfig nicknameConfig
    ) {
        this.plugin = plugin;
        this.server = plugin.getServer();
        this.nicknameRegistry = nicknameRegistry;
        this.settingConfig = settingConfig;
        this.nicknameConfig = nicknameConfig;
        try {
            String version = server.getClass().getName().replace(".", ",").split(",")[3];
            craftPlayerClass = Class.forName("org.bukkit.craftbukkit." + version + ".entity.CraftPlayer");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean isNickname(String nickname) {
        return nicknameRegistry.isNickname(nickname);
    }

    public void setNickname(Player player, String nickname) {
        nicknameRegistry.setNickname(player.getUniqueId(), nickname);
        applyNickname(player);
    }

    public void save() {
        server.getScheduler().runTaskAsynchronously(plugin, nicknameConfig::save);
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
            Object craftPlayer = craftPlayerClass.cast(player);
            Method method = craftPlayerClass.getMethod("getProfile");
            Object gameProfile = method.invoke(craftPlayer);
            Field field = gameProfile.getClass().getDeclaredField("name");
            field.setAccessible(true);
            field.set(gameProfile, nickname);
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException | NoSuchFieldException e) {
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
