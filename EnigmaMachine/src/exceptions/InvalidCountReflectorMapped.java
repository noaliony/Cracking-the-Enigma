package exceptions;

public class InvalidCountReflectorMapped extends EnigmaLogicException {

    private String id;

    public InvalidCountReflectorMapped(String id) {

        this.id = id;
    }

    @Override
    public String getMessage() {

        return "The file is not valid because the count of mapping in reflector " + id + " does not match the amount of alphabet";
    }
}
