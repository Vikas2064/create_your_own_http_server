import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.zip.GZIPOutputStream;


public class Main {
    public static void main(String[] args) {
        try {
            int i = 0;
            ServerSocket serverSocket = new ServerSocket(5000);
            while (i <= 3) {
                System.out.println("accepting the connection from client");
                Socket socket = serverSocket.accept();
                new Thread(() -> {
                    try {
                        handleClient(socket);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }).start();
                i = i + 1;
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static byte[] compress(String data) throws IOException {
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        GZIPOutputStream gzipOut = new GZIPOutputStream(byteStream);

        gzipOut.write(data.getBytes());
        gzipOut.close(); // finish compression
        return byteStream.toByteArray(); // return compressed bytes
    }

    static void handleClient(Socket socket) throws IOException {

        BufferedReader buff = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        String line = "";
        String encoding_method = "";
        while ((line = buff.readLine()) != null && !(line.isEmpty())) {
            System.out.println("this is the line : " + line);
            if (line.contains("Accept-Encoding")) {
                encoding_method = line.split(" ")[1];
            }
        }
        String body = "this is the response body";

        String response="";
        OutputStream out = socket.getOutputStream();
        if(encoding_method.equals("gzip")) {
            byte[] responseBody = compress(body);
            System.out.println("this is the respnose body: "+responseBody);
            response = "HTTP/1.1 200 OK\r\n" +
                    "Content-Encoding: gzip\r\n" +
                    "Content-Type: text/plain\r\n" +
                    "Content-Length: " + body.getBytes().length + "\r\n";
            out.write(response.getBytes());
            out.write(responseBody);
        }
        else {
            System.out.println("this is the response body: "+body);
            response = "HTTP/1.1 200 OK\r\n" +
                    "Content-Type: text/plain\r\n" +
                    "Content-Length: " + body.getBytes().length + "\r\n";
            out.write(response.getBytes());
            out.write(body.getBytes());
        }
        out.flush();
    }
}


