package components;
import enums.ReflectorID;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Reflector implements Serializable {

    private List<Integer> reflectorArray = new ArrayList<>();
    private ReflectorID id;

    public ReflectorID getID() {
        return this.id;
    }

    public void setID(ReflectorID reflectorID) {
        this.id = reflectorID;
    }

    public void setReflectorArray(List<Integer> reflectorArray) {
        this.reflectorArray = reflectorArray;
    }

    @Override
    public String toString()
    {
        String result = new String();

        result = "Reflector #" + id +":" + "\n";
        for (Integer currInt : reflectorArray)
            result = result.concat(currInt + " ");

        return result;
    }

    public int getDataByIndex(int newIndexReflector) {

        int numToSearch = reflectorArray.get(newIndexReflector);

        for (int i = 0; i < reflectorArray.size(); i++)
        {
            if (reflectorArray.get(i) == numToSearch && i != newIndexReflector)
                return i;
        }

        return  -1;
    }
}
