package kr.cosine.nickname.registry;

public class NicknameSettingRegistry {
    private String regex = "^[a-zA-Z0-9_가-힣]*$";

    private int minLength = 2;
    private int maxLength = 10;

    public boolean isValidNickname(String nickname) {
        return nickname.matches(regex);
    }

    public void setRegex(String regex) {
        this.regex = regex;
    }

    public boolean isValidNicknameLength(String nickname) {
        if (nickname.isEmpty()) return false;
        int length = nickname.length();
        return minLength <= length && length <= maxLength;
    }

    public void setMinLength(int minLength) {
        this.minLength = minLength;
    }

    public void setMaxLength(int maxLength) {
        this.maxLength = maxLength;
    }
}
