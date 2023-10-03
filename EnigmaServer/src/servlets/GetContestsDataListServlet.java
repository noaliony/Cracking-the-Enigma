package servlets;

import battle.field.Battlefield;
import battle.field.BattlefieldManager;
import com.google.gson.Gson;
import dto.BattlefieldDetails;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import users.UserManager;
import utils.ServletUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static constants.Constants.USERNAME;
import static constants.Constants.USER_TYPE;

@WebServlet(name = "GetContestsDataListServlet", value = "/get-contests-data-list")
public class GetContestsDataListServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        BattlefieldManager battlefieldManager = ServletUtils.getBattlefieldManager(getServletContext());
        List<Battlefield> battlefieldList = battlefieldManager.getBattlefieldList();
        List<BattlefieldDetails> battlefieldDetailsList = new ArrayList<>();
        Gson gson = new Gson();

        battlefieldList.forEach(battlefield -> battlefieldDetailsList.add(battlefield.getNewBattlefieldDetails()));
        response.getWriter().println(gson.toJson(battlefieldDetailsList));
        response.setStatus(HttpServletResponse.SC_OK);
    }

}
