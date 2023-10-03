package exceptions;

public class ReflectorIDIsNotValidException extends Exception {

    private  String idReflector;

    public ReflectorIDIsNotValidException(String idReflectorString) {

        this.idReflector = idReflectorString;
    }

    @Override
    public String getMessage() {

        return "The file is not valid because reflector ID " + idReflector + "is out of range of I - V";
    }
}
