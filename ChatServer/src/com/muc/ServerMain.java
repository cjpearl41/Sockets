package com.muc;

import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;

public class ServerMain {
    public static void main(String[] args){
        int port = 8818;
        try{
            ServerSocket serverSocket = new ServerSocket(port);
            while(true){
                System.out.println("About to accept client connection...");
                Socket clientSocket = serverSocket.accept();
                System.out.println("Accepted connection from " + clientSocket);
                Thread t = new Thread(){
                    public void run(){
                        try {
                            handleClientSocket(clientSocket);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                };
                t.start();

            }

        }catch(IOException e){
           e.printStackTrace();
        }
    }

    private static void handleClientSocket(Socket clientSocket) throws IOException, InterruptedException {
        OutputStream outputStream = clientSocket.getOutputStream();
        for(int i=0; i<10; i++){
            outputStream.write(("The time is now " + new Date()+"\n").getBytes());
            Thread.sleep(1000);
        }
        outputStream.write("Hello World\nBy Caleb P April 2019\n".getBytes());
        clientSocket.close();
    }
}
