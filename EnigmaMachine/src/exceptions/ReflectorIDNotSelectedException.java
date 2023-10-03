package exceptions;

public class ReflectorIDNotSelectedException extends RuntimeException {

    @Override
    public String getMessage() {

        return "Please select reflector ID for the configuration!";
    }
}
