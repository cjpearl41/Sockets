package com.muc;

/**
 * Creates the layout for onMessage
 */
public interface MessageListener {
    public void onMessage(String fromLogin, String msgBody);
}
