package exceptions;

public class DecipherPartDoesNotExistInXMLFileException extends EnigmaLogicException {

    @Override
    public String getMessage() {

        return "The file is not valid because the decipher part does not exist in XML file";
    }
}
