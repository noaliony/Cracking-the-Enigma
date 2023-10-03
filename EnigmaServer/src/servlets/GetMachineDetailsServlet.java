package servlets;

import com.google.gson.Gson;
import engine.Engine;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import machine.details.MachineDetailsObject;
import users.UBoat;
import users.UserManager;
import utils.ServletUtils;

import java.io.IOException;

import static constants.Constants.USERNAME;

@WebServlet(name = "GetMachineDetailsServlet", value = "/get-machine-details")
public class GetMachineDetailsServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        UserManager userManager = ServletUtils.getUserManager(getServletContext());
        String userName = (String) request.getSession().getAttribute(USERNAME);
        UBoat uBoat = userManager.getUBoatByUserName(userName);
        Engine engine = uBoat.getEngine();
        MachineDetailsObject machineDetailsObject = engine.getMachineDetails();
        Gson gson = new Gson();

        response.getWriter().println(gson.toJson(machineDetailsObject));
        response.setStatus(HttpServletResponse.SC_OK);
    }
}
