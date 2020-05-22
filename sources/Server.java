

import java.io.*;
import java.net.*;
import java.util.*;
import com.pranay.realtimedatabase.FileWatcher;

public class Server {
    private static ArrayList<Socket> listOfSockets;

    private static void startFileWatcherService(String logFileLocation) throws FileNotFoundException {
        FileWatcher fWatcher = new FileWatcher(logFileLocation);
        System.out.println("Located log file at " + logFileLocation);
        fWatcher.setFileContentsChangedListener((line) -> {
            for (Socket socket : listOfSockets) {
                try {
                    DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream());
                    outputStream.writeUTF(line);
                } catch (IOException e) {
                    System.err.println(e.getMessage());
                }

            }
        });
        fWatcher.start();
    }
    public static void main(String args[]) {
        System.out.println("Initializing Client list");
        listOfSockets = new ArrayList<Socket>();

        try(ServerSocket server = new ServerSocket(5000);){
        // Starting FileWatcher Service
            System.out.println("Starting File Watcher Service to watch log files");
            
            if (args.length == 0)
                throw new FileNotFoundException("File location not provided as the parameter");
            startFileWatcherService(args[0]);

            System.out.println("Server started successfully");

            while(true){
                System.out.println("Waiting for connection at port " + server.getLocalPort());
                Socket socket = server.accept();
                listOfSockets.add(socket);
                ClientThread thread = new ClientThread(socket, listOfSockets);
                System.out.println("Connected to the client with port " + socket.getPort());
                thread.start();
            }
        }
        catch(FileNotFoundException e){
            System.err.println(e.getMessage());
            System.out.println("Invalid log file provided");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

class ClientThread extends Thread{
	private Socket socket;
	DataInputStream in;
	
        ArrayList<Socket> listOfSockets;
	public ClientThread(Socket socket, ArrayList<Socket> list){
            this.socket = socket;   
            this.listOfSockets = list;
	}
	public void start(){
            try{
                in = new DataInputStream(socket.getInputStream());
                	
            }
            catch(Exception e){
                System.out.println(e.toString());
            }
            super.start();
	}
	public void run() {
            while(true){
                try{
                    String line = in.readUTF();
                    System.out.println("Got from " +  socket.getPort() + ": " + line);
                    for(Socket sock : listOfSockets){
                        DataOutputStream out = new DataOutputStream( sock.getOutputStream());
                        System.out.println("Writing to " + sock.getPort());
                        out.writeUTF(line);
                    }
                }
                catch(Exception e){
                    System.out.println(e.toString());
                    break;
                }

            }
	}
}

