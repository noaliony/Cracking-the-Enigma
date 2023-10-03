package exceptions;

public class RotorHaveLessMappingsException extends Exception {

    private int id;

    public RotorHaveLessMappingsException(int id) {

        this.id = id;
    }

    @Override
    public String getMessage() {

        return "the file is not valid because the rotor ID " + id + " have less mappings" ;
    }
}
