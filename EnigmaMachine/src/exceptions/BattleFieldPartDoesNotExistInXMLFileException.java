package exceptions;

public class BattleFieldPartDoesNotExistInXMLFileException extends EnigmaLogicException {
    @Override
    public String getMessage() {

        return "The file is not valid because the battlefield part does not exist in XML file";
    }
}
