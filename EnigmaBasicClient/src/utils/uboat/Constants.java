package utils.uboat;

import com.google.gson.Gson;

public class Constants {

    // fxml locations
    public final static String MAIN_UBOAT_FXML_RESOURCE_LOCATION = "/main/uboat/MainUBoat.fxml";

    // Server resources locations
    public final static String BASE_DOMAIN = "localhost";
    private final static String BASE_URL = "http://" + BASE_DOMAIN + ":8080";
    private final static String CONTEXT_PATH = "/EnigmaServer";
    public final static String FULL_SERVER_PATH = BASE_URL + CONTEXT_PATH;

    public final static String LOGIN_PAGE = FULL_SERVER_PATH + "/login";
   // public final static String USERS_LIST = FULL_SERVER_PATH + "/userslist";
   // public final static String LOGOUT = FULL_SERVER_PATH + "/chat/logout";

    // GSON instance
    public final static Gson GSON_INSTANCE = new Gson();

    public final static int STATE_OK = 200;
}
