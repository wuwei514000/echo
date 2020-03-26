package com.kaikeba.content.impl;

import com.kaikeba.content.HttpSession;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class DefaultHttpSession implements HttpSession {
    private String sessionId;
    private Map<String,Object> attributes = new HashMap<String,Object>();
    public DefaultHttpSession(){
        this.sessionId = UUID.randomUUID().toString();//随机生成一个sessionId
    }
    public Object getAttribute(String name) {

        return this.attributes.get(name);
    }

    public void setAttribute(String name, Object value) {
        this.attributes.put(name,value);
    }

    public void removeAttribute(String name) {
        this.attributes.remove(name);
    }

    public String getId() {
        return this.sessionId;
    }

    public void invalidate() {
        this.sessionId = null;
    }
}
