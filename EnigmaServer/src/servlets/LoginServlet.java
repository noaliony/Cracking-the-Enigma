package servlets;

import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import users.UserManager;
import utils.ServletUtils;
import utils.SessionUtils;

import java.io.IOException;

import static constants.Constants.*;


@WebServlet(name = "LoginServlet", value = "/login")
public class LoginServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String usernameFromSession = SessionUtils.getUsername(request);
        UserManager userManager = ServletUtils.getUserManager(getServletContext());

        response.setContentType("text/plain;charset=UTF-8");

        if (usernameFromSession == null) { //user is not logged in yet
            String usernameFromParameter = request.getParameter(USERNAME);

            if (usernameFromParameter == null || usernameFromParameter.isEmpty()) {
                response.setStatus(HttpServletResponse.SC_CONFLICT);
            } else {
                usernameFromParameter = usernameFromParameter.trim();

                synchronized (this) {
                    if (userManager.isUserExists(usernameFromParameter)) {
                        String errorMessage = "Username " + usernameFromParameter + " already exists." + System.lineSeparator() +"Please enter a different username.";

                        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                        response.getOutputStream().print(errorMessage);
                    }
                    else {
                        String userTypeFromParameter = request.getParameter(USER_TYPE);

                        userManager.addUser(usernameFromParameter, userTypeFromParameter);

                        request.getSession(true).setAttribute(USERNAME, usernameFromParameter);
                        request.getSession(true).setAttribute(USER_TYPE, userTypeFromParameter);

                        System.out.println("On login, request URI is: " + request.getRequestURI());
                        response.setStatus(HttpServletResponse.SC_OK);
                    }
                }
            }
        } else {
            response.setStatus(HttpServletResponse.SC_OK);
        }
    }
}
