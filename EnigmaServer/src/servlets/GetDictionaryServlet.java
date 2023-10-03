package servlets;

import com.google.gson.Gson;
import components.Dictionary;
import engine.Engine;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import users.UBoat;
import users.UserManager;
import utils.ServletUtils;
import java.io.IOException;

import static constants.Constants.*;

@WebServlet(name = "GetDictionaryServlet", value = "/get-dictionary")
public class GetDictionaryServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        UserManager userManager = ServletUtils.getUserManager(getServletContext());
        String userName = (String) request.getSession().getAttribute(USERNAME);
        UBoat uBoat = userManager.getUBoatByUserName(userName);
        Engine engine = uBoat.getEngine();
        Dictionary dictionary = engine.getDictionaryObject();
        Gson gson = new Gson();

        response.getWriter().println(gson.toJson(dictionary));
        response.setStatus(HttpServletResponse.SC_OK);
    }
}


