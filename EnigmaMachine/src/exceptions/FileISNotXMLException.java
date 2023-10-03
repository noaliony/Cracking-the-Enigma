package exceptions;

public class FileISNotXMLException extends EnigmaLogicException {

    @Override
    public String getMessage() {

        return "The file is not valid because the file is not XML";
    }
}
