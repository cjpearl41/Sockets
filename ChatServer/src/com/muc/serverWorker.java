package com.muc;

import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.net.Socket;
import java.util.HashSet;
import java.util.List;

/**
 * This class handles commands and forming messages
 */
public class serverWorker extends Thread {

    private final Socket clientSocket;
    private final Server server;
    private String login = null;
    private OutputStream outputStream;
    private HashSet<String>  topicSet = new HashSet<>();

    public serverWorker(Server server, Socket clientSocket) {
        this.server = server;
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

    /**
     *
     * This is used to take commands and use the correct methods for those functions
     * @throws IOException
     * @throws InterruptedException
     */
    private void handleClientSocket() throws IOException, InterruptedException {
        InputStream inputStream = clientSocket.getInputStream();
        this.outputStream = clientSocket.getOutputStream();

        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        String line;
        while((line = reader.readLine()) != null){
            //String[] tokens = Line.split("");
            String[] tokens = StringUtils.split(line, null, 3);
            if(tokens != null && tokens.length > 0){
                String cmd = tokens[0];
                if("logoff".equals(cmd) || "quit".equalsIgnoreCase(line)){
                    handleLogoff();
                    break;
                }else if ("login".equalsIgnoreCase(cmd)) {
                    handleLogin(outputStream, tokens);
                } else if ("msg".equalsIgnoreCase(cmd)){
                    String[] tokenMsg = StringUtils.split(line,null, 3);
                    handleMessage(tokens);
                }
                else if ("join".equalsIgnoreCase(cmd)){
                    handleJoin(tokens);
                }
                else if ("leave". equalsIgnoreCase(cmd)){
                    handleLeave(tokens);
                }
                else {
                    String msg = "unknown " + cmd + "\n";
                    outputStream.write(msg.getBytes());
                }
            }
        }

        clientSocket.close();
    }

    /**
     * This removes the person from the topic
     * @param tokens
     */
    private void handleLeave(String[] tokens) {
        if (tokens.length > 1){
            String topic = tokens[1];
            topicSet.remove(topic);
        }
    }

    /**
     * This allows for checking if a person is in the topic
     * @param topic
     * @return
     */
    public boolean isMemberOfTopic(String topic){
        return topicSet.contains(topic);
    }

    /**
     * This adds a person to a topic
     * @param tokens
     */
    private void handleJoin(String[] tokens) {
        if (tokens.length > 1){
            String topic = tokens[1];
            topicSet.add(topic);
        }
    }

    //format: "msg" "login" body...
    //format: "msg" "#topic" body...

    /**
     * This creates the message
     * @param tokens
     * @throws IOException
     */
    private void handleMessage(String[] tokens) throws IOException {
        String sendTo = tokens[1];
        String body = tokens[2];

        boolean isTopic = sendTo.charAt(0) == '#';


        List<serverWorker> workerList = server.getWorkerList();
        for(serverWorker worker: workerList){
            if(isTopic){
                if(worker.isMemberOfTopic(sendTo)){

                        String outMsg = "msg " + sendTo + ": " + login + " " + body + "\n";
                        worker.send(outMsg);
                }

            }else{
                if (sendTo.equalsIgnoreCase(worker.getLogin())){
                    String outMsg = "msg " + login + " " + body + "\n";
                    worker.send(outMsg);
                }
            }

        }
    }

    /**
     * This removes the person from the worker and being able to logoff
     * @throws IOException
     */
    private void handleLogoff() throws IOException {
        server.removeWorker(this);
        List<serverWorker> workerList = server.getWorkerList();
        String onlineMsg = "offline " + login  +"\n";
        for(serverWorker worker: workerList){
            if(!login.equals(worker.getLogin())) {
                worker.send(onlineMsg);
            }
        }
        clientSocket.close();
    }

    public String getLogin(){
        return login;
    }

    /**
     * This allows for a person to login
     * @param outputStream
     * @param tokens
     * @throws IOException
     */
    private void handleLogin(OutputStream outputStream, String[] tokens) throws IOException {
        if(tokens.length == 3){
            String login = tokens[1];
            String password = tokens[2];

            if((login.equals("guest") && password.equals("guest")) || (login.equals("jim") && password.equals("jim"))){
                String msg = "Ok login\n";
                    outputStream.write(msg.getBytes());


                this.login = login;
                System.out.println("User logged in successfully " + login);


                //Send current user all either online login
                List<serverWorker> workerList = server.getWorkerList();
                for(serverWorker worker: workerList){
                    if (worker.getLogin() != null) {
                        if(!login.equals(worker.getLogin())) {
                            String msg2 = "online " + worker.getLogin() + "\n";
                            send(msg2);
                        }
                    }
                }
                String onlineMsg = "online " + login  +"\n";
                for(serverWorker worker: workerList){
                    if(!login.equals(worker.getLogin())) {
                        worker.send(onlineMsg);
                    }
                }
            } else {
                String msg = "Error login \n";
                outputStream.write(msg.getBytes());
                System.err.println("Login failed for " + login);
            }
        }
    }

    private void send(String onlineMsg) throws IOException {
        outputStream.write(onlineMsg.getBytes());
    }
}
