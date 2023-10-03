package servlets;

import battle.field.Battlefield;
import battle.field.BattlefieldManager;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import users.Agent;
import users.Allies;
import users.UBoat;
import users.UserManager;
import utils.ServletUtils;
import utils.SessionUtils;

import java.io.IOException;

import static constants.Constants.*;

@WebServlet(name = "LogoutServlet", urlPatterns = {"/logout"})
public class LogoutServlet extends HttpServlet  {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String usernameFromSession = SessionUtils.getUsername(request);
        String userTypeFromSession = SessionUtils.getUserType(request);
        UserManager userManager = ServletUtils.getUserManager(getServletContext());

        if (usernameFromSession != null) {
            String joinedBattleName = null;

            switch (userTypeFromSession) {
                case UBOAT:
                    UBoat uBoat = userManager.getUBoatByUserName(usernameFromSession);

                    joinedBattleName = uBoat.getBattlefieldName();
                    break;
                case "allies":
                    Allies ally = userManager.getAllyByUserName(usernameFromSession);

                    joinedBattleName = ally.getBattlefieldName();
                    // MAYBE - logout all connected agents
                    break;
                case AGENT:
                    Agent agent = userManager.getAgentByUserName(userTypeFromSession);
                    Allies allyForAgent = userManager.getAllyByUserName(agent.getAllyName());

                    allyForAgent.removeAgent(agent);
                    joinedBattleName = agent.getBattlefieldName();
                    break;
            }

            if (joinedBattleName != null) {
                BattlefieldManager battleFieldManager = ServletUtils.getBattlefieldManager(getServletContext());
                Battlefield battlefield;

                switch (userTypeFromSession) {
                    case UBOAT:
                        battlefield = battleFieldManager.getBattlefieldByBattlefieldName(joinedBattleName);
                        battlefield.getAlliesList().forEach(Allies::leaveBattlefield);
                        battleFieldManager.removeBattlefield(joinedBattleName);
                        break;
                    case "allies":
                        Allies ally = userManager.getAllyByUserName(usernameFromSession);

                        ally.getAgentList().forEach(Agent::leaveBattlefield);
                        battlefield = battleFieldManager.getBattlefieldByBattlefieldName(joinedBattleName);
                        battlefield.removeAlly(ally);
                        break;
                }
            }

            userManager.removeUser(usernameFromSession, userTypeFromSession);
            SessionUtils.clearSession(request);
        } else {
            response.getWriter().println("User is not logged in!");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }
    }
}
