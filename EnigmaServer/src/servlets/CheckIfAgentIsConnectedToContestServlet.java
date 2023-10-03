package servlets;

import com.google.gson.Gson;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import users.Agent;
import users.UserManager;
import utils.ServletUtils;
import utils.SessionUtils;

import java.io.IOException;

@WebServlet(name = "CheckIfAgentIsConnectedToContestServlet", value = "/check-if-agent-is-connected-to-contest")
public class CheckIfAgentIsConnectedToContestServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        UserManager userManager = ServletUtils.getUserManager(getServletContext());
        String agentName = SessionUtils.getUsername(request);
        Agent agent = userManager.getAgentByUserName(agentName);
        boolean isAgentConnectedToContest = agent.checkIfAgentIsConnectedToContest();
        Gson gson = new Gson();

        response.getWriter().println(gson.toJson(isAgentConnectedToContest));
        response.setStatus(HttpServletResponse.SC_OK);
    }
}
