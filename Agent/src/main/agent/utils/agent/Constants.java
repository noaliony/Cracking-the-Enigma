package main.agent.utils.agent;

import com.google.gson.Gson;

public class Constants {

    // fxml locations
    public final static String MAIN_AGENT_FXML_RESOURCE_LOCATION = "/main/agent/MainAgent.fxml";

    // Server resources locations
    public final static String BASE_DOMAIN = "localhost";
    private final static String BASE_URL = "http://" + BASE_DOMAIN + ":8080";
    private final static String CONTEXT_PATH = "/EnigmaServer";
    public final static String FULL_SERVER_PATH = BASE_URL + CONTEXT_PATH;

    public final static String LOGIN_AGENT_PAGE = FULL_SERVER_PATH + "/login-agent";
    public final static String GET_MACHINE_STRING_PAGE = FULL_SERVER_PATH + "/get-machine-string";
    public final static String GET_MESSAGE_TO_DECODE = FULL_SERVER_PATH + "/get-message-to-decode";
    public final static String GET_ALL_ALLIES_NAMES_PAGE = FULL_SERVER_PATH + "/get-all-allies-names";
    public static final String GET_CONTEST_STATUS_PAGE = FULL_SERVER_PATH + "/get-contest-status";
    public static final String GET_TASKS_DETAILS_PAGE = FULL_SERVER_PATH + "/get-tasks-details";
    public static final String GET_TASKS_RESULTS_DETAILS_PAGE = FULL_SERVER_PATH + "/get-tasks-results-details";
    public static final String POST_TASK_RESULT_TO_BATTLEFIELD_PAGE = FULL_SERVER_PATH + "/post-task-result-to-battlefield";
    public static final String GET_CONTEST_DATA_PAGE = FULL_SERVER_PATH + "/get-contest-data";
    public static final String GET_STRING_DECRYPTED_CANDIDATE_PAGE = FULL_SERVER_PATH + "/get-string-decrypted-candidate";
    public static final String GET_WINNER_STRING_PAGE = FULL_SERVER_PATH + "/get-winner-string";
    public static final String LOGOUT = FULL_SERVER_PATH + "/logout";
    public static final String IS_RESET_DATA_REQUESTED = FULL_SERVER_PATH + "/is-reset-data-requested";
    public static final String CHECK_IF_AGENT_IS_CONNECTED_TO_CONTEST_PAGE = FULL_SERVER_PATH + "/check-if-agent-is-connected-to-contest";


    // GSON instance
    public final static Gson GSON_INSTANCE = new Gson();

    public final static int REFRESH_HALF_SECOND = 500;
    public final static int REFRESH_TWO_SECONDS = 2000;
    public final static int SLEEP_FIVE_SECONDS = 5000;
    public final static int ZERO = 0;
}
