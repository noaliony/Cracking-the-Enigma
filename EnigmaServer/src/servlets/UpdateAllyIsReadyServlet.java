package servlets;

import battle.field.Battlefield;
import battle.field.BattlefieldManager;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import users.Allies;
import users.UserManager;
import utils.ServletUtils;

import java.io.IOException;

import static constants.Constants.USERNAME;

@WebServlet(name = "UpdateAllyIsReadyServlet", value = "/update-ally-is-ready")
public class UpdateAllyIsReadyServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        BattlefieldManager battleFieldManager = ServletUtils.getBattlefieldManager(getServletContext());
        UserManager userManager = ServletUtils.getUserManager(getServletContext());
        String userName = (String) request.getSession().getAttribute(USERNAME);
        Allies ally = userManager.getAllyByUserName(userName);
        Battlefield battleField = battleFieldManager.getBattlefieldByBattlefieldName(ally.getBattlefieldName());

        if (ally.getAgentsCount() == 0) {
            response.getWriter().println("Before ready - at list one agent must to connect to your team, please wait.");
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        } else {
            ally.setIsReady(true);
            battleField.handleCaseIfAllUsersAreReady();
            response.setStatus(HttpServletResponse.SC_OK);
        }
    }
}
