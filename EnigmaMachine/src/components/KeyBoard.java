package components;

import machine.Machine;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class KeyBoard implements Serializable {

    private List<Character> alphabet = new ArrayList<>();
    int length;

    public void setAlphabet(List<Character> alphabet) {
        this.alphabet = alphabet;
        this.length = alphabet.size();
    }

    @Override
    public String toString()
    {
        String result = "";

        result = result.concat("alphabet: ");
        for (Character currChar : alphabet)
            result = result.concat(currChar + " ");
        result = result.concat(System.lineSeparator());

        return result;
    }

    public int getLength() {
        return length;
    }

    public int getIndexByCharFromAlphabetList (char newChar) {
        return alphabet.indexOf(newChar);
    }

    public char getCharByIndexFromAlphabetList(int indexToKeyBoard) {

        return  alphabet.get(indexToKeyBoard);
    }

    public List <Character> getAlphabet(){
        return alphabet;
    }
}
