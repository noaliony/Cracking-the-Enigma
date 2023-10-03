package servlets;

import battle.field.Battlefield;
import battle.field.BattlefieldManager;
import com.google.gson.Gson;
import dto.StringDecryptedCandidate;
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

import static constants.Constants.*;
import static constants.Constants.AGENT;

@WebServlet(name = "GetWinnerStringServlet", value = "/get-winner-string")
public class GetWinnerStringServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        UserManager userManager = ServletUtils.getUserManager(getServletContext());
        String userType = SessionUtils.getUserType(request);
        String userName = (String) request.getSession().getAttribute(USERNAME);
        String battleName = "";

        if (userType.equals(UBOAT)) {
            UBoat uBoat = userManager.getUBoatByUserName(userName);

            battleName = uBoat.getBattlefieldName();
        } else if (userType.equals("allies")) {
            Allies allies = userManager.getAllyByUserName(userName);

            battleName = allies.getBattlefieldName();

        } else if (userType.equals(AGENT)) {
            Agent agent = userManager.getAgentByUserName(userName);

            battleName = agent.getBattlefieldName();
        }

        BattlefieldManager battleFieldManager = ServletUtils.getBattlefieldManager(getServletContext());
        Battlefield battleField = battleFieldManager.getBattlefieldByBattlefieldName(battleName);
        StringDecryptedCandidate stringDecryptedCandidate = battleField.getWinnerString();
        Gson gson = new Gson();

        response.getWriter().println(gson.toJson(stringDecryptedCandidate));
        response.setStatus(HttpServletResponse.SC_OK);
    }
}
