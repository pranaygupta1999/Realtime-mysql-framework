package com.pranay.realtimedatabase;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.regex.Pattern;

/**
 * ServerConnect
 * Create a connection to the Realtime mysql server and listens to the changes made
 * 
 */
public class ServerConnect {
    private int port;
    private Socket socket;
    private DatabaseEventListener databaseEventListener;
    private DataInputStream dataInputStream;
    private Thread changeService;
    private boolean started = false;
    /**
     * Creates a new connecion with the port 5000 and default host i.e. 127.0.0.1
     * @throws UnknownHostException if the IP address of the host could not be determined.
     * @throws IOException if an I/O error occurs when creating the socket.
     */
    public ServerConnect() throws UnknownHostException, IOException {
        socket = new Socket("127.0.0.1",5000);
        dataInputStream = new DataInputStream(socket.getInputStream());
        createNewThread();
    }
    /**
     * Creates a new connecion with the port and default host i.e. 127.0.0.1
     * @param port The port where the realtime server is running
     * @throws UnknownHostException if the IP address of the host could not be determined.
     * @throws IOException if an I/O error occurs when creating the socket.
     */
    public ServerConnect(final int port) throws UnknownHostException, IOException {
        this.port = port;
        socket = new Socket("127.0.0.1", this.port);
        dataInputStream = new DataInputStream(socket.getInputStream());
        createNewThread();
    }
    
    /**
     * Create a new connection to the realtime server with the socket provided
     * @param socket The socket object with host and port specified for realtime
     *               mysql server
     * @throws IOException if an I/O error occurs when creating the socket.
     */
    public ServerConnect(final Socket socket) throws IOException {
        this.socket = socket;
        dataInputStream = new DataInputStream(this.socket.getInputStream());
        createNewThread();
    }
    /**
     * 
     * @param databaseEventListener set the event change listsener
     */
    public void setDatabaseChangeEventListener(DatabaseEventListener databaseEventListener){
       this.databaseEventListener = databaseEventListener;
    }
    
    private void createNewThread(){
        this.changeService= new Thread(){
            @Override
            public void run(){
                while(true){
                    try {
                        String line = dataInputStream.readUTF();
                        if (Pattern.matches(".+(?)Query\tselect.+", line)) {
                        databaseEventListener.onSelect(line);
                        }
                        if (Pattern.matches(".+(?)Query\tupdate.+", line)) {
                            databaseEventListener.onUpdate(line);
                        }
                        if (Pattern.matches(".+(?)Query\talter.+", line)) {
                            databaseEventListener.onAlter(line);
                        }
                        if (Pattern.matches(".+(?)Query\tdelete.+", line)) {
                            databaseEventListener.onSelect(line);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    
                }
            }
        };
    }

    public void startDatabaseChangeListenerService(){
        if(!started){
            changeService.start();
            started = true;
        }
        else{
            System.out.println("Service already running");
        }
    }

    public Socket getSocket(){
        return socket;
    }
    public Thread getServiceThread(){
        return changeService;
    }
}
