package servlets;

import battle.field.Battlefield;
import battle.field.BattlefieldManager;
import com.google.gson.Gson;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import users.Allies;
import users.UserManager;
import utils.ServletUtils;

import java.io.IOException;

import static constants.Constants.USERNAME;

@WebServlet(name = "JoinContestServlet", value = "/join-contest")
public class JoinContestServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            synchronized (getServletContext()) {
                UserManager userManager = ServletUtils.getUserManager(getServletContext());
                BattlefieldManager battleFieldManager = ServletUtils.getBattlefieldManager(getServletContext());
                String allyName = (String) request.getSession().getAttribute(USERNAME);
                String battlefieldName = request.getReader().readLine();
                Allies ally = userManager.getAllyByUserName(allyName);
                Gson gson = new Gson();
                Battlefield battlefield = battleFieldManager.getBattlefieldByBattlefieldName(battlefieldName) ;
                boolean isBattlefieldValid = checkValidityToBattlefield(battlefield);

                if (isBattlefieldValid) {
                    ally.setBattlefieldName(battlefieldName);
                    ally.updateBattlefieldNameInAllConnectedAgents();
                    battlefield.addAllyToAlliesList(ally);
                    response.getWriter().println(gson.toJson(battlefield.getNewBattlefieldDetails()));
                    response.setStatus(HttpServletResponse.SC_OK);
                } else {
                    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                }
            }
        } catch (Exception exception) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().println(exception.getMessage());
        }

    }

    private boolean checkValidityToBattlefield(Battlefield battlefield) {
        return (battlefield.getRegisteredAlliesCount() != battlefield.getTotalAlliesCount()) &&
                (battlefield.getTotalAlliesCount() > 0);
    }
}
