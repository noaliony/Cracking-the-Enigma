package components;

import java.util.List;
import java.util.Set;

public class Dictionary {

    private List<Character> excludeChars;
    private Set<String> dictionary;

    public Dictionary(List<Character> excludeChars, Set<String> dictionary){
        this.excludeChars = excludeChars;
        this.dictionary = dictionary;
    }

    public List<Character> getExcludeChars() {
        return excludeChars;
    }

    public Set<String> getDictionarySet() {
        return dictionary;
    }
}
