package servlets;

import com.google.gson.Gson;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import users.UserManager;
import utils.ServletUtils;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "GetAllAlliesNamesServlet", value = "/get-all-allies-names")
public class GetAllAlliesNamesServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        UserManager userManager = ServletUtils.getUserManager(getServletContext());
        List<String> allAlliesNamesList = userManager.getAllAlliesNamesList();
        Gson gson = new Gson();

        response.getWriter().println(gson.toJson(allAlliesNamesList));
        response.setStatus(HttpServletResponse.SC_OK);
    }
}
