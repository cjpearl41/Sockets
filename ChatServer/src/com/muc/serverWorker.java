package com.muc;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Date;

public class serverWorker extends Thread {

    private final Socket clientSocket;

    public serverWorker(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }
    public void run(){
        try {
            handleClientSocket();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    private void handleClientSocket() throws IOException, InterruptedException {
        OutputStream outputStream = clientSocket.getOutputStream();
        for(int i=0; i<10; i++){
            outputStream.write(("The time is now " + new Date()+"\n").getBytes());
            Thread.sleep(1000);
        }
        outputStream.write("Hello World\nBy Caleb P April 2019\n".getBytes());
        clientSocket.close();
    }
}
