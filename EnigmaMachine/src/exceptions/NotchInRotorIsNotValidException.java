package exceptions;

public class NotchInRotorIsNotValidException extends EnigmaLogicException {

    private int idRotor;

    public NotchInRotorIsNotValidException(int id) {

        this.idRotor = id;
    }

    @Override
    public String getMessage() {

        String newLine = System.lineSeparator();
        return "The file is not valid because the notch in rotor ID " + idRotor + " is not valid." + newLine + "The notch is out of range of alphabet";
    }
}
