package com.muc;

import java.io.*;
import java.net.Socket;

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
        InputStream inputStream = clientSocket.getInputStream();
        OutputStream outputStream = clientSocket.getOutputStream();

        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        String line;
        while((line = reader.readLine()) != null){
            if("quit".equalsIgnoreCase(line)){
                break;
            }
            String msg="You typed: " +line+"\n \r";
            outputStream.write(msg.getBytes());
        }

        clientSocket.close();
    }
}
