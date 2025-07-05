import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.FileSystemException;
import java.nio.file.Files;


public class Main {
  public static void main(String[] args) {
      try{
          int i=0;
          ServerSocket serverSocket= new ServerSocket(5000);
          while(i<=3)
          {
              System.out.println("accepting the connection from client");
              Socket socket = serverSocket.accept();
              new Thread(()-> handleClient(socket)).start();
              i=i+1;
          }

      } catch (IOException e) {
          throw new RuntimeException(e);
      }
  }

  static void handleClient(Socket socket){
      try{
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
                  String endPoints= line.split(" ")[1].split("/")[2];
                  System.out.println("this is the endpoint: "+endPoints);
                  try{
                      File file= new File("E:/Interview/create your own http server/codecrafters-http-server-java/src/main/tmp/"+endPoints+".txt");
                      String fileContent = Files.readString(file.toPath());
                      System.out.println("this is the content of the file : "+fileContent);
                      String header = "HTTP/1.1 200 OK\r\n"+
                                     "Content-Type: application/octet-stream\r\n"+
                                     "Content-Length: " + fileContent.getBytes().length+ "\r\n" +
                                     "\r\n";
                      OutputStream out = socket.getOutputStream();
                      out.write(header.getBytes());
                      out.write(fileContent.getBytes());
                      out.flush();

                  }catch (FileSystemException fx){
                      System.out.println(" no such file exception : "+ fx);
                  }
                  firstLine = false;
              }
          }


          System.out.println("sending the response to the client");
          OutputStream out = socket.getOutputStream();

          out.write(httpResponse.getBytes("UTF-8"));

          socket.close();
      }
      catch (IOException ex)
      {
          ex.printStackTrace();
      }

  }


}


