package servlets;

import battle.field.Battlefield;
import battle.field.BattlefieldManager;
import com.google.gson.Gson;
import dto.BattlefieldDetails;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import users.Agent;
import users.Allies;
import users.UserManager;
import utils.ServletUtils;

import java.io.IOException;

import static constants.Constants.*;
import static constants.Constants.AGENT;

@WebServlet(name = "GetContestDataServlet", value = "/get-contest-data")
public class GetContestDataServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        UserManager userManager = ServletUtils.getUserManager(getServletContext());
        String userType = request.getParameter(USER_TYPE);
        String userName = (String) request.getSession().getAttribute(USERNAME);
        String battleName = "";

        if (userType.equals(ALLY)) {
            Allies ally = userManager.getAllyByUserName(userName);
            battleName = ally.getBattlefieldName();

        } else if (userType.equals(AGENT)) {
            Agent agent = userManager.getAgentByUserName(userName);
            battleName = agent.getBattlefieldName();
        }
        BattlefieldManager battlefieldManager = ServletUtils.getBattlefieldManager(getServletContext());
        Battlefield battlefield = battlefieldManager.getBattlefieldByBattlefieldName(battleName);
        BattlefieldDetails battlefieldDetails = battlefield.getNewBattlefieldDetails();
        Gson gson = new Gson();

        response.getWriter().println(gson.toJson(battlefieldDetails));
        response.setStatus(HttpServletResponse.SC_OK);
    }
}
