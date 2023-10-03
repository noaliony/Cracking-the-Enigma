package exceptions;

public class ThereAreNoExistAllReflectorsException extends Exception {

    String bound;

    public ThereAreNoExistAllReflectorsException(String bound) {
        this.bound = bound;
    }

    @Override
    public String getMessage() {
        return "The file is not valid because there are no exist all reflectors between I and " + bound;
    }
}
