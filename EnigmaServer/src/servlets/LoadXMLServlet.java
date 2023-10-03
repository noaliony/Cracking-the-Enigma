package servlets;

import battle.field.Battlefield;
import battle.field.BattlefieldManager;
import engine.Engine;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import users.UBoat;
import users.UserManager;
import utils.ServletUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

import static constants.Constants.USERNAME;


@MultipartConfig(fileSizeThreshold = 1024 * 1024, maxFileSize = 1024 * 1024 * 5, maxRequestSize = 1024 * 1024 * 5 * 5)
@WebServlet(name = "LoadXMLServlet", value = "/load-xml")
public class LoadXMLServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            Part part = request.getPart("xml-file");
            validateReceivedFile(part);
            UserManager userManager = ServletUtils.getUserManager(getServletContext());
            String userName = (String) request.getSession().getAttribute(USERNAME);
            UBoat uBoat = userManager.getUBoatByUserName(userName);
            Engine engine = uBoat.getEngine();
            InputStream XMLFileInputStream = part.getInputStream();
            BattlefieldManager battleFieldManager = ServletUtils.getBattlefieldManager(getServletContext());
            Battlefield battleField;

            engine.loadXMLFileToMachine(XMLFileInputStream, battleFieldManager.getBattlefieldNameList());
            String machineString = readFromInputStream(XMLFileInputStream);

            battleField = engine.createBattleField(machineString, uBoat);
            battleFieldManager.addBattlefield(battleField);
            response.getWriter().println("The file was loaded successfully");
            response.setStatus(HttpServletResponse.SC_OK);
        } catch (Exception exception) {
            response.getWriter().println(exception.getMessage());
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    private String readFromInputStream(InputStream inputStream) throws IOException {
        inputStream.reset();

        return new Scanner(inputStream).useDelimiter("\\Z").next();
    }

    private void validateReceivedFile(Part part) throws ServletException {
        if (part == null) {
            throw new ServletException("Did not receive a file with the \"xml-file\" parameter!");
        } else {
            String fileName = getFileName(part);

            if (fileName == null) {
                throw new ServletException("Did not receive a file name!");
            } else if (!fileName.endsWith(".xml")) {
                throw new ServletException("File received is not XML!");
            }
        }
    }


    private String getFileName(final Part part) {
        final String partHeader = part.getHeader("content-disposition");

        for (String content : part.getHeader("content-disposition").split(";")) {
            if (content.trim().startsWith("filename")) {
                return content.substring(
                        content.indexOf('=') + 1).trim().replace("\"", "");
            }
        }

        return null;
    }
}
