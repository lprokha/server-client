package client;

import java.io.*;
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

            InputStream is = socket.getInputStream();
            InputStreamReader isr = new InputStreamReader(is, "UTF-8");
            Scanner scReader = new Scanner(isr);

            try(sc; writer; scReader) {
                while (true) {
                     String message = sc.nextLine().strip();
                     writer.write(message);
                     writer.write(System.lineSeparator());
                     writer.flush();

                     if ("bye".equalsIgnoreCase(message)) {
                         return;
                     }

                     String reply = scReader.nextLine();
                     System.out.printf("Server: %s%n", reply);
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
