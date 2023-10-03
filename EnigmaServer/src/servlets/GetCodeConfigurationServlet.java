package servlets;

import com.google.gson.Gson;
import engine.Engine;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import dto.MachineSetting;
import users.UBoat;
import users.UserManager;
import utils.ServletUtils;

import java.io.IOException;

import static constants.Constants.USERNAME;

@WebServlet(name = "GetCodeConfigurationServlet", value = "/get-code-configuration")
public class GetCodeConfigurationServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        UserManager userManager = ServletUtils.getUserManager(getServletContext());
        String userName = (String) request.getSession().getAttribute(USERNAME);
        UBoat uBoat = userManager.getUBoatByUserName(userName);
        Engine engine = uBoat.getEngine();
        MachineSetting machineSetting = engine.getCurrentMachineSetting();
        Gson gson = new Gson();

        response.getWriter().println(gson.toJson(machineSetting));
        response.setStatus(HttpServletResponse.SC_OK);
    }
}
