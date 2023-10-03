package exceptions;

public class ABCAmountIsNotEvenException extends Exception {

    @Override
    public String getMessage() {

        return "The file is not valid because the amount of characters of ABC is not even";
    }
}
