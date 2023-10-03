package components;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Rotor implements Serializable, Cloneable {

    private int notch;
    private int startingNotch;
    private int length;
    private List<Character> leftArray = new ArrayList<>();
    private List<Character> rightArray = new ArrayList<>();
    private int startPosition;
    private int currentPosition;
    private int id;

    public int getIndexInRightArrayByChar(char charToFind) {
        return rightArray.indexOf(charToFind) ;
    }

    public void setStartPosition(int startPosition) {
        this.startPosition = startPosition;
    }

    public void setCurrPosition(int currentPosition) {
        this.currentPosition = currentPosition;
    }

    public int getID() {
        return this.id;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public void setNotch(Integer notch) {
        this.notch = notch;
    }

    public void setStartingNotch(Integer startingNotch) {
        this.startingNotch = startingNotch;
    }

    public void setLeftArray(List<Character> leftArray) {
        this.leftArray = leftArray;
    }

    public void setRightArray(List<Character> rightArray) {
        this.rightArray = rightArray;
    }

    public void setID(Integer id) {
        this.id = id;
    }

    public int getCurrPosition() {
        return this.currentPosition;
    }

    public int getStartPosition() {
        return this.startPosition;
    }

    public void advanceCurrPosition (){
        currentPosition = (currentPosition + 1) % length;
        if(currentPosition == 0)
            currentPosition = 1;
    }

    public void decreaseNotch (){
        notch = (notch - 1) % length;
        if (notch == 0)
            notch = length - 1;
    }

    @Override
    public String toString()
    {
        final String newLine = System.lineSeparator(); // Maybe - replace '\n'
        String result = "";

        result = "Rotor #" + id +":" + "\n";
        result = result.concat("left: ");
        for (Character currChar : leftArray)
            result = result.concat(currChar + " ");
        result = result.concat("\n" + "right: ");
        for (Character currChar : rightArray)
            result = result.concat(currChar + " ");
        result = result.concat("\n" + "notch: " + notch + "\n");

        return result;
    }

    public int getNotch() {
        return notch;
    }

    public List<Character>  getRightArray() {
        return rightArray;
    }

    public List<Character> getLeftArray() {
        return leftArray;
    }

    public int getStartingNotch() {

        return startingNotch;
    }

    @Override
    public Rotor clone() {
        try {
            Rotor clone = (Rotor) super.clone();

            return clone;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}
