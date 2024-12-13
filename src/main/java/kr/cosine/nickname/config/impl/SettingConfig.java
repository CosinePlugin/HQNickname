package kr.cosine.nickname.config.impl;

import kr.cosine.nickname.config.Configuration;
import kr.cosine.nickname.enums.Format;
import kr.cosine.nickname.enums.Sync;
import kr.cosine.nickname.registry.NicknameSettingRegistry;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.Plugin;

import java.util.function.BiConsumer;

public class SettingConfig extends Configuration {
    private final NicknameSettingRegistry nicknameSettingRegistry;

    public SettingConfig(Plugin plugin, NicknameSettingRegistry nicknameSettingRegistry) {
        super(plugin);
        this.nicknameSettingRegistry = nicknameSettingRegistry;
    }

    private long ausoSavePriod = 12000;

    @Override
    public void load() {
        ausoSavePriod = config.getLong("auto-save-period");
        loadNicknameSection();
        loadSyncSection();
        loadFormatSection();
    }

    private void loadNicknameSection() {
        ConfigurationSection nicknameSection = config.getConfigurationSection("nickname");
        String regex = nicknameSection.getString("regex");
        int minLength = nicknameSection.getInt("length.min", 2);
        int maxLength = nicknameSection.getInt("length.max", 10);
        nicknameSettingRegistry.setRegex(regex);
        nicknameSettingRegistry.setMinLength(minLength);
        nicknameSettingRegistry.setMaxLength(maxLength);
    }

    private void loadSyncSection() {
        ConfigurationSection syncSection = config.getConfigurationSection("sync");
        loopSection(syncSection, Sync::setSync, Sync.class);
    }

    private void loadFormatSection() {
        ConfigurationSection formatSection = config.getConfigurationSection("format");
        loopSection(formatSection, Format::setMessage, Format.class);
    }

    @SuppressWarnings("unchecked")
    private <E extends Enum<E>, T> void loopSection(ConfigurationSection section, BiConsumer<E, T> consumer, Class<E> clazz) {
        section.getKeys(false).forEach((text) -> {
            T value = (T) section.get(text);
            if (value != null) {
                E key = E.valueOf(clazz, text.toUpperCase().replace("-", "_"));
                consumer.accept(key, value);
            }
        });
    }

    @Override
    public void reload() {
        super.reload();
    }

    public long getAusoSavePriod() {
        return ausoSavePriod;
    }
}