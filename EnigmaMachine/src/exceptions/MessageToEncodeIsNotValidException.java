package exceptions;

public class MessageToEncodeIsNotValidException extends EnigmaLogicException {
    @Override
    public String getMessage() {
        return "The message to encode is not valid because there are in it no valid words";
    }
}
