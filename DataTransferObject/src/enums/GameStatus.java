package enums;

public enum GameStatus {
    ACTIVE ("ACTIVE"),
    WAITING("WAITING"),
    READY("READY");

    private final String gameStatusText;

    GameStatus(String gameStatusText) {
        this.gameStatusText = gameStatusText;
    }

    public String getGameStatusText() {
        return gameStatusText;
    }
}
