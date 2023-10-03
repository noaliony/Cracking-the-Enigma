package servlets;

import battle.field.Battlefield;
import battle.field.BattlefieldManager;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import users.Agent;
import users.UserManager;
import utils.ServletUtils;

import java.io.IOException;

import static constants.Constants.USERNAME;

@WebServlet(name = "GetMachineStringServlet", value = "/get-machine-string")
public class GetMachineStringServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        BattlefieldManager battleFieldManager = ServletUtils.getBattlefieldManager(getServletContext());
        UserManager userManager = ServletUtils.getUserManager(getServletContext());
        String userName = (String) request.getSession().getAttribute(USERNAME);
        Agent agent = userManager.getAgentByUserName(userName);
        String battleName = agent.getBattlefieldName();
        Battlefield battleField = battleFieldManager.getBattlefieldByBattlefieldName(battleName);
        String machineString = battleField.getMachineString();

        response.getWriter().println(machineString);
        response.setStatus(HttpServletResponse.SC_OK);
    }
}
