package servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import users.Allies;
import users.UserManager;
import utils.ServletUtils;

import java.io.IOException;

import static constants.Constants.USERNAME;

@WebServlet(name = "GetTasksCountServlet", value = "/get-tasks-count")
public class GetTasksCountServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        UserManager userManager = ServletUtils.getUserManager(getServletContext());
        String allyName = (String) request.getSession().getAttribute(USERNAME);
        Allies ally = userManager.getAllyByUserName(allyName);
        Integer tasksCount = ally.getTasksCount();

        response.getWriter().println(tasksCount.toString());
        response.setStatus(HttpServletResponse.SC_OK);
    }
}
