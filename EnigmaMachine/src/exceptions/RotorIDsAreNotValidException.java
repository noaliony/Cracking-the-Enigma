package exceptions;

public class RotorIDsAreNotValidException extends RuntimeException {

    @Override
    public String getMessage() {

        return "Please select all rotor IDs for the configuration!";
    }
}
