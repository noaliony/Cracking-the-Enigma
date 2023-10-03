package servlets;

import battle.field.Battlefield;
import battle.field.BattlefieldManager;
import com.google.gson.Gson;
import dto.AlliesDetails;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import users.Allies;
import users.UBoat;
import users.UserManager;
import utils.ServletUtils;
import utils.SessionUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static constants.Constants.*;
import static constants.Constants.ALLY;

@WebServlet(name = "GetActiveTeamsDetailsServlet", value = "/get-active-teams-details")
public class GetActiveTeamsDetailsServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        UserManager userManager = ServletUtils.getUserManager(getServletContext());
        String userName = SessionUtils.getUsername(request);
        String userType = SessionUtils.getUserType(request);
        String battlefieldName = "";

        switch (userType) {
            case UBOAT:
                UBoat uBoat = userManager.getUBoatByUserName(userName);

                battlefieldName = uBoat.getBattlefieldName();
                break;
            case "allies":
                Allies ally = userManager.getAllyByUserName(userName);

                battlefieldName = ally.getBattlefieldName();
                break;
        }
        BattlefieldManager battlefieldManager = ServletUtils.getBattlefieldManager(getServletContext());
        Battlefield battlefield = battlefieldManager.getBattlefieldByBattlefieldName(battlefieldName);
        List<Allies> alliesList = battlefield.getAlliesList();
        List<AlliesDetails> alliesDetailsList = new ArrayList<>();
        Gson gson = new Gson();

        alliesList.forEach(currentAlly -> alliesDetailsList.add(currentAlly.getNewAllieDetails()));
        response.getWriter().println(gson.toJson(alliesDetailsList));
        response.setStatus(HttpServletResponse.SC_OK);
    }
}
