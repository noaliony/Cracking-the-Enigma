package servlets;

import com.google.gson.Gson;
import dto.AgentDetails;
import dto.StringDecryptedCandidate;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import users.Agent;
import users.Allies;
import users.UserManager;
import utils.ServletUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static constants.Constants.USERNAME;

@WebServlet(name = "GetTeamsAgentDataServlet", value = "/get-teams-agent-data")
public class GetTeamsAgentDataServlet extends HttpServlet {
    private List<Agent> agentList = new ArrayList<>();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        UserManager userManager = ServletUtils.getUserManager(getServletContext());
        String userName = (String) request.getSession().getAttribute(USERNAME);
        Allies ally = userManager.getAllyByUserName(userName);
        List<Agent> AgentListFromAlly = ally.getAgentList();
        List<AgentDetails> deltaAgentDetailsList = new ArrayList<>();
        Gson gson = new Gson();
        List<Agent> deltaAgentList;

        deltaAgentList = calculateTheDeltaBetweenTwoLists(AgentListFromAlly, agentList);
        agentList = AgentListFromAlly;
        deltaAgentList.forEach(currentAlly -> deltaAgentDetailsList.add(currentAlly.getNewAgentDetails()));
        response.getWriter().println(gson.toJson(deltaAgentDetailsList));
        response.setStatus(HttpServletResponse.SC_OK);
    }

    private List<Agent> calculateTheDeltaBetweenTwoLists(List<Agent> AgentListFromAllies,
                                                         List<Agent> agentList) {
        return AgentListFromAllies
                .stream()
                .filter(currentAgent -> !agentList.contains(currentAgent))
                .collect(Collectors.toList());
    }
}
