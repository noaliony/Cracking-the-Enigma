package servlets;

import battle.field.Battlefield;
import battle.field.BattlefieldManager;
import com.google.gson.Gson;
import dto.TaskResult;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import users.Agent;
import users.UserManager;
import utils.ServletUtils;

import java.io.IOException;

import static constants.Constants.USERNAME;

@WebServlet(name = "PostTaskResultToBattlefieldServlet", value = "/post-task-result-to-battlefield")
public class PostTaskResultToBattleFieldServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        BattlefieldManager battleFieldManager = ServletUtils.getBattlefieldManager(getServletContext());
        UserManager userManager = ServletUtils.getUserManager(getServletContext());
        String userName = (String) request.getSession().getAttribute(USERNAME);
        Agent agent = userManager.getAgentByUserName(userName);
        String battleName = agent.getBattlefieldName();
        Battlefield battleField = battleFieldManager.getBattlefieldByBattlefieldName(battleName);
        Gson gson = new Gson();
        TaskResult taskResult = gson.fromJson(request.getReader(), TaskResult.class);

        battleField.addTaskResultToList(taskResult);
        agent.advanceCompletedTasksCount();
        agent.setFoundCandidatesCount(taskResult.getStringDecryptedCandidateList().size());
        response.setStatus(HttpServletResponse.SC_OK);
    }
}
