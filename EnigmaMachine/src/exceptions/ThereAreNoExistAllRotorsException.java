package exceptions;

public class ThereAreNoExistAllRotorsException extends EnigmaLogicException {

    int bound;

    public ThereAreNoExistAllRotorsException(Integer bound) {
        this.bound = bound;
    }

    @Override
    public String getMessage() {
        return "The file is not valid because there are no exist all rotors between 1 and " + bound;
    }
}
