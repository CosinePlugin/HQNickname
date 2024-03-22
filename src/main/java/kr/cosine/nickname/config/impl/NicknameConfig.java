package kr.cosine.nickname.config.impl;

import kr.cosine.nickname.config.Configuration;
import kr.cosine.nickname.registry.NicknameRegistry;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.Plugin;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class NicknameConfig extends Configuration {

    private final NicknameRegistry nicknameRegistry;

    public NicknameConfig(Plugin plugin, NicknameRegistry nicknameRegistry) {
        super(plugin, "nickname.yml");
        this.nicknameRegistry = nicknameRegistry;
    }

    public void load() {
        if (!file.exists()) return;
        ConfigurationSection nicknameSection = config.getConfigurationSection(rootSectionKey);
        nicknameSection.getKeys(false).forEach(uniqueIdText -> {
            UUID uniqueId = UUID.fromString(uniqueIdText);
            String nickname = nicknameSection.getString(uniqueIdText);
            nicknameRegistry.setNickname(uniqueId, nickname);
        });
    }

    public void save() {
        try {
            if (nicknameRegistry.isChanged) {
                Map<UUID, String> nicknameMap = new HashMap<>(nicknameRegistry.getNicknameMap());
                nicknameMap.forEach((uniqueId, nickname) -> config.set(rootSectionKey + "." + uniqueId, nickname));
                config.save(file);
                nicknameRegistry.isChanged = false;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
