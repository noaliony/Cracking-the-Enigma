package servlets;

import battle.field.Battlefield;
import battle.field.BattlefieldManager;
import com.google.gson.Gson;
import dto.StringDecryptedCandidate;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import users.Agent;
import users.Allies;
import users.UBoat;
import users.UserManager;
import utils.ServletUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static constants.Constants.*;
import static constants.Constants.AGENT;

@WebServlet(name = "GetStringDecryptedCandidateServlet", value = "/get-string-decrypted-candidate")
public class GetStringDecryptedCandidateServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        UserManager userManager = ServletUtils.getUserManager(getServletContext());
        String userName = (String) request.getSession().getAttribute(USERNAME);
        String userType = (String) request.getSession().getAttribute(USER_TYPE);

        if (userType != null) {
            String battlefieldName = null;

            switch (userType) {
                case UBOAT:
                    UBoat uBoat = userManager.getUBoatByUserName(userName);

                    battlefieldName = uBoat.getBattlefieldName();
                    break;
                case "allies":
                    Allies ally = userManager.getAllyByUserName(userName);

                    battlefieldName = ally.getBattlefieldName();
                    break;
                case AGENT:
                    Agent agent = userManager.getAgentByUserName(userName);

                    battlefieldName = agent.getBattlefieldName();
                    break;
            }

            if (battlefieldName != null) {
                synchronized (getServletContext()) {
                    BattlefieldManager battlefieldManager = ServletUtils.getBattlefieldManager(getServletContext());
                    Battlefield battlefield = battlefieldManager.getBattlefieldByBattlefieldName(battlefieldName);
                    List<StringDecryptedCandidate> stringDecryptedCandidateListFromDM = new ArrayList<>(battlefield.getStringDecryptedCandidateList());
                    Gson gson = new Gson();

                    response.getWriter().println(gson.toJson(stringDecryptedCandidateListFromDM));
                    response.setStatus(HttpServletResponse.SC_OK);
                }
            }

        } else {
            response.getWriter().println("User is not logged in yet!");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }
    }

    private List<StringDecryptedCandidate> calculateTheDeltaBetweenTwoLists(List<StringDecryptedCandidate> stringDecryptedCandidateListFromDM,
                                                                            List<StringDecryptedCandidate> stringDecryptedCandidateList) {

        return stringDecryptedCandidateListFromDM
                .stream()
                .filter(currentString -> !stringDecryptedCandidateList.contains(currentString))
                .collect(Collectors.toList());
    }
}
