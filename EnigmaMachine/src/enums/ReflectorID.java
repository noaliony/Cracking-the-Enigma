package enums;

import java.util.ArrayList;
import java.util.List;

public enum ReflectorID {

    I, II, III, IV, V;

    public static final int size = ReflectorID.values().length;

    public static ReflectorID convertStringToReflectorID(String ID) {

        ReflectorID result = null;

        switch (ID)
        {
            case "I":
            case "1":
                result = ReflectorID.I;
                break;
            case "II":
            case "2":
                result = ReflectorID.II;
                break;
            case "III":
            case "3":
                result = ReflectorID.III;
                break;
            case "IV":
            case "4":
                result = ReflectorID.IV;
                break;
            case "V":
            case "5":
                result = ReflectorID.V;
                break;
        }

        return result;
    }

    public static int convertStringToInt(String ID) {

        int result = 0;

        switch (ID)
        {
            case "I":
                result = 1;
                break;
            case "II":
                result = 2;
                break;
            case "III":
                result = 3;
                break;
            case "IV":
                result = 4;
                break;
            case "V":
                result = 5;
                break;
        }

        return result;
    }

    public static String convertIntegerToString(Integer ID) {

        String result = "";

        switch (ID)
        {
            case 1:
                result = ReflectorID.I.toString();
                break;
            case 2:
                result = ReflectorID.II.toString();
                break;
            case 3:
                result = ReflectorID.III.toString();
                break;
            case 4:
                result = ReflectorID.IV.toString();
                break;
            case 5:
                result = ReflectorID.V.toString();
                break;
        }

        return result;
    }

    public static ReflectorID convertIntToReflectorID(int ID) {

        ReflectorID result = null;

        switch (ID)
        {
            case 1:
                result = ReflectorID.I;
                break;
            case 2:
                result = ReflectorID.II;
                break;
            case 3:
                result = ReflectorID.III;
                break;
            case 4:
                result = ReflectorID.IV;
                break;
            case 5:
                result = ReflectorID.V;
                break;
        }

        return result;
    }
}
