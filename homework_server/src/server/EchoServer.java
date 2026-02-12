package server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class EchoServer {
    private final int port;

    private EchoServer(int port) {
        this.port = port;
    }

    public static EchoServer bindToPort(int port) {
        return new EchoServer(port);
    }

    public void run(){
        try(ServerSocket server = new ServerSocket(port)) {
            try(Socket clientSocket = server.accept()) {
                handle(clientSocket);
            }
        } catch (IOException e) {
            System.out.printf("Вероятнее всего порт %s занят. %n", port);
            e.printStackTrace();
        }
    }

    private void handle(Socket socket) throws IOException {
        InputStream is = socket.getInputStream();
        InputStreamReader isr = new InputStreamReader(is, "UTF-8");
        Scanner sc = new Scanner(isr);

        OutputStream os = socket.getOutputStream();
        PrintWriter writer = new PrintWriter(os);

        try (sc; writer) {
            while (true) {
                String message = sc.nextLine().strip();
                System.out.printf("Client: %s%n", message);

                if ("bye".equalsIgnoreCase(message)) {
                    System.out.println("Bye bye!");
                    return;
                }

                String reply = new StringBuilder(message).reverse().toString();
                writer.write(reply);
                writer.write(System.lineSeparator());
                writer.flush();


            }
        } catch (NoSuchElementException e) {
            System.out.println("Client dropped connection!");
        }
    }
}
