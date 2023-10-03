package xml;

import components.PlugBoard;
import components.Reflector;
import components.Repository;
import components.Rotor;
import enums.ReflectorID;
import machine.*;

import generated.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MachineBuilder {

    private static CTEEnigma cteEnigma;

    public Machine createMachineFromCTEEnigma(CTEEnigma cteEnigma) {

        this.cteEnigma = cteEnigma;
        List<Character> alphabet = createAlphabet();
        PlugBoard plugBoard = createPlugBoard(alphabet);
        int rotorsCount = cteEnigma.getCTEMachine().getRotorsCount();
        Repository repository = createRepository();
        Machine machineResult = new Machine(alphabet, plugBoard, rotorsCount, repository);

        return machineResult;
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
        reflectorIDForSingleReflector = ReflectorID.convertFromStringToReflectorID(currentCTEReflector.getId());
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
        rightArrayForSingleRotor.add(' ');
        leftArrayForSingleRotor.add(' ');

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
        List<Character> alphabet = convertABCToCharacterList(ABC.trim());

        return alphabet;
    }

    private static List<Character> convertABCToCharacterList(String abc) {

        List<Character> alphabet = new ArrayList<>();

        abc = abc.toUpperCase();
        for (int i = 0; i < abc.length(); i++) {
            alphabet.add(abc.charAt(i));

        }

        return alphabet;
    }


}
