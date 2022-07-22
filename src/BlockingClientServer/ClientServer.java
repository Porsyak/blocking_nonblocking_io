package BlockingClientServer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class ClientServer {
    public static String deleteSpaces(String string) {
        return string.trim();
    }

}

class Client {
    public static void main(String[] args) {
        while (true) {
            try (Socket socket = new Socket("localhost", 23445)) {
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                Scanner scanner = new Scanner(System.in);
                System.out.println("Ведите слово с пробелами ");
                String msg = scanner.nextLine();
                out.println(msg);
                System.out.println("Сообщение от сервера: " + in.readLine());
                if (msg.equals("end")) break;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

    }
}

class Server {
    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(23445);) { // стартуем один раз

            while (true) { // в цикле (!) принимаем подключение
                try (
                        Socket socket = serverSocket.accept();
                        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                        PrintWriter out = new PrintWriter(socket.getOutputStream());
                ) {
                    String task = in.readLine();
                    if (task.equals("end")) break;
                    System.out.println("Принято сообщение от клиента: " + task);
                    String result = ClientServer.deleteSpaces(task);
                    out.println(result);

                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
