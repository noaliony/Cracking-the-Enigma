package exceptions;

public class RotorsCountBiggerThanReceivedRotorsException extends EnigmaLogicException {

    @Override
    public String getMessage() {

        return  "The file is not valid because the rotors count that the machine need is bigger than received count rotors in XML file";
    }
}
