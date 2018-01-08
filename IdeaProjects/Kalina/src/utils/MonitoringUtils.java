package utils;

import java.util.Date;

public class MonitoringUtils {

    public static void logger(KindLogger kind, String message) {
        logger(kind, message, null);
    }

    public static void logger(KindLogger kind, String message, Exception exception) {
        System.out.println("[" + kind.name() + "] " + message);

        if (exception != null) {
            exception.printStackTrace();
        }
    }
}
