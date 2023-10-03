package components;
import enums.ReflectorID;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Repository implements Serializable, Cloneable {

    private List<Rotor> rotorsList;
    private List<Integer> rotorsNotch;
    private List<Reflector> reflectorsList;

    public Rotor getRotorByID(int ID) {

        for(Rotor currentRotor : rotorsList) {
            if(currentRotor.getID() == ID)
                return currentRotor;
        }
        return null;
    }

    public Reflector getReflectorByID(ReflectorID ID) {

        for(Reflector currentReflector : reflectorsList) {
            if(currentReflector.getID().equals(ID))
                return currentReflector;
        }
        return null;
    }

    public void setRotorsList(List<Rotor> rotorsList) {

        this.rotorsList = rotorsList;
    }

    public void setReflectorsList(List<Reflector> reflectorsList) {

        this.reflectorsList = reflectorsList;
    }

    @Override
    public String toString()
    {
        String result = new String();

        for (Rotor currRotor : rotorsList)
            result = result.concat(currRotor.toString());

        for (Reflector currReflector : reflectorsList)
            result = result.concat((currReflector.toString()));

        return result;
    }

    public List<Rotor> getRotorsList() {

        return rotorsList;
    }

    public int getAmountOfReflectorsInRepository() {

        return reflectorsList.size();
    }

    public int getAmountOfRotors() {
        return rotorsList.size();
    }

    public List<ReflectorID> getReflectorIDsList() {
        List<ReflectorID> reflectorIDsList = new ArrayList<>();

        reflectorsList.forEach(reflector -> reflectorIDsList.add(reflector.getID()));

        return reflectorIDsList;
    }

    @Override
    public Repository clone() {
        try {
            Repository clone = (Repository) super.clone();

            clone.rotorsList = new ArrayList<>();
            rotorsList.forEach(rotor -> clone.rotorsList.add(rotor.clone()));

            return clone;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}
