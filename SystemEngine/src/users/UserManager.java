package users;

import java.util.*;

public class UserManager {

    private final Set<String> usersSet;
    private Map<String,UBoat> userNameToUBoatMap = new HashMap<>();
    private Map<String,Allies> userNameToAlliesMap = new HashMap<>();
    private Map<String,Agent> userNameToAgentMap = new HashMap<>();
    private List<String> allAlliesNamesList = new ArrayList<>();

    public UserManager() {
        usersSet = new HashSet<>();
    }

    public synchronized void addUser(String username, String userType) {
        usersSet.add(username);

        switch (userType){
            case "uBoat":
                userNameToUBoatMap.put(username, new UBoat(username));
                break;
            case "allies":
                userNameToAlliesMap.put(username, new Allies(username));
                allAlliesNamesList.add(username);
                break;
        }
    }

    public synchronized void addUser(String username, String allieName, int threadsCount, int tasksCountToPull, String userType) {
        usersSet.add(username);

        if (userType.equals("agent")) {
            Agent agent = new Agent(username, allieName, threadsCount, tasksCountToPull);

            userNameToAgentMap.put(username, agent);
        }
    }

    public synchronized void removeUser(String username, String userType) {
        usersSet.remove(username);
        switch (userType){
            case "uBoat":
                removeUBoatByUserName(username);
                break;
            case "allies":
                removeAllyByUserName(username);
                break;
            case "agent":
                removeAgentByUserName(username);
                break;
        }
    }

    public boolean isUserExists(String username) {
        return usersSet.contains(username);
    }

    public UBoat getUBoatByUserName(String userName){
        return userNameToUBoatMap.get(userName);
    }

    public Agent getAgentByUserName(String userName){
        return userNameToAgentMap.get(userName);
    }

    public Allies getAllyByUserName(String userName){
        return userNameToAlliesMap.get(userName);
    }

    public void removeUBoatByUserName(String userName) {
        userNameToUBoatMap.remove(userName);
    }

    public void removeAgentByUserName(String userName) {
        userNameToAgentMap.remove(userName);
    }

    public void removeAllyByUserName(String userName) {
        userNameToAlliesMap.remove(userName);
        allAlliesNamesList.remove(userName);
    }

    public List<String> getAllAlliesNamesList() {
        return allAlliesNamesList;
    }
}