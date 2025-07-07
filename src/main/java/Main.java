import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


public class Main {
    public static void main(String[] args) {
        try {
            int i = 0;
            ServerSocket serverSocket = new ServerSocket(5000);
            while (i <= 3) {
                System.out.println("accepting the connection from client");
                Socket socket = serverSocket.accept();
                new Thread(() -> handleClient(socket)).start();
                i = i + 1;
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    static void handleClient(Socket socket) {
        try {
            InputStream inputStream = socket.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            OutputStream out = socket.getOutputStream();

            String line;
            boolean firstLine = true;
            int contentLength = 0;
            String fileName="";
            // Read headers

            String response = "HTTP/1.1 400 not found\r\nContent-Length: 2\r\n\r\nNOTFOUND";
            while ((line = reader.readLine()) != null && !line.isEmpty()) {

                if(firstLine)
                {
//                    System.out.println("this is the split part          "+line.split(" ")[1].split("/")[2]);
                    fileName= line.split(" ")[1].split("/")[2];
                    firstLine=false;
                }
                System.out.println("this is the header and body:  "+line);
                // Look for Content-Length header
                if (line.toLowerCase().startsWith("content-length:")) {
                    contentLength = Integer.parseInt(line.split(":")[1].trim());
                }
            }

            // Now read the body (if there is one)
            String requestBody="";
            if (contentLength > 0) {
                char[] body = new char[contentLength];
                int read = reader.read(body, 0, contentLength);
                requestBody = new String(body, 0, read);
                System.out.println("this is the request body: "+requestBody);
                Path path = Paths.get("src/main/tmp/"+fileName+".txt");
                try{
                    Files.createDirectories(path.getParent());
                    Files.createFile(path);
                    System.out.println("File created at : "+path.toAbsolutePath());
                    File file = new File(String.valueOf(path));
                    file.getParentFile().mkdirs();
                    FileOutputStream fos= new FileOutputStream(file);
                    fos.write(requestBody.getBytes());
                    response = "HTTP/1.1 200 OK\r\nContent-Length: 2\r\n\r\nOK";
                    fos.close();
                }catch (IOException ex)
                {
                    System.out.println("error is creating file: "+ex.getMessage());
                }

            } else {
                System.out.println("No request body.");
            }

            // Send a dummy 200 OK response
            out.write(response.getBytes());
            out.flush();
            socket.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }



}


