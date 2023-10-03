package xml;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import enums.ReflectorID;
import exceptions.*;
import generated.*;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.*;

public class ReadXML {

    private static String JAXB_XML_PACKAGE_NAME = "generated";
    private static CTEEnigma cteEnigma;

    public static void fromXMLFileToObject(String pathXML) {

        try {
            InputStream inputStream = new FileInputStream(new File(pathXML));
            cteEnigma = deserializeFrom(inputStream);

        } catch (JAXBException | FileNotFoundException exception) {
            exception.printStackTrace();
        }
    }

    private static CTEEnigma deserializeFrom (InputStream inputStream) throws JAXBException{

        JAXBContext jaxbContext = JAXBContext.newInstance(JAXB_XML_PACKAGE_NAME);
        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
        return (CTEEnigma) unmarshaller.unmarshal(inputStream);
    }

    public static boolean checkIfValidXMLFile(String pathXML) throws EnigmaLogicException {

        Path path = Paths.get(pathXML);
        boolean isValid = checkIfXMLFileExist(path);

        isValid &= checkIfXMLFile(pathXML);
        fromXMLFileToObject(pathXML);
        isValid &= CTEMachineValidator();

        return isValid;
    }

    private static boolean CTEMachineValidator() throws EnigmaLogicException {

        boolean resultValidator = checkIfDecipherPartExistInXMLFile();

        resultValidator &= checkIfABCIsEvenAmount();
        resultValidator &= checkIfRotorsCountIsValid();
        resultValidator &= checkIfEveryRotorHaveUniqueID();
        resultValidator &= checkIfThereAreNoDoubleMappingsInTheRotors();
        resultValidator &= checkIfTheNotchIsValidInEveryRotor();
        resultValidator &= checkIfEveryReflectorHaveUniqueID();
        resultValidator &= checkIfThereIsNoMappingBetweenLetterAndItselfInEveryReflector();
        resultValidator &= checkIfAgentsCountIsValid();

        return resultValidator;
    }

    private static boolean checkIfAgentsCountIsValid() throws InvalidAgentsCountException {
        int agentsCount = cteEnigma.getCTEDecipher().getAgents();

        if (agentsCount < 2 || agentsCount > 50){
            throw new InvalidAgentsCountException();
        }

        return true;
    }

    private static boolean checkIfDecipherPartExistInXMLFile() throws DecipherPartDoesNotExistInXMLFileException {
        if (cteEnigma.getCTEDecipher() == null){
            throw new DecipherPartDoesNotExistInXMLFileException();
        }

        return true;
    }

    private static boolean checkIfThereIsNoMappingBetweenLetterAndItselfInEveryReflector() throws ThereIsMappingBetweenLetterAndItselfInReflectorException, InvalidRelectorMapped, InvalidCountReflectorMapped {

        List<CTEReflector> CTEReflectList = cteEnigma.getCTEMachine().getCTEReflectors().getCTEReflector();
        int countAlphabet = cteEnigma.getCTEMachine().getABC().trim().length();

        for (CTEReflector currentCTEReflector : CTEReflectList) {

            List<CTEReflect> reflectListForCurrentReflector = currentCTEReflector.getCTEReflect();
            List<Integer> reflectorListInputOutput = new ArrayList<>();

            for (CTEReflect currentCTEReflect : reflectListForCurrentReflector) {

                int input = currentCTEReflect.getInput();
                int output = currentCTEReflect.getOutput();

                 if (input == output)
                    throw new ThereIsMappingBetweenLetterAndItselfInReflectorException(currentCTEReflector.getId());

                 if (input < 1 || input > countAlphabet || output < 1 || output > countAlphabet)
                     throw new InvalidRelectorMapped(currentCTEReflector.getId());

                if (reflectorListInputOutput.contains(input) == true || reflectorListInputOutput.contains(output) == true)
                    throw new ThereIsMappingBetweenLetterAndItselfInReflectorException(currentCTEReflector.getId());

                 reflectorListInputOutput.add(input);
                 reflectorListInputOutput.add(output);
            }

            if (reflectorListInputOutput.size() != countAlphabet)
                throw new InvalidCountReflectorMapped(currentCTEReflector.getId());

        }

        return true;
    }

    private static boolean checkIfEveryReflectorHaveUniqueID() throws ReflectorIDIsNotValidException, ReflectorsHaveNotUniqueIDException, ThereAreNoExistAllReflectorsException {

        List<CTEReflector> CTEReflectorList = cteEnigma.getCTEMachine().getCTEReflectors().getCTEReflector();
        Set<ReflectorID> reflectorsID = new HashSet<>();
        List<Integer> reflectorsIDList = new ArrayList<>();
        String idReflectorString = "";

        for (CTEReflector currentCTEReflector : CTEReflectorList) {

            idReflectorString = currentCTEReflector.getId();
            ReflectorID reflectorID = ReflectorID.convertStringToReflectorID(idReflectorString);

            if (reflectorsID == null)
                throw new ReflectorIDIsNotValidException(idReflectorString);
            reflectorsIDList.add(ReflectorID.convertStringToInt(idReflectorString));
            reflectorsID.add(reflectorID);
        }

        if (reflectorsID.size() != CTEReflectorList.size())
              throw new ReflectorsHaveNotUniqueIDException();
        Collections.sort(reflectorsIDList);
        for (int i = 0; i < reflectorsIDList.size() - 1; i++) {

            if (reflectorsIDList.get(i+1) - reflectorsIDList.get(i) > 1) {
                ReflectorID bound = ReflectorID.convertStringToReflectorID(reflectorsIDList.get(reflectorsIDList.size() - 1).toString());
                throw new ThereAreNoExistAllReflectorsException(bound.toString());
            }
        }
        return true;
    }

