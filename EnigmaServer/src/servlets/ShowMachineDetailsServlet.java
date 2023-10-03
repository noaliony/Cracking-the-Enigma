package servlets;

import display.machine.specification.DisplayMachineSpecification;
import engine.Engine;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import users.UBoat;
import users.UserManager;
import utils.ServletUtils;

import java.io.IOException;

import static constants.Constants.USERNAME;

@WebServlet(name = "ShowMachineDetailsServlet", value = "/show-machine-details")
public class ShowMachineDetailsServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        StringBuilder machineSpecification = buildMachineSpecificationString(request);

        response.getWriter().println(machineSpecification);
        response.setStatus(HttpServletResponse.SC_OK);
    }

    private StringBuilder buildMachineSpecificationString(HttpServletRequest request) {
        UserManager userManager = ServletUtils.getUserManager(getServletContext());
        String userName = (String) request.getSession().getAttribute(USERNAME);
        UBoat uBoat = userManager.getUBoatByUserName(userName);
        Engine engine = uBoat.getEngine();
        DisplayMachineSpecification displayMachineSpecification = new DisplayMachineSpecification(engine);
        StringBuilder machineSpecification = new StringBuilder();

        machineSpecification.append(displayMachineSpecification.buildMachineSpecificationString());

        return machineSpecification;
    }
}
