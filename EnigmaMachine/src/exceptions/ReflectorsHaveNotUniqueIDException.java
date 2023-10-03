package exceptions;

public class ReflectorsHaveNotUniqueIDException extends EnigmaLogicException {

    @Override
    public String getMessage() {

        return "The file is not valid because reflectors have not unique ID";
    }
}
