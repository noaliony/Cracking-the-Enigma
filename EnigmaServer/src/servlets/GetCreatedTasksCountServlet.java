package servlets;

import battle.field.Battlefield;
import battle.field.BattlefieldManager;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import org.omg.PortableInterceptor.USER_EXCEPTION;
import users.Allies;
import users.UserManager;
import utils.ServletUtils;

import java.io.IOException;

import static constants.Constants.USERNAME;

@WebServlet(name = "GetCreatedTasksCountServlet", value = "/get-created-tasks-count")
public class GetCreatedTasksCountServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        UserManager userManager = ServletUtils.getUserManager(getServletContext());
        String allyName = (String) request.getSession().getAttribute(USERNAME);
        Allies ally = userManager.getAllyByUserName(allyName);
        Integer createdTasksCount = ally.getCreatedTasksCount();

        response.getWriter().println(createdTasksCount);
        response.setStatus(HttpServletResponse.SC_OK);
    }
}
