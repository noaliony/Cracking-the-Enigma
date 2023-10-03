package exceptions;

public class StartPositionAreNotValidException extends RuntimeException {

    @Override
    public String getMessage() {

        return "Please select all rotors start position for the configuration";
    }
}
