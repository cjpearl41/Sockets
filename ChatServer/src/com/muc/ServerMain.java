package com.muc;

/**
 * This starts the connection progress
 */
public class ServerMain {
    public static void main(String[] args){
        int port = 8280;
        Server server = new Server(port);
        server.start();

    }


}
