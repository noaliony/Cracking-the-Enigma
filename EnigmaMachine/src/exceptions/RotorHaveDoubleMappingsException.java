package exceptions;

public class RotorHaveDoubleMappingsException extends EnigmaLogicException {

    private int id;

    public RotorHaveDoubleMappingsException(int id) {

        this.id = id;
    }

    @Override
    public String getMessage() {

        return "The file is not valid because the rotor ID " + id + " have double mapping";
    }
}
