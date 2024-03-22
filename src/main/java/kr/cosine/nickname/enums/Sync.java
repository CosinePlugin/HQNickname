package kr.cosine.nickname.enums;

import org.bukkit.entity.Player;

public enum Sync {
    DISPLAY_NAME(true, Player::setDisplayName),
    TAB(true, Player::setPlayerListName);

    private static final Sync[] values = values();

    public static void syncAll(Player player, String nickname) {
        for (Sync sync : values) {
            if (sync.isSync) {
                System.out.println(sync + " 적용");
                sync.syncAction.sync(player, nickname);
            } else {
                System.out.println(sync + " 미적용");
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
        System.out.println(this + " : " + isSync);
        this.isSync = isSync;
    }

    private interface SyncAction {
        void sync(Player player, String nickname);
    }
}
