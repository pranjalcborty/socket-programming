package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import static helper.Helper.*;

public class ServerManager {
    public static void main(String[] args) throws IOException {
        System.out.println("Listening...");
        ServerSocket serverSocket = new ServerSocket(PORT);

        while (true) {
            Socket socket = serverSocket.accept();
            new ServerFileManager(socket).start();
        }
    }
}
