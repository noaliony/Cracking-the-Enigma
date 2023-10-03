package servlets;

import battle.field.Battlefield;
import battle.field.BattlefieldManager;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import users.UBoat;
import users.UserManager;
import utils.ServletUtils;

import java.io.IOException;

import static constants.Constants.USERNAME;

@WebServlet(name = "UpdateUBoatIsReadyServlet", value = "/update-uboat-is-ready")
public class UpdateUBoatIsReadyServlet extends HttpServlet {
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        BattlefieldManager battleFieldManager = ServletUtils.getBattlefieldManager(getServletContext());
        UserManager userManager = ServletUtils.getUserManager(getServletContext());
        String userName = (String) request.getSession().getAttribute(USERNAME);
        UBoat uBoat = userManager.getUBoatByUserName(userName);
        Battlefield battleField = battleFieldManager.getBattlefieldByBattlefieldName(uBoat.getBattlefieldName());

        uBoat.setIsReady(true);
        battleField.handleCaseIfAllUsersAreReady();
        response.setStatus(HttpServletResponse.SC_OK);
    }
}
