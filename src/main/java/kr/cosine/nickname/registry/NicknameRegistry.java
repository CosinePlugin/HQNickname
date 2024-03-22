package kr.cosine.nickname.registry;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class NicknameRegistry {

    private final Map<UUID, String> nicknameMap = new HashMap<>();

    public boolean isChanged = false;

    public boolean isNickname(String nickname) {
        return nicknameMap.containsValue(nickname);
    }

    public String findNickname(UUID uniqueId) {
        return nicknameMap.get(uniqueId);
    }

    public void setNickname(UUID uniqueId, String nickname) {
        nicknameMap.put(uniqueId, nickname);
        isChanged = true;
    }

    public Map<UUID, String> getNicknameMap() {
        return nicknameMap;
    }
}
