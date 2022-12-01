import com.rw.Model.ServerResponse;
import com.rw.Model.ClientRequest;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Main {
    public static void main(String[] args) {
        System.out.println("Server is starting...");
        while (true) {
            try (ServerSocket server = new ServerSocket(3345);) {
// становимся в ожидание подключения к сокету под именем - "client" на серверной стороне
                Socket client = server.accept();

// после хэндшейкинга сервер ассоциирует подключающегося клиента с этим сокетом-соединением
                System.out.print("Connection accepted.");

// инициируем каналы общения в сокете, для сервера

                // канал чтения из сокета
                ObjectInputStream in = new ObjectInputStream(client.getInputStream());

                // канал записи в сокет
                ObjectOutputStream out = new ObjectOutputStream(client.getOutputStream());
                ClientRequest clientRequest = null;

// начинаем диалог с подключенным клиентом в цикле, пока сокет не закрыт
                if (!client.isClosed()) {


// сервер ждёт в канале чтения (inputstream) получения данных клиента
                    clientRequest = (ClientRequest) in.readObject();
                    if (clientRequest.requestType == "registration") {
                        ServerResponse serverResponse = new ServerResponse();
                        serverResponse.body = "Registration";
                        out.writeObject(serverResponse);
                    } else {
                        ServerResponse serverResponse = new ServerResponse();
                        serverResponse.body = clientRequest.password + clientRequest.username;
                        out.writeObject(serverResponse);
                    }
// после получения данных считывает их


// и выводит в консоль


// если условие окончания работы не верно - продолжаем работу - отправляем эхо обратно клиенту

                    System.out.println("Server Wrote message to client.");

// освобождаем буфер сетевых сообщений
                    out.flush();

                }

// если условие выхода - верно выключаем соединения
                System.out.println("Client disconnected");
                System.out.println("Closing connections & channels.");

                // закрываем сначала каналы сокета !
                in.close();
                out.close();

                // потом закрываем сокет общения на стороне сервера!
                client.close();

                // потом закрываем сокет сервера который создаёт сокеты общения
                // хотя при многопоточном применении его закрывать не нужно
                // для возможности поставить этот серверный сокет обратно в ожидание нового подключения
                // server.close();
                System.out.println("Closing connections & channels - DONE.");
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
    }
}