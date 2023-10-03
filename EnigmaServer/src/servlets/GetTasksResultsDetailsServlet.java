package servlets;

import com.google.gson.Gson;
import dto.TasksResultsDetails;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import users.Agent;
import users.UserManager;
import utils.ServletUtils;

import java.io.IOException;

import static constants.Constants.USERNAME;

@WebServlet(name = "GetTasksResultsDetailsServlet", value = "/get-tasks-results-details")
public class GetTasksResultsDetailsServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        UserManager userManager = ServletUtils.getUserManager(getServletContext());
        String agentName = (String) request.getSession().getAttribute(USERNAME);
        Agent agent = userManager.getAgentByUserName(agentName);
        TasksResultsDetails tasksResultsDetails = agent.getNewTasksResultsDetails();
        Gson gson = new Gson();

        response.getWriter().println(gson.toJson(tasksResultsDetails));
        response.setStatus(HttpServletResponse.SC_OK);
    }
}
