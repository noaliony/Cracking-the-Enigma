package enums;

public enum GameLevel {
    EASY("EASY"),
    MEDIUM("MEDIUM"),
    DIFFICULT("DIFFICULT"),
    IMPOSSIBLE("IMPOSSIBLE");

    private final String difficultyText;

    GameLevel(String difficultyText) {
        this.difficultyText = difficultyText;
    }

    public String getDifficultyText() {
        return difficultyText;
    }

    public static GameLevel convertStringToGameLevel(String text) {
        for (GameLevel enumValue : GameLevel.values()) {
            if (enumValue.difficultyText.equals(text)) {
                return enumValue;
            }
        }

        return null;
    }
}
