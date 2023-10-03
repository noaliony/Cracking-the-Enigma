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

@WebServlet(name = "GetCompletedTasksCountServlet", value = "/get-completed-tasks-count")
public class GetCompletedTasksCountServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            BattlefieldManager battlefieldManager = ServletUtils.getBattlefieldManager(getServletContext());
            UserManager userManager = ServletUtils.getUserManager(getServletContext());
            String allyName = (String) request.getSession().getAttribute(USERNAME);
            Allies ally = userManager.getAllyByUserName(allyName);
            String battleName = ally.getBattlefieldName();
            Battlefield battlefield = battlefieldManager.getBattlefieldByBattlefieldName(battleName);
            Integer completedTasksCount = battlefield.getCompletedTasksCount();

            response.getWriter().println(new Gson().toJson(completedTasksCount));
            response.setStatus(HttpServletResponse.SC_OK);
        } catch (Exception exception) {
            response.getWriter().println(exception.getMessage());
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
    }
}
