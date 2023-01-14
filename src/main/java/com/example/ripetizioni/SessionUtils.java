package com.example.ripetizioni;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

public class SessionUtils {
    public static Map<String, HttpSession> sessionMap = new HashMap<>();
    public static boolean isJson(String body) {
        return !(Character.isLetter(body.charAt(0)) || Character.isDigit(body.charAt(0)));
    }
}
