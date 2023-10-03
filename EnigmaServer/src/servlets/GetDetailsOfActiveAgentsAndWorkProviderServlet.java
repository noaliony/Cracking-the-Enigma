package servlets;

import battle.field.Battlefield;
import battle.field.BattlefieldManager;
import com.google.gson.Gson;
import dto.AlliesDetails;
import dto.TasksResultsDetails;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import users.Agent;
import users.Allies;
import users.UBoat;
import users.UserManager;
import utils.ServletUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static constants.Constants.USERNAME;

@WebServlet(name = "getDetailsOfActiveAgentsAndWorkProviderServlet", value = "/get-details-of-active-agents-and-work-provider")
public class GetDetailsOfActiveAgentsAndWorkProviderServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        UserManager userManager = ServletUtils.getUserManager(getServletContext());
        String userName = (String) request.getSession().getAttribute(USERNAME);
        Allies ally = userManager.getAllyByUserName(userName);
        List<Agent> agentList = ally.getAgentList();
        List<TasksResultsDetails> tasksResultsDetailsList = new ArrayList<>();
        Gson gson = new Gson();

        agentList.forEach(currentAgent -> tasksResultsDetailsList.add(currentAgent.getNewTasksResultsDetails()));
        response.getWriter().println(gson.toJson(tasksResultsDetailsList));
        response.setStatus(HttpServletResponse.SC_OK);

    }
}
