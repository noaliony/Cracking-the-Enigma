package servlets;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import users.Agent;
import users.Allies;
import users.UserManager;
import utils.ServletUtils;
import utils.SessionUtils;

import java.io.IOException;

import static constants.Constants.*;

@WebServlet(name = "LoginAgentServlet", value = "/login-agent")
public class LoginAgentServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            String usernameFromSession = SessionUtils.getUsername(request);
            UserManager userManager = ServletUtils.getUserManager(getServletContext());

            response.setContentType("text/plain;charset=UTF-8");

            if (usernameFromSession == null) { //user is not logged in yet
                String usernameFromParameter = request.getParameter(USERNAME);
                String allyNameFromParameter = request.getParameter(ALLY_NAME);
                int threadsCountFromParameter = Integer.parseInt(request.getParameter(THREADS_COUNT));
                int tasksCountToPullFromParameter = Integer.parseInt(request.getParameter(TASKS_COUNT_TO_PULL)); //maybe insert check validity to this variable

                if (usernameFromParameter == null || usernameFromParameter.isEmpty()) {
                    response.setStatus(HttpServletResponse.SC_CONFLICT);
                } else {
                    usernameFromParameter = usernameFromParameter.trim();
                    allyNameFromParameter = allyNameFromParameter.trim();

                    synchronized (this) {
                        if (userManager.isUserExists(usernameFromParameter)) {
                            handleUsernameAlreadyExistError(usernameFromParameter, response);
                        } else if (allyNameFromParameter == null) {
                            handleAllyNameIsNotValidError(response);
                        } else if (threadsCountFromParameter < MIN_THREADS_COUNT || threadsCountFromParameter > MAX_THREADS_COUNT) {
                            handleThreadsCountIsNotValidError(threadsCountFromParameter, response);
                        } else {
                            String userTypeFromParameter = request.getParameter(USER_TYPE);

                            userManager.addUser(usernameFromParameter, allyNameFromParameter, threadsCountFromParameter,
                                    tasksCountToPullFromParameter, userTypeFromParameter);

                            Agent agent = userManager.getAgentByUserName(usernameFromParameter);
                            Allies ally = userManager.getAllyByUserName(agent.getAllyName());

                            if (ally.isReady()) {
                                ally.addStandbyAgent(agent);
                            } else {
                                ally.addAgentToList(agent);
                                if (ally.getBattlefieldName() != null) {
                                    agent.setBattlefieldName(ally.getBattlefieldName());
                                }
                            }

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
        } catch (Exception exception) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().println(exception.getMessage());
        }
    }

    private void handleAllyNameIsNotValidError(HttpServletResponse response) throws IOException {
        String errorMessage = "No selected ally." + System.lineSeparator() + "Please choose one.";

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.getOutputStream().print(errorMessage);
    }

    private void handleThreadsCountIsNotValidError(int threadsCountFromParameter, HttpServletResponse response) throws IOException {
        String errorMessage = "Threads count " + threadsCountFromParameter + " is not valid." + System.lineSeparator() + "Please enter a number between 1 and 4 (including).";

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.getOutputStream().print(errorMessage);
    }

    private void handleUsernameAlreadyExistError(String usernameFromParameter, HttpServletResponse response) throws IOException {
        String errorMessage = "Username " + usernameFromParameter + " already exists." + System.lineSeparator() + "Please enter a different username.";

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.getOutputStream().print(errorMessage);
    }
}
