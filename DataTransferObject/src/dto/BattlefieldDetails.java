package dto;

import enums.GameLevel;
import enums.GameStatus;

public class BattlefieldDetails {

    private String battlefieldName;
    private String uBoatName;
    private GameStatus gameStatus;
    private GameLevel gameLevel;
    private Integer registeredAlliesCount;
    private Integer totalAlliesCount;
    private String messageToDecode;

    public BattlefieldDetails(String battlefieldName, String uBoatName,
                              GameStatus gameStatus, GameLevel gameLevel,
                              Integer registeredAlliesCount, Integer totalAlliesCount) {
        this.battlefieldName = battlefieldName;
        this.uBoatName = uBoatName;
        this.gameStatus = gameStatus;
        this.gameLevel = gameLevel;
        this.registeredAlliesCount = registeredAlliesCount;
        this.totalAlliesCount = totalAlliesCount;
    }

    public String getBattlefieldName() {
        return battlefieldName;
    }

    public String getUBoatName() {
        return uBoatName;
    }

    public GameStatus getGameStatus() {
        return gameStatus;
    }

    public GameLevel getGameLevel() {
        return gameLevel;
    }

    public Integer getRegisteredAlliesCount() {
        return registeredAlliesCount;
    }

    public Integer getTotalAlliesCount() {
        return totalAlliesCount;
    }

    public String getMessageToDecode() {
        return messageToDecode;
    }
}
