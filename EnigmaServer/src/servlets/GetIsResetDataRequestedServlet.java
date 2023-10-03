package servlets;

import battle.field.Battlefield;
import battle.field.BattlefieldManager;
import com.google.gson.Gson;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import users.Agent;
import users.Allies;
import users.UBoat;
import users.UserManager;
import utils.ServletUtils;
import utils.SessionUtils;

import java.io.IOException;

import static constants.Constants.AGENT;
import static constants.Constants.UBOAT;

@WebServlet(name = "GetIsResetDataRequestedServlet", value = "/is-reset-data-requested")
public class GetIsResetDataRequestedServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String usernameFromSession = SessionUtils.getUsername(request);
        String userTypeFromSession = SessionUtils.getUserType(request);
        UserManager userManager = ServletUtils.getUserManager(getServletContext());

        if (usernameFromSession != null) {
            if (AGENT.equals(userTypeFromSession)) {
                Agent agent = userManager.getAgentByUserName(usernameFromSession);
                boolean resetDataRequested = agent.isResetDataRequested();

                if (resetDataRequested) {
                    agent.setResetDataRequested(false);
                }

                response.getWriter().println(new Gson().toJson(resetDataRequested));
                response.setStatus(HttpServletResponse.SC_OK);
            } else {
                response.getWriter().println("Only agent can access this!");
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            }
        } else {
            response.getWriter().println("User is not logged in!");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }
    }
}
