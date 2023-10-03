package exceptions;

public class ThereIsNotRunningNumberInReflectorsException extends EnigmaLogicException {

    @Override
    public String getMessage() {

        return "The file is not valid because there is not running number of reflectors";
    }
}
