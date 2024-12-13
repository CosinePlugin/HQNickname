package kr.cosine.nickname.enums;

import org.bukkit.entity.Player;

public enum Sync {
    DISPLAY_NAME(true, Player::setDisplayName),
    TAB(true, Player::setPlayerListName);

    private static final Sync[] values = values();

    public static void syncAll(Player player, String nickname) {
        for (Sync sync : values) {
            if (sync.isSync) {
                sync.syncAction.sync(player, nickname);
            }
        }
    }

    private boolean isSync;
    private final SyncAction syncAction;

    Sync(boolean isSync, SyncAction syncAction) {
        this.isSync = isSync;
        this.syncAction = syncAction;
    }

    public void setSync(boolean isSync) {
        this.isSync = isSync;
    }

    private interface SyncAction {
        void sync(Player player, String nickname);
    }
}
