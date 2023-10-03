package exceptions;

public class RotorHaveTooMuchMappingsException extends Exception {

    private int id;

    public RotorHaveTooMuchMappingsException(int id) {
        this.id = id;
    }

    @Override
    public String getMessage() {

        return "The file is not valid because the rotor ID " + id + " have too much mappings" ;
    }
}
