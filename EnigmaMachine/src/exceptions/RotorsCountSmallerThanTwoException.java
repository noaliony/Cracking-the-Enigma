package exceptions;

public class RotorsCountSmallerThanTwoException extends EnigmaLogicException {

    @Override
    public String getMessage() {

        return  "The file is not valid because the rotors count that the machine need is smaller than 2";
    }
}
