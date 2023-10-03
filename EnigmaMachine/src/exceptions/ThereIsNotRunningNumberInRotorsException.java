package exceptions;

public class ThereIsNotRunningNumberInRotorsException extends EnigmaLogicException {

    @Override
    public String getMessage() {

        return "The file is not valid because there is not running number of rotors";
    }
}
