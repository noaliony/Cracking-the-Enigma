package servlets;

import battle.field.Battlefield;
import battle.field.BattlefieldManager;
import engine.Engine;
import exceptions.MessageToEncodeIsNotValidException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.*;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import users.UBoat;
import users.UserManager;
import utils.ServletUtils;

import java.io.IOException;

import static constants.Constants.USERNAME;

@WebServlet(name = "ValidatorAndEncodingMessageServlet", value = "/validator-and-encoding-message")
public class ValidatorAndEncodingMessageServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        synchronized (getServletContext()) {
            UserManager userManager = ServletUtils.getUserManager(getServletContext());
            String userName = (String) request.getSession().getAttribute(USERNAME);
            UBoat uBoat = userManager.getUBoatByUserName(userName);
            Engine engine = uBoat.getEngine();

            try {
                String originalMessage = request.getReader().readLine();
                String messageToEncode = engine.validatorMessageToEncode(originalMessage);
                String messageToDecode = engine.encodingStringInput(messageToEncode, false);
                String battleName = uBoat.getBattlefieldName();
                BattlefieldManager battleFieldManager = ServletUtils.getBattlefieldManager(getServletContext());
                Battlefield battleField = battleFieldManager.getBattlefieldByBattlefieldName(battleName);

                battleField.setOriginalMessage(originalMessage);
                battleField.setMessageToEncode(messageToEncode);
                battleField.setMessageToDecode(messageToDecode);
                response.getWriter().println(originalMessage + System.lineSeparator() +
                        messageToEncode + System.lineSeparator() + messageToDecode);
                response.setStatus(HttpServletResponse.SC_OK);
            } catch (MessageToEncodeIsNotValidException exception) {
                response.getWriter().println(exception.getMessage());
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            }
        }
    }
}
