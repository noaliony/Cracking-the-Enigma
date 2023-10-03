package servlets;

import battle.field.Battlefield;
import battle.field.BattlefieldManager;
import com.google.gson.Gson;
import enums.GameStatus;
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

@WebServlet(name = "GetContestStatusServlet", value = "/get-contest-status")
public class GetContestStatusServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        UserManager userManager = ServletUtils.getUserManager(getServletContext());
        String userName = SessionUtils.getUsername(request);
        String userType = SessionUtils.getUserType(request);
        String battlefieldName = "";

        switch (userType) {
            case UBOAT:
                UBoat uBoat = userManager.getUBoatByUserName(userName);

                battlefieldName = uBoat.getBattlefieldName();
                break;
            case AGENT:
                Agent agent = userManager.getAgentByUserName(userName);

                battlefieldName = agent.getBattlefieldName();
                break;
            case "allies":
                Allies ally = userManager.getAllyByUserName(userName);

                battlefieldName = ally.getBattlefieldName();
                break;
        }

        BattlefieldManager battleFieldManager = ServletUtils.getBattlefieldManager(getServletContext());
        Battlefield battleField = battleFieldManager.getBattlefieldByBattlefieldName(battlefieldName);
        GameStatus gameStatus = battleField.getGameStatus();
        Gson gson = new Gson();

        response.getWriter().println(gson.toJson(gameStatus));
        response.setStatus(HttpServletResponse.SC_OK);
    }
}
