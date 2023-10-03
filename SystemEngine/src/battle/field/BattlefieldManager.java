package battle.field;

import users.Allies;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BattlefieldManager {
    private Map<String, Battlefield> battleNameToBattleFieldMap = new HashMap<>();
    private List<String> battleNameList = new ArrayList<>();
    private List<Battlefield> battlefieldList = new ArrayList<>();

    public List<String> getBattlefieldNameList() {
        return battleNameList;
    }

    public Battlefield getBattlefieldByBattlefieldName(String battleName) {
        return battleNameToBattleFieldMap.get(battleName);
    }

    public List<Battlefield> getBattlefieldList(){
        return battlefieldList;
    }

    public void addBattlefield(Battlefield battlefield) {
        String battleName = battlefield.getBattlefieldName();

        battleNameList.add(battleName);
        battlefieldList.add(battlefield);
        battleNameToBattleFieldMap.put(battleName, battlefield);
    }

    public void removeBattlefield(String battlefieldName) {
        Battlefield battlefield = getBattlefieldByBattlefieldName(battlefieldName);
        List<Allies> alliesList = battlefield.getAlliesList();

        alliesList.forEach(Allies::leaveBattlefield);
        battleNameList.remove(battlefieldName);
        battlefieldList.remove(battlefield); //need to check this
        battleNameToBattleFieldMap.remove(battlefieldName);
    }
}
