package exceptions;

public class ReflectorsHaveNotUniqueIDException extends Exception {

    @Override
    public String getMessage() {

        return "The file is not valid because reflectors have not unique ID";
    }
}
