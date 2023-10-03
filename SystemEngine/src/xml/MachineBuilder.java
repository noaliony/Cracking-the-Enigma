package xml;

import components.PlugBoard;
import components.Reflector;
import components.Repository;
import components.Rotor;
import enums.GameLevel;
import enums.ReflectorID;
import machine.*;

import generated.*;

import java.util.*;

public class MachineBuilder {

    private static CTEEnigma cteEnigma;

    public Machine createMachineFromCTEEnigma(CTEEnigma cteEnigma) {
        this.cteEnigma = cteEnigma;
        List<Character> alphabet = createAlphabet();
        PlugBoard plugBoard = createPlugBoard(alphabet);
        int rotorsCount = cteEnigma.getCTEMachine().getRotorsCount();
        Repository repository = createRepository();
        List<Character> excludeChars = createExcludeCharsList();
        Set<String> dictionary = createDictionary(excludeChars);
        String gameLevelString = cteEnigma.getCTEBattlefield().getLevel().trim().toUpperCase();
        GameLevel gameLevel = GameLevel.convertStringToGameLevel(gameLevelString);
        String gameTitle = cteEnigma.getCTEBattlefield().getBattleName();
        int alliesCount = cteEnigma.getCTEBattlefield().getAllies();
        Machine machineResult = new Machine(alphabet, plugBoard, rotorsCount, repository, excludeChars, dictionary, gameLevel, gameTitle, alliesCount);

        machineResult.initializeMachineDetailsObject();

        return machineResult;
    }

    private Set<String> createDictionary(List<Character> excludeChars) {
        String wordsBefore = cteEnigma.getCTEDecipher().getCTEDictionary().getWords().toUpperCase().trim();
        String wordsAfter = wordsBefore.chars().filter(character -> !excludeChars.contains((char) character))
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append).toString();
        String[] splittedStrings = wordsAfter.split(" ");

        return new HashSet<>(Arrays.asList(splittedStrings));
    }

    private List<Character> createExcludeCharsList() {
        String excludeCharsString = cteEnigma.getCTEDecipher().getCTEDictionary().getExcludeChars();
        List<Character> excludeCharsList = convertStringToCharacterList(excludeCharsString);

        return excludeCharsList;
    }

    private PlugBoard createPlugBoard(List<Character> alphabet) {
        PlugBoard plugBoard = new PlugBoard(alphabet);

        return plugBoard;
    }

    private static Repository createRepository() {
        Repository repository = new Repository();
        List<Rotor> rotorsList;
        List<Reflector> reflectorsList;

        rotorsList = createRotorsList();
        reflectorsList = createReflectorsList();
        repository.setRotorsList(rotorsList);
        repository.setReflectorsList(reflectorsList);

        return repository;
    }

    private static List<Reflector> createReflectorsList() {
        List<Reflector> reflectorsList = new ArrayList<>();
        List<CTEReflector> CTEReflectList = cteEnigma.getCTEMachine().getCTEReflectors().getCTEReflector();

        for (CTEReflector currentCTEReflector : CTEReflectList) {

            Reflector currentReflector = createReflector(currentCTEReflector);
            reflectorsList.add(currentReflector);
        }
        return reflectorsList;
    }

    private static Reflector createReflector(CTEReflector currentCTEReflector) {
        Reflector currentReflector = new Reflector();
        List<CTEReflect> reflectListForCurrentReflector = currentCTEReflector.getCTEReflect();
        List<Integer> reflectorArray = new ArrayList<>(Collections.nCopies(reflectListForCurrentReflector.size() * 2, 0));
        ReflectorID reflectorIDForSingleReflector = null;

        for (CTEReflect currentCTEReflect : reflectListForCurrentReflector) {
            int input = currentCTEReflect.getInput();
            int output = currentCTEReflect.getOutput();

            reflectorArray.set(input - 1, input);
            reflectorArray.set(output - 1, input);
        }

        currentReflector.setReflectorArray(reflectorArray);
        reflectorIDForSingleReflector = ReflectorID.convertStringToReflectorID(currentCTEReflector.getId());
        currentReflector.setID(reflectorIDForSingleReflector);

       return currentReflector;
    }

    private static List<Rotor> createRotorsList() {
        List<CTERotor> CTERotorList = cteEnigma.getCTEMachine().getCTERotors().getCTERotor();
        List<Rotor> rotorsList = new ArrayList<>();

        for (CTERotor currentCTERotor : CTERotorList) {

            Rotor currentRotor = createRotor(currentCTERotor);
            rotorsList.add(currentRotor);
        }
        return rotorsList;
    }

    private static Rotor createRotor(CTERotor currentCTERotor) {

        Rotor currentRotor = new Rotor();
        List<Character> rightArrayForSingleRotor = new ArrayList<>();
        List<Character> leftArrayForSingleRotor = new ArrayList<>();

        rightArrayForSingleRotor.add(null);
        leftArrayForSingleRotor.add(null);
        ReadXML.createRightAndLeftListsForSingleRotor(currentCTERotor, rightArrayForSingleRotor, leftArrayForSingleRotor);
        currentRotor.setNotch(currentCTERotor.getNotch());
        currentRotor.setStartingNotch(currentCTERotor.getNotch());
        currentRotor.setID(currentCTERotor.getId());
        currentRotor.setLeftArray(leftArrayForSingleRotor);
        currentRotor.setRightArray(rightArrayForSingleRotor);
        currentRotor.setLength(rightArrayForSingleRotor.size());

        return currentRotor;
    }

    private static List<Character> createAlphabet() {

        String ABC = cteEnigma.getCTEMachine().getABC();

        return convertStringToCharacterList(ABC.trim());
    }

    private static List<Character> convertStringToCharacterList(String stringReceived) {

        List<Character> characterList = new ArrayList<>();

        stringReceived = stringReceived.toUpperCase();
        for (int i = 0; i < stringReceived.length(); i++) {
            characterList.add(stringReceived.charAt(i));
        }

        return characterList;
    }


}
