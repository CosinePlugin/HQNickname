package kr.cosine.nickname.registry;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class NicknameRegistry {
    private final Map<UUID, String> nicknameMap = new HashMap<>();

    public transient boolean isChanged = false;

    public void restore(NicknameRegistry nicknameRegistry) {
        nicknameMap.clear();
        nicknameMap.putAll(nicknameRegistry.nicknameMap);
    }

    public boolean isNickname(String nickname) {
        return nicknameMap.containsValue(nickname);
    }

    public boolean hasNickname(UUID uniqueId) {
        return nicknameMap.containsKey(uniqueId);
    }

    public String findNickname(UUID uniqueId) {
        return nicknameMap.get(uniqueId);
    }

    public void setNickname(UUID uniqueId, String nickname) {
        nicknameMap.put(uniqueId, nickname);
        isChanged = true;
    }

    public void removeNickname(UUID uniqueId) {
        nicknameMap.remove(uniqueId);
        isChanged = true;
    }
}
