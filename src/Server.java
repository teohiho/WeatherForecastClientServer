import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
 
 
public class Server extends Thread {
   private ServerSocket server;
   private int port = 5678;
 
    Server() {
        try {
            server = new ServerSocket(port);
            System.out.println(">>>>");
            handleConnection();
        } catch (IOException e) {
           e.printStackTrace();
        }
    }
 
    public void handleConnection() {
    	 System.out.println(">>ssss");
       
            try {
                Socket socket = server.accept();
                System.out.println(">>ssss");
                new ConnectionHandler(socket);
            } catch (IOException e) {
                e.printStackTrace();
            }
        
    }
 
    public static void main(String a[]) {
        Server handle = new Server();
        handle.handleConnection();
    }
}