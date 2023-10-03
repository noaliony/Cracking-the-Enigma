package servlets;

import com.google.gson.Gson;
import dto.TaskDetails;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import users.Agent;
import users.Allies;
import users.UserManager;
import utils.ServletUtils;

import java.io.IOException;
import java.util.List;

import static constants.Constants.*;

@WebServlet(name = "GetTasksDetailsServlet", value = "/get-tasks-details")
public class GetTasksDetailsServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        UserManager userManager = ServletUtils.getUserManager(getServletContext());
        String userName = (String) request.getSession().getAttribute(USERNAME);
        Agent agent = userManager.getAgentByUserName(userName);
        String allyName = agent.getAllyName();
        Allies ally = userManager.getAllyByUserName(allyName);
        int tasksCount = Integer.parseInt(request.getParameter(TASKS_COUNT));
        Gson gson = new Gson();

        try {
            List<TaskDetails> taskDetailsList = ally.getListOfTaskDetails(tasksCount);
            String taskDetailsListString = gson.toJson(taskDetailsList);

            agent.setPulledTasksCount(taskDetailsList.size());
            response.getWriter().println(taskDetailsListString);
            response.setStatus(HttpServletResponse.SC_OK);
        } catch (Exception exception) {
            response.getWriter().println(exception.getMessage());
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
    }
}
