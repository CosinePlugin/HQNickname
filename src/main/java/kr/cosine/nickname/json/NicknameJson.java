package kr.cosine.nickname.json;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import kr.cosine.nickname.registry.NicknameRegistry;
import org.bukkit.plugin.Plugin;

import java.io.*;

public class NicknameJson {
    private final Gson gson;

    private final File file;

    private final NicknameRegistry nicknameRegistry;

    public NicknameJson(Plugin plugin, NicknameRegistry nicknameRegistry) {
        gson = new GsonBuilder().setPrettyPrinting().create();
        file = new File(plugin.getDataFolder(), "nickname.json");
        this.nicknameRegistry = nicknameRegistry;
    }

    public void load() {
        if (!file.exists()) return;
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            var nicknameRegistry = gson.fromJson(reader, NicknameRegistry.class);
            this.nicknameRegistry.restore(nicknameRegistry);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void save() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            var json = gson.toJson(nicknameRegistry);
            writer.write(json);
            writer.newLine();
            writer.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
