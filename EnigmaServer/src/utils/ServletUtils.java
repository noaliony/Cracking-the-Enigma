package utils;

import battle.field.BattlefieldManager;
import jakarta.servlet.ServletContext;
import users.UserManager;

import static constants.Constants.*;

public class ServletUtils {

    private static final Object userManagerLock = new Object();
    private static final Object battleFieldManagerLock = new Object();

    public static UserManager getUserManager(ServletContext servletContext) {

        synchronized (userManagerLock) {
            if (servletContext.getAttribute(USER_MANAGER_ATTRIBUTE_NAME) == null) {
                servletContext.setAttribute(USER_MANAGER_ATTRIBUTE_NAME, new UserManager());
            }
        }
        return (UserManager) servletContext.getAttribute(USER_MANAGER_ATTRIBUTE_NAME);
    }

    public static BattlefieldManager getBattlefieldManager(ServletContext servletContext) {

        synchronized (battleFieldManagerLock) {
            if (servletContext.getAttribute(BATTLE_FIELD_MANAGER_ATTRIBUTE_NAME) == null) {
                servletContext.setAttribute(BATTLE_FIELD_MANAGER_ATTRIBUTE_NAME, new BattlefieldManager());
            }
        }
        return (BattlefieldManager) servletContext.getAttribute(BATTLE_FIELD_MANAGER_ATTRIBUTE_NAME);
    }
}
