package exceptions;

public class ThereIsMappingBetweenLetterAndItselfInReflectorException extends Exception {

    private String id;

    public ThereIsMappingBetweenLetterAndItselfInReflectorException(String id) {

        this.id = id;
    }

    @Override
    public String getMessage() {

        return "The file is not valid because there is mapping between letter and itself in reflector ID " + id;
    }
}
