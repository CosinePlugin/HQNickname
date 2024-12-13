package kr.cosine.nickname;

import kr.cosine.nickname.command.NicknameAdminCommand;
import kr.cosine.nickname.config.impl.SettingConfig;
import kr.cosine.nickname.json.NicknameJson;
import kr.cosine.nickname.listener.NicknameListener;
import kr.cosine.nickname.registry.NicknameRegistry;
import kr.cosine.nickname.registry.NicknameSettingRegistry;
import kr.cosine.nickname.service.NicknameService;
import org.bukkit.plugin.java.JavaPlugin;

public class HQNickname extends JavaPlugin {
    private NicknameJson nicknameJson;

    @Override
    public void onEnable() {
        NicknameSettingRegistry nicknameSettingRegistry = new NicknameSettingRegistry();
        NicknameRegistry nicknameRegistry = new NicknameRegistry();

        SettingConfig settingConfig = new SettingConfig(this, nicknameSettingRegistry);
        settingConfig.load();

        nicknameJson = new NicknameJson(this, nicknameRegistry);
        nicknameJson.load();

        NicknameService nicknameService = new NicknameService(this, nicknameRegistry, settingConfig, nicknameJson);

        getServer().getPluginManager().registerEvents(new NicknameListener(nicknameService), this);
        getCommand("닉네임관리").setExecutor(new NicknameAdminCommand(nicknameSettingRegistry, nicknameService));

        long autoSavePeriod = settingConfig.getAusoSavePriod();
        getServer().getScheduler().runTaskTimerAsynchronously(this, nicknameJson::save, autoSavePeriod, autoSavePeriod);
    }

    @Override
    public void onDisable() {
        nicknameJson.save();
    }
}
