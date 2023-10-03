package exceptions;

public class InvalidCountReflectorMapped extends Exception {

    private String id;

    public InvalidCountReflectorMapped(String id) {

        this.id = id;
    }

    @Override
    public String getMessage() {

        return "The file is not valid because the count of mapping in reflector " + id + " does not match the amount of alphabet";
    }
}
