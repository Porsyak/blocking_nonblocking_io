package NonBlockingIO;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class NonBlocking {
}

class Server {
    public static void main(String[] args) {
        //занимаем порт, определяем сереверный сокет
        try (ServerSocketChannel serverSocketChannel = ServerSocketChannel.open()) {
            serverSocketChannel.bind(new InetSocketAddress(23333));
            //noinspection InfiniteLoopStatement
            while (true) {
                // ждем подключение клиента и получаем потоки для дальнейшей работы
                try (SocketChannel socketChannel = serverSocketChannel.accept()) {
                    // определяем буффер для получения данных
                    ByteBuffer byteBuffer = ByteBuffer.allocate(2 << 10);
                    while (socketChannel.isConnected()) {
                        //читаем данные из канала в буфер
                        int byteCount = socketChannel.read(byteBuffer);
                        //если из потока читать нельзя перестаём работать с этим клиентом
                        if (byteCount == -1) break;
                        // получаем переданную строку от клиента и ощищаем буффер
                        final String msg = new String(byteBuffer.array(), 0, byteCount, StandardCharsets.UTF_8);
                        byteBuffer.clear();
                        System.out.println("Полученно сообщение от клиента " + msg);
                        socketChannel.write(ByteBuffer.wrap(("Вы прислали- " + msg).getBytes()));
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}

class Client {
    public static void main(String[] args) {
        try (SocketChannel socketChannel = SocketChannel.open();
             Scanner scanner = new Scanner(System.in)) // получаем входящие и исходящие потоки информации
        {
            InetSocketAddress socketAddress = new InetSocketAddress("127.0.0.1", 23333);
            socketChannel.connect(socketAddress);
            // определяем буфер для полущения данных
            ByteBuffer inputBuffer = ByteBuffer.allocate(2 << 10);
            String msg;
            while (true) {
                System.out.println("Введите сообщение для сервера ");
                msg = scanner.nextLine();
                if (msg.equals("end")) break;
                socketChannel.write(ByteBuffer.wrap(msg.getBytes()));
                //noinspection BusyWait
                Thread.sleep(2000);
                int byteCount = socketChannel.read(inputBuffer);
                System.out.println(new String(inputBuffer.array()).trim());
                inputBuffer.clear();
            }
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}









