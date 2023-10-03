package exceptions;

public class RotorIDIsNotValidException extends EnigmaLogicException {

    private int id;

    public RotorIDIsNotValidException(int id) {

        this.id = id;
    }

    @Override
    public String getMessage() {

        return "The file is not valid because rotor ID " + id + " is not valid. The number of ID is less than 1";
    }
}
