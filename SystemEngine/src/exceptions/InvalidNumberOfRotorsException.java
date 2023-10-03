package exceptions;

public class InvalidNumberOfRotorsException extends Exception {

    @Override
    public String getMessage() {

        return "The file is not valid because the number of rotors is not valid. The number of rotors that machine needs to be less or equal to 99";
    }
}
