package client;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class EchoClient {
    private final int port;
    private final String host;

    private EchoClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public static EchoClient connectTo(int port) {
        String localhost = "127.0.0.1";
        return new EchoClient(localhost, port);
    }

    public void run() {
        System.out.println("Чтобы выйти, напишите 'Bye'\n\n");

        try(Socket socket = new Socket(host, port)) {
            Scanner sc = new Scanner(System.in, "UTF-8");
            OutputStream os = socket.getOutputStream();
            PrintWriter writer = new PrintWriter(os);

            try(sc; writer) {
                while (true) {
                     String message = sc.nextLine();
                     writer.write(message);
                     writer.write(System.lineSeparator());
                     writer.flush();

                     if ("bye".equalsIgnoreCase(message)) {
                         return;
                     }
                }
            }
        } catch (NoSuchElementException e) {
            System.out.println("Connection dropped!");
        } catch (IOException e) {
            System.out.printf("Can't connect to %s:%s!%n", host, port);
            e.printStackTrace();
        }
    }
}
