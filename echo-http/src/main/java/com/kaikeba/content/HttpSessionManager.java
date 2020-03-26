package com.kaikeba.content;

import com.kaikeba.content.impl.DefaultHttpSession;

import java.util.HashMap;
import java.util.Map;

/**
 * 实现一个session操作的并发处理
 */
public class HttpSessionManager {
    //在一个http服务器的进程之中，只允许一个Session保存集合
    //使用ConCurrentHashMap是因为这个子类会将集合进行分片保存，每一段数据多线程同步，而不同段进行不同的异步操作
    private static final Map<String,HttpSession> SESSION_MAP = new HashMap<String, HttpSession>();

    public static String createSession(){
        HttpSession session = new DefaultHttpSession();
        String sessionId = session.getId();
        SESSION_MAP.put(sessionId,session);
        return sessionId;
    }

    /**
     * 判断当前SessionId是否已经存在于集合中
     * @param sessionId
     * @return
     */
    public static boolean isExists(String sessionId){
        if (SESSION_MAP.containsKey(sessionId)){
            HttpSession session = SESSION_MAP.get(sessionId);
            if (session.getId() == null){
                SESSION_MAP.remove(sessionId);
                return false;
            }
            return true;
        }else{
            return false;
        }
    }

    public static void invalidate(String sessionId){
        SESSION_MAP.remove(sessionId);
    }

    public static HttpSession getSession(String sessionId){
        return SESSION_MAP.get(sessionId);
    }
}
