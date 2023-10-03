package exceptions;

public class RotorsHaveNotUniqueIDException extends Exception {

    @Override
    public String getMessage() {

        return "The file is not valid because rotors have not unique ID";
    }
}
