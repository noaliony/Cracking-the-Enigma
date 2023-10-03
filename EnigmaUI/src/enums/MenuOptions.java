package enums;

public enum MenuOptions {
    NOSELECTION(0), READFILE(1), SHOWMACHINESPECIFICATION(2),
    MANUALSETTING(3), AUTOMATICSETTING(4), INPUTPROCESSING(5),
    RESETCURRENTCODE(6), HISTORYANDSTATISTICS(7), SAVETOFILE(8),
    LOADFROMFILE(9), EXIT(10);

    private int userSelection;

    private MenuOptions(){}
    private MenuOptions(final int userSelection){
        this.userSelection = userSelection;
    }

    public int getUserSelection(){
        return userSelection;
    }

    public static MenuOptions convertFromStringToMenuOptions(String menuOptions) {

        MenuOptions result = null;

        switch (menuOptions)
        {
            case "1":
                result = MenuOptions.READFILE;
                break;
            case "2":
                result = MenuOptions.SHOWMACHINESPECIFICATION;
                break;
            case "3":
                result = MenuOptions.MANUALSETTING;
                break;
            case "4":
                result = MenuOptions.AUTOMATICSETTING;
                break;
            case "5":
                result = MenuOptions.INPUTPROCESSING;
                break;
            case "6":
                result = MenuOptions.RESETCURRENTCODE;
                break;
            case "7":
                result = MenuOptions.HISTORYANDSTATISTICS;
                break;
            case "8":
                result = MenuOptions.SAVETOFILE;
                break;
            case "9":
                result = MenuOptions.LOADFROMFILE;
                break;
            case "10":
                result = MenuOptions.EXIT;
                break;
        }

        return result;
    }



}
