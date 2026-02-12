package server;

import commands.*;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class EchoServer {
    private final int port;
    private final Map<String, Command> commands = new HashMap<>();
    private final Command defaultCommand = new DefaultCommand();

    private EchoServer(int port) {
        this.port = port;
         commands.put("date", new DateCommand());
         commands.put("time", new TimeCommand());
         commands.put("reverse", new ReverseCommand());
         commands.put("upper", new UpperCommand());
         commands.put("bye", new ByeCommand());
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

                String[] parts = splitCommand(message);
                String cmd = parts[0];
                String str = parts[1];

                Command handler = commands.getOrDefault(cmd, defaultCommand);
                CommandResult result = handler.execute(str, message);

                writer.write(result.getReply());
                writer.write(System.lineSeparator());
                writer.flush();

                if (result.shouldClose()) {
                    return;
                }
            }
        } catch (NoSuchElementException e) {
            System.out.println("Client dropped connection!");
        }
    }

    private String[] splitCommand(String message) {
        String trimmed = message.strip();
        int space = trimmed.indexOf(' ');

        if (space == -1) {
            return new String[]{trimmed.toLowerCase(), ""};
        }

        String cmd = trimmed.substring(0, space).toLowerCase();
        String str = trimmed.substring(space + 1).strip();

        return new String[]{cmd, str};
    }
}
