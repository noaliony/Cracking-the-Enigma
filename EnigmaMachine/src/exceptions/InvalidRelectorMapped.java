package exceptions;

public class InvalidRelectorMapped extends EnigmaLogicException {

    private String id;

    public InvalidRelectorMapped(String id) {

        this.id = id;
    }

    @Override
    public String getMessage() {

        return "The file is not valid because the mapping in reflector " + id + " is out of range" ;
    }
}
