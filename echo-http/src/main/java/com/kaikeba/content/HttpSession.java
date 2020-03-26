package com.kaikeba.content;

/**
 * 与JavaWeb开发接口完全一致
 */
public interface HttpSession {
    public static final String SESSIONID = "XiaoliLshiLaoshiSESSIONID";
    public Object getAttribute(String name);
    public void setAttribute(String name,Object value);
    public void removeAttribute(String name);
    public String getId();
    public void invalidate();
}
