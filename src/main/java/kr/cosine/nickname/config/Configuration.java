package kr.cosine.nickname.config;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;

public abstract class Configuration {

    protected final String rootSectionKey;

    protected final File file;
    protected final YamlConfiguration config;

    public Configuration(Plugin plugin) {
        String path = "config.yml";
        rootSectionKey = slice(path);
        File newFile = new File(plugin.getDataFolder(), path);
        if (!newFile.exists() && plugin.getResource(path) != null) {
            plugin.saveResource(path, false);
        }
        file = newFile;
        config = YamlConfiguration.loadConfiguration(file);
    }

    public Configuration(Plugin plugin, String path) {
        rootSectionKey = slice(path);
        file = new File(plugin.getDataFolder(), path);
        config = YamlConfiguration.loadConfiguration(file);
    }

    private String slice(String path) {
        return path.substring(0, path.length() - 4);
    }

    public void load() {}

    public void save() {
        try {
            config.save(file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void reload() {
        try {
            config.load(file);
            load();
        } catch (IOException | InvalidConfigurationException e) {
            throw new RuntimeException(e);
        }
    }
}
