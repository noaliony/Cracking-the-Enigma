package main.allies.utils.allies;

import com.google.gson.Gson;

public class Constants {

    // fxml locations
    public final static String MAIN_ALLIES_FXML_RESOURCE_LOCATION = "/main/allies/MainAllies.fxml";

    // Server resources locations
    public final static String BASE_DOMAIN = "localhost";
    private final static String BASE_URL = "http://" + BASE_DOMAIN + ":8080";
    private final static String CONTEXT_PATH = "/EnigmaServer";
    public final static String FULL_SERVER_PATH = BASE_URL + CONTEXT_PATH;

    public final static String LOGIN_PAGE = FULL_SERVER_PATH + "/login";
    public static final String GET_TASKS_COUNT_PAGE = FULL_SERVER_PATH + "/get-tasks-count";
    public static final String GET_CREATED_TASKS_COUNT_PAGE = FULL_SERVER_PATH + "/get-created-tasks-count";
    public static final String GET_COMPLETED_TASKS_COUNT_PAGE = FULL_SERVER_PATH + "/get-completed-tasks-count";
    public static final String GET_TEAMS_AGENT_DATA_PAGE = FULL_SERVER_PATH + "/get-teams-agent-data";
    public static final String GET_ACTIVE_TEAMS_DETAILS_PAGE = FULL_SERVER_PATH + "/get-active-teams-details";
    public static final String GET_CONTEST_DATA_PAGE = FULL_SERVER_PATH + "/get-contest-data";
    public static final String GET_CONTESTS_DATA_LIST_PAGE = FULL_SERVER_PATH + "/get-contests-data-list";
    public static final String GET_CONTEST_STATUS_PAGE = FULL_SERVER_PATH + "/get-contest-status";
    public static final String GET_WINNER_STRING_PAGE = FULL_SERVER_PATH + "/get-winner-string";
    public static final String GET_STRING_DECRYPTED_CANDIDATE_PAGE = FULL_SERVER_PATH + "/get-string-decrypted-candidate";
    public static final String JOIN_CONTEST_PAGE = FULL_SERVER_PATH + "/join-contest";
    public static final String UPDATE_TASK_SIZE_PAGE = FULL_SERVER_PATH + "/update-task-size";
    public static final String UPDATE_ALLY_IS_READY_PAGE = FULL_SERVER_PATH + "/update-ally-is-ready";
    public static final String GET_DETAILS_OF_ACTIVE_AGENT_AND_WORK_PROVIDER_PAGE = FULL_SERVER_PATH + "/get-details-of-active-agents-and-work-provider";
    public static final String LOGOUT = FULL_SERVER_PATH + "/logout";
    public final static String RESET_CONTEST = FULL_SERVER_PATH + "/reset-contest";
    public final static String GET_MESSAGE_TO_DECODE = FULL_SERVER_PATH + "/get-message-to-decode";

    // GSON instance
    public final static Gson GSON_INSTANCE = new Gson();

    public final static int STATE_OK = 200;
    public final static int REFRESH_TWO_SECONDS = 2000;
    public final static int REFRESH_HALF_SECOND = 500;
    public final static int SLEEP_FIVE_SECONDS = 5000;
    public final static int ZERO = 0;
}
