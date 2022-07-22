package BlockingClientServer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class ClientServer {
    public static String deleteSpaces(String string){
        return string.trim();
    }

}
class Client {
    public static void main(String[] args) {
        System.out.println("Ведите слово с пробелами ");
        while (true) {
            try (
                    Socket socket = new Socket("localhost", 23445);
                    BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                    Scanner scanner = new Scanner(System.in);
                )
            {
                String msg = scanner.nextLine();
                out.println(msg);
                System.out.println(in.readLine());
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
                    String result = ClientServer.deleteSpaces(task);
                    out.println("Hey Client, your message without spaces " + result);
                    if (task.equals("end")) break;
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
