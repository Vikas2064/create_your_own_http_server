import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;


public class Main {
  public static void main(String[] args) {
      try{
          ServerSocket serverSocket= new ServerSocket(5000);


          System.out.println("accepting the connection from client");
          Socket socket = serverSocket.accept();

          System.out.println("parsing the http request from the server: ");

          InputStream in = socket.getInputStream();
          InputStreamReader inr= new InputStreamReader(in);
          BufferedReader buff = new BufferedReader(inr);
          String line = buff.readLine();
          System.out.println("this is the request line : "+line);
          String httpResponse = "";
          if(line!=null && line.split(" ")[1].equals("/"))
          {
              httpResponse = "HTTP/1.1 200 OK\r\n\r\n";
          }
          else
          {
              httpResponse = "HTTP/1.1 404 Not Found\r\n\r\n";
          }


          System.out.println("sending the response to the client");
          OutputStream out = socket.getOutputStream();

          out.write(httpResponse.getBytes("UTF-8"));

          socket.close();
          serverSocket.close();  // optional, to close server

      } catch (IOException e) {
          throw new RuntimeException(e);
      }
  }
}
