package com.muc;

/**
 * Creates the layout for online/offline
 */
public interface UserStatusListener {
    public void online(String login);
    public void offline(String login);
}
