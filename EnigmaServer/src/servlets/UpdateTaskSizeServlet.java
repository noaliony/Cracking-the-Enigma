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

@WebServlet(name = "UpdateTaskSizeServlet", value = "/update-task-size")
public class UpdateTaskSizeServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        UserManager userManager = ServletUtils.getUserManager(getServletContext());
        BattlefieldManager battleFieldManager = ServletUtils.getBattlefieldManager(getServletContext());
        String allyName = (String) request.getSession().getAttribute(USERNAME);
        Allies ally = userManager.getAllyByUserName(allyName);
        String battleName = ally.getBattlefieldName();
        Battlefield battlefield = battleFieldManager.getBattlefieldByBattlefieldName(battleName);
        int taskSize = Integer.parseInt(request.getReader().readLine());

        battlefield.setTaskSize(taskSize);
        ally.setTaskSize(taskSize);
        response.setStatus(HttpServletResponse.SC_OK);
    }
}
