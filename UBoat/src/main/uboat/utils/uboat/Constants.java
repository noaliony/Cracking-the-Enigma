package main.uboat.utils.uboat;

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
    public final static String LOAD_FILE_PAGE = FULL_SERVER_PATH + "/load-xml";
    public final static String SET_RANDOM_CODE_PAGE = FULL_SERVER_PATH + "/set-code-configuration";
    public final static String SET_MANUAL_CODE_PAGE = FULL_SERVER_PATH + "/set-code-configuration";
    public static final String GET_CODE_CONFIGURATION_PAGE = FULL_SERVER_PATH + "/get-code-configuration";
    public static final String GET_MACHINE_DETAILS_PAGE = FULL_SERVER_PATH + "/get-machine-details";
    public static final String GET_DICTIONARY_PAGE = FULL_SERVER_PATH + "/get-dictionary";
    public static final String VALIDATOR_AND_ENCODING_MESSAGE_PAGE = FULL_SERVER_PATH + "/validator-and-encoding-message";
    public static final String ENCODING_MESSAGE_INPUT_PAGE = FULL_SERVER_PATH + "/encoding-message-input";
    public static final String UPDATE_UBOAT_IS_READY_PAGE = FULL_SERVER_PATH + "/update-uboat-is-ready";
    public static final String GET_ACTIVE_TEAMS_DETAILS_PAGE = FULL_SERVER_PATH + "/get-active-teams-details";
    public static final String GET_CONTEST_STATUS_PAGE = FULL_SERVER_PATH + "/get-contest-status";
    public static final String GET_STRING_DECRYPTED_CANDIDATE_PAGE = FULL_SERVER_PATH + "/get-string-decrypted-candidate";
    public static final String GET_WINNER_STRING_PAGE = FULL_SERVER_PATH + "/get-winner-string";
    public final static String LOGOUT = FULL_SERVER_PATH + "/logout";
    public final static String RESET_CONTEST = FULL_SERVER_PATH + "/reset-contest";

    // GSON instance
    public final static Gson GSON_INSTANCE = new Gson();

    public final static int STATE_OK = 200;
    public final static int REFRESH_TWO_SECONDS = 2000;
    public final static int REFRESH_HALF_SECOND = 500;
    public final static int SLEEP_FIVE_SECONDS = 5000;
    public final static int ZERO = 0;
}
