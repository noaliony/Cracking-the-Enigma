package exceptions;

public class BattleNameIsAlreadyExistInSystem extends EnigmaLogicException {
    private String battleName;

    public BattleNameIsAlreadyExistInSystem(String battleName) {
        this.battleName = battleName;
    }

    @Override
    public String getMessage() {
        return "The file is not valid because the battlefield name " + battleName +" already exist in System";
    }
}
