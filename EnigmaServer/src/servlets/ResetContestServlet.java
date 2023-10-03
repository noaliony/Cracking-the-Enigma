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
import java.util.ArrayList;
import java.util.List;

import static constants.Constants.UBOAT;

@WebServlet(name = "ResetContestServlet", urlPatterns = {"/reset-contest"})
public class ResetContestServlet extends HttpServlet  {
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

                    ally.getAgentList().forEach(agent -> agent.setResetDataRequested(true));
                    joinedBattleName = ally.getBattlefieldName();
                    break;
            }

            if (joinedBattleName != null) {
                BattlefieldManager battleFieldManager = ServletUtils.getBattlefieldManager(getServletContext());
                Battlefield battlefield;

                synchronized (getServletContext()) {
                    battlefield = battleFieldManager.getBattlefieldByBattlefieldName(joinedBattleName);
                    List<Allies> alliesToRemoveList = new ArrayList<>();

                    battlefield.getAlliesList().forEach(ally -> {
                        alliesToRemoveList.add(ally);
                        ally.leaveBattlefield();
                    });

                    alliesToRemoveList.forEach(battlefield::removeAlly);
                }
            }
        } else {
            response.getWriter().println("User is not logged in!");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }
    }
}
