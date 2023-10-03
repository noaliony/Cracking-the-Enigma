package servlets;

import battle.field.Battlefield;
import battle.field.BattlefieldManager;
import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import users.Agent;
import users.Allies;
import users.UBoat;
import users.UserManager;
import utils.ServletUtils;
import utils.SessionUtils;

import java.io.IOException;

import static constants.Constants.*;

@WebServlet(name = "GetMessageToDecodeServlet", value = "/get-message-to-decode")
public class GetMessageToDecodeServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        BattlefieldManager battleFieldManager = ServletUtils.getBattlefieldManager(getServletContext());
        UserManager userManager = ServletUtils.getUserManager(getServletContext());
        String userName = (String) request.getSession().getAttribute(USERNAME);
        String userType = SessionUtils.getUserType(request);
        String battlefieldName = "";

        switch (userType) {
            case AGENT:
                Agent agent = userManager.getAgentByUserName(userName);

                battlefieldName = agent.getBattlefieldName();
                break;
            case "allies":
                Allies ally = userManager.getAllyByUserName(userName);

                battlefieldName = ally.getBattlefieldName();
                break;
        }

        Battlefield battleField = battleFieldManager.getBattlefieldByBattlefieldName(battlefieldName);
        String messageToDecode = battleField.getMessageToDecode();

        response.getWriter().println(new Gson().toJson(messageToDecode));
        response.setStatus(HttpServletResponse.SC_OK);
    }
}
