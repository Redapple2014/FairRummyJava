package com.fcesur.cs.cookie;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class CookieVsUserID {
    private static final Map<String, Long> cookieVsUserID = new ConcurrentHashMap<>();

    public static boolean put(String cookieId, long userId) {
        return cookieVsUserID.put(cookieId, userId) == null ? true : false;
    }

    public static Long getUserId(String cookieId) {
        System.out.println("UplayerSessionserId cookieId " + cookieId);
        return cookieVsUserID.getOrDefault(cookieId, -1l);
    }

    public static Long removeChannelInfo(String cookieId) {
        return cookieVsUserID.remove(cookieId);
    }
}
