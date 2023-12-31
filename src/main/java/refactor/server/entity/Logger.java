package refactor.server.entity;

import java.util.ArrayList;
import java.util.List;

public class Logger {
    private static ArrayList<String> msgQueue = new ArrayList<>(128);
    public static void log(String msg) {
        // add new message to msgQueue
        msgQueue.add(msg);
    }
    //retrieve latest n messages from deprecated.logger
    public static List<String> getLog(int n) {
        n = Math.min(n, msgQueue.size());
        List<String> response = new ArrayList<>(n);
        for(int i = msgQueue.size() - n; i < msgQueue.size(); i++) {
            response.add(msgQueue.get(i));
        }
        return response;
    }
}