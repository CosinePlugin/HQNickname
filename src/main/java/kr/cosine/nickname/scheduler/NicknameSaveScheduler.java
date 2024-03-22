package kr.cosine.nickname.scheduler;

import kr.cosine.nickname.config.impl.NicknameConfig;
import org.bukkit.scheduler.BukkitRunnable;

public class NicknameSaveScheduler extends BukkitRunnable {

    private final NicknameConfig nicknameConfig;

    public NicknameSaveScheduler(NicknameConfig nicknameConfig) {
        this.nicknameConfig = nicknameConfig;
    }

    @Override
    public void run() {
        nicknameConfig.save();
    }
}
