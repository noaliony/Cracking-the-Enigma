package servlets;

import engine.Engine;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import users.UBoat;
import users.UserManager;
import utils.ServletUtils;

import java.io.IOException;

import static constants.Constants.*;

@WebServlet(name = "EncodingServlet", value = "/encoding-message-input")
public class EncodingMessageInputServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        UserManager userManager = ServletUtils.getUserManager(getServletContext());
        String userName = (String) request.getSession().getAttribute(USERNAME);
        UBoat uBoat = userManager.getUBoatByUserName(userName);
        Engine engine = uBoat.getEngine();
        String messageToEncode = request.getReader().readLine();

        if (messageToEncode != null && !messageToEncode.isEmpty()) {
            synchronized (getServletContext()) {
                try {
                    response.getWriter().println(engine.encodingStringInput(messageToEncode,false));
                    response.setStatus(HttpServletResponse.SC_OK);
                } catch (Exception exception) {
                    response.getWriter().println(exception.getMessage());
                    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                }
            }
        }
    }

    private void logServerMessage(String message) {
        System.out.println(message);
    }
}
