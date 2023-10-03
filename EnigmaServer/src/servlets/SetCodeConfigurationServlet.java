package servlets;

import com.google.gson.Gson;
import engine.Engine;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import dto.ConfigurationDetails;
import users.UBoat;
import users.UserManager;
import utils.ServletUtils;

import java.io.IOException;

import static constants.Constants.USERNAME;

@WebServlet(name = "SetCodeConfigurationServlet", value = "/set-code-configuration")
public class SetCodeConfigurationServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            UserManager userManager = ServletUtils.getUserManager(getServletContext());
            String userName = (String) request.getSession().getAttribute(USERNAME);
            UBoat uBoat = userManager.getUBoatByUserName(userName);
            Engine engine = uBoat.getEngine();
            String newLine = System.lineSeparator();
            Gson gson = new Gson();
            ConfigurationDetails configurationDetails = gson.fromJson(request.getReader(), ConfigurationDetails.class);

            engine.setMachineSetting(configurationDetails);
            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().println("The manual code setting has been set successfully" + newLine);
            response.getWriter().println("The machine code configuration: " + engine.getCurrentMachineSetting() + newLine);
        } catch (Exception exception) {
            response.getWriter().println(exception.getMessage());
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        UserManager userManager = ServletUtils.getUserManager(getServletContext());
        String userName = (String) request.getSession().getAttribute(USERNAME);
        UBoat uBoat = userManager.getUBoatByUserName(userName);
        Engine engine = uBoat.getEngine();
        String newLine = System.lineSeparator();

        engine.updateAutomaticSetting();
        response.setStatus(HttpServletResponse.SC_OK);
        response.getWriter().println("The automatic code setting has been set successfully" + newLine);
        response.getWriter().println("The machine code configuration: " + engine.getCurrentMachineSetting() + newLine);
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        UserManager userManager = ServletUtils.getUserManager(getServletContext());
        String userName = (String) request.getSession().getAttribute(USERNAME);
        UBoat uBoat = userManager.getUBoatByUserName(userName);
        Engine engine = uBoat.getEngine();

        if (engine.isCodeSet()) {
            engine.resetCurrentCode();
        }
        response.setStatus(HttpServletResponse.SC_OK);
    }
}
