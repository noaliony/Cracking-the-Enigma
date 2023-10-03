package exceptions;

import java.util.List;

public class MessageForCodingNotValidException extends RuntimeException {

    private List<Character> alphabet;

    public MessageForCodingNotValidException(List<Character> alphabet) {
        this.alphabet = alphabet;
    }

    @Override
    public String getMessage() {
        return "Please press only characters from the alphabet:" + alphabet;
    }

}
