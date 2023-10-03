package users;

import engine.Engine;

public class UBoat {

    private String userName;
    private Engine engine;
    private boolean isReady = false;
    private String battleName;

    public UBoat (String userName){
        this.userName = userName;
        engine = new Engine();
    }

    public Engine getEngine() {
        return engine;
    }

    public void setIsReady(boolean isReady) {
        this.isReady = isReady;
    }

    public String getBattlefieldName() {
        return battleName;
    }

    public void setBattleName(String battleName) {
        this.battleName = battleName;
    }

    public boolean isReady() {
        return isReady;
    }

    public String getUserName() {
        return userName;
    }
}
