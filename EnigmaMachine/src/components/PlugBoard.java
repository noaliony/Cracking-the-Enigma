package components;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlugBoard implements Serializable, Cloneable {

    private Map<Character, Character> plugPairs;

    public PlugBoard(List<Character> alphabet) {

        plugPairs = new HashMap<>();

        for (Character currentChar : alphabet) {

            plugPairs.put(currentChar, currentChar);
        }
    }

    public void SetPlugPairs(Map<Character, Character> plugPairs) {
        this.plugPairs = plugPairs;
    }

    public char getValueByKey(char keyInput) {
        return plugPairs.get(keyInput);
    }

    public Map<Character, Character> getPlugPairs() {

        return plugPairs;
    }

    @Override
    public PlugBoard clone() {
        try {
            PlugBoard clone = (PlugBoard) super.clone();

            clone.plugPairs = new HashMap<>(plugPairs);

            return clone;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}
