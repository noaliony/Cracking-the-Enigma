package exceptions;

public class InvalidAgentsCountException extends EnigmaLogicException {

    @Override
    public String getMessage() {

        return "The file is not valid because the agents count is not valid - should ne number between 2 and 50!";
    }
}
