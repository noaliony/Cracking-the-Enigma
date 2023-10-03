package users;

import decryption.manager.DecryptionManager;
import dto.AlliesDetails;
import dto.TaskDetails;

import java.util.ArrayList;
import java.util.List;

public class Allies {

    private String userName;
    private boolean isReady;
    private int agentsCount;
    private int taskSize;
    private String battlefieldName;
    private List<Agent> agentList = new ArrayList<>();
    private List<Agent> standbyAgentsList = new ArrayList<>();
    private DecryptionManager DM;

    public Allies (String userName){ this.userName = userName; }

    public boolean isReady() {
        return isReady;
    }

    public String getUserName() {
        return userName;
    }

    public List<Agent> getAgentList() {
        return agentList;
    }

    public AlliesDetails getNewAllieDetails() {
        return new AlliesDetails(userName, agentList.size(), taskSize, isReady);
    }

    public void addAgentToList(Agent agent) {
        agentList.add(agent);
    }

    public void setDM(DecryptionManager DM) {
        this.DM = DM;
    }

    public void runDM() {
        DM.runDM();
    }

    public List<TaskDetails> getListOfTaskDetails(int tasksCount) throws RuntimeException{
        return DM.getListOfTaskDetails(tasksCount);
    }

    public String getBattlefieldName() {
        return battlefieldName;
    }

    public Integer getCreatedTasksCount() {
        if (DM == null) {
            return 0;
        }
        return DM.getCreatedTasksCount();
	}
	
    public void setBattlefieldName(String battlefieldName) {
        this.battlefieldName = battlefieldName;
    }

    public void setIsReady(boolean ready) {
        isReady = ready;
    }

    public void leaveBattlefield() {
        setBattlefieldName(null);
        setIsReady(false);
        taskSize = 0;
        agentList.forEach(Agent::leaveBattlefield);
        agentList.addAll(standbyAgentsList);
        standbyAgentsList.clear();
    }

    public void removeAgent(Agent agent) {
        agentList.remove(agent);
        // MAYBE - unready if this was only agent
    }

    public int getAgentsCount() {
        return agentList.size();
    }

    public Integer getTasksCount() {
        return DM.getTasksCount();
    }

    public void setTaskSize(int taskSize) {
        this.taskSize = taskSize;
    }

    public void addStandbyAgent(Agent agent) {
        standbyAgentsList.add(agent);
    }
    
    public void updateBattlefieldNameInAllConnectedAgents() {
        agentList.forEach(agent -> agent.setBattlefieldName(battlefieldName));
    }
}
