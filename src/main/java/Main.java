import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

import static java.lang.System.out;

public class Main {
  public static void main(String[] args) {
    // You can use print statements as follows for debugging, they'll be visible when running tests.
    out.println("Logs from your program will appear here!");

//     Uncomment this block to pass the first stage

     try {
           ServerSocket serverSocket = new ServerSocket(4221);

           // Since the tester restarts your program quite often, setting SO_REUSEADDR
           // ensures that we don't run into 'Address already in use' errors
           serverSocket.setReuseAddress(true);

           Socket clientSocket=serverSocket.accept(); // Wait for connection from client.
           out.println("New client connected");
           BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
           StringBuilder request = new StringBuilder();
           String line = "";
           while((line = in.readLine())!=null && !line.isEmpty())
           {
               request.append(line).append("\r\n");
           }
           out.println("received http request"+request);;
           OutputStream outputStream= clientSocket.getOutputStream();
           String response = "HTTP/1.1 200 OK\r\nContent-Type: text/plain\r\n\r\nHello from Java Server!";
           outputStream.write(response.getBytes());
           outputStream.flush();

     } catch (IOException e) {
       out.println("IOException: " + e.getMessage());
     }
  }
}