    private static boolean checkIfTheNotchIsValidInEveryRotor() throws NotchInRotorIsNotValidException {

        List<CTERotor> CTERotorList = cteEnigma.getCTEMachine().getCTERotors().getCTERotor();
        int countAlphabet = cteEnigma.getCTEMachine().getABC().trim().length();

        for (CTERotor currentCTERotor : CTERotorList) {

            if (currentCTERotor.getNotch() < 1 || currentCTERotor.getNotch() > countAlphabet)
                throw new NotchInRotorIsNotValidException(currentCTERotor.getId());
        }

        return true;
    }

    private static boolean checkIfThereAreNoDoubleMappingsInTheRotors() throws RotorHaveDoubleMappingsException, RotorHaveTooMuchMappingsException, RotorHaveLessMappingsException {

        List<CTERotor> CTERotorList = cteEnigma.getCTEMachine().getCTERotors().getCTERotor();
        int countAlphabet = cteEnigma.getCTEMachine().getABC().trim().length();

        for (CTERotor currentCTERotor : CTERotorList) {

            List<Character> rightArrayForSingleRotor = new ArrayList<>();
            List<Character> leftArrayForSingleRotor = new ArrayList<>();

            createRightAndLeftListsForSingleRotor(currentCTERotor, rightArrayForSingleRotor, leftArrayForSingleRotor);

            if ((rightArrayForSingleRotor.size() < countAlphabet) || (leftArrayForSingleRotor.size() < countAlphabet)) {
                throw new RotorHaveLessMappingsException(currentCTERotor.getId());
            }

            if ((rightArrayForSingleRotor.size() > countAlphabet) || (leftArrayForSingleRotor.size() > countAlphabet)) {
                throw new RotorHaveTooMuchMappingsException(currentCTERotor.getId());
            }
            if (rightArrayForSingleRotor.stream().distinct().count() != countAlphabet ||
                    leftArrayForSingleRotor.stream().distinct().count() != countAlphabet) {
                throw new RotorHaveDoubleMappingsException(currentCTERotor.getId());
            }
        }
        return true;
    }
    public static void createRightAndLeftListsForSingleRotor(CTERotor currentCTERotor, List<Character> rightArrayForSingleRotor,
                                                             List<Character> leftArrayForSingleRotor ) {

        for (CTEPositioning currentCTEPositioning : currentCTERotor.getCTEPositioning()) {

            String right = currentCTEPositioning.getRight().toUpperCase();
            String left = currentCTEPositioning.getLeft().toUpperCase();


            rightArrayForSingleRotor.add(right.charAt(0));
            leftArrayForSingleRotor.add(left.charAt(0));
        }
    }

    private static boolean checkIfEveryRotorHaveUniqueID() throws RotorIDIsNotValidException, RotorsHaveNotUniqueIDException, ThereAreNoExistAllRotorsException {

        List<CTERotor> CTERotorList = cteEnigma.getCTEMachine().getCTERotors().getCTERotor();
        Set<Integer> rotorsID = new HashSet<>();
        List<Integer> rotorsIDList = new ArrayList<>();

        int idRotor;

        for (CTERotor currentCTERotor : CTERotorList) {

            idRotor = currentCTERotor.getId();
            if (idRotor < 1)
                throw new RotorIDIsNotValidException(currentCTERotor.getId());
            rotorsIDList.add(idRotor);
            rotorsID.add(idRotor);
        }

        if (rotorsID.size() != CTERotorList.size())
            throw new RotorsHaveNotUniqueIDException();
        Collections.sort(rotorsIDList);
        for (int i = 0; i < rotorsIDList.size() - 1; i++) {

            if (rotorsIDList.get(i+1) - rotorsIDList.get(i) > 1)
                throw new ThereAreNoExistAllRotorsException(rotorsIDList.get(rotorsIDList.size() - 1));
        }

        return true;
    }

    private static boolean checkIfRotorsCountIsValid() throws RotorsCountSmallerThanTwoException,
            RotorsCountBiggerThanReceivedRotorsException, InvalidNumberOfRotorsException {

        CTEMachine cteMachine = cteEnigma.getCTEMachine();

        if (cteMachine.getRotorsCount() < 2)
           throw new RotorsCountSmallerThanTwoException();

        if(cteMachine.getRotorsCount() > 99)
            throw new InvalidNumberOfRotorsException();

        if (cteMachine.getRotorsCount() > cteMachine.getCTERotors().getCTERotor().size())
            throw new RotorsCountBiggerThanReceivedRotorsException();

        return true;
    }

    private static boolean checkIfABCIsEvenAmount() throws ABCAmountIsNotEvenException {

        if (cteEnigma.getCTEMachine().getABC().trim().length() % 2 == 1)
            throw new ABCAmountIsNotEvenException();

        return true;
    }

    private static boolean checkIfXMLFile(String pathXML) throws FileISNotXMLException {

        boolean isXMLFile = pathXML.endsWith(".xml");

        if (!isXMLFile)
            throw new FileISNotXMLException();

        return true;
    }

    private static boolean checkIfXMLFileExist(Path pathXML) throws FileIsNotExistException {

        boolean existXMLFile = Files.exists(pathXML);

        if (!existXMLFile)
            throw new FileIsNotExistException();

        return true;
    }

    public static CTEEnigma getCTEEnigma() {

        return cteEnigma;
    }

}
