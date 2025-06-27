import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;


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
          String line = null;
          System.out.println("this is the request line : "+line);
          String httpResponse = "";
          boolean firstLine=true;
          httpResponse = "HTTP/1.1 404 Not Found\r\n\r\n";
          while ((line=buff.readLine())!=null  && !line.isEmpty()){
              if (firstLine) {
                  System.out.println("Request Line: " + line); // e.g., GET / HTTP/1.1
                  firstLine = false;
              } else {
                  System.out.println("Header: " + line); // All header lines
                  if(line.contains("User-Agent"))
                  {
                      httpResponse = "HTTP/1.1 200 OK\r\n\r\n"+line;
                  }
              }
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
