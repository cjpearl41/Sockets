package com.muc;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Server extends Thread {
    private final int serverPort;

    public ArrayList<serverWorker> workerList = new ArrayList<>();

    public Server(int port){
        this.serverPort = port;
    }

    public List<serverWorker> getWorkerList() {
        return workerList;
    }

    public void run(){
        try{
            ServerSocket serverSocket = new ServerSocket(serverPort);
            while(true){
                System.out.println("About to accept client connection...");
                Socket clientSocket = serverSocket.accept();
                System.out.println("Accepted connection from " + clientSocket);
                serverWorker worker = new serverWorker(this, clientSocket);
                workerList.add(worker);
                worker.start();


            }

        }catch(IOException e){
            e.printStackTrace();
        }
    }
}
