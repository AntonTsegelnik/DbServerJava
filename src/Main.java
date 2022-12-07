import Database.DatabaseHandler;
import com.rw.Model.*;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

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
                    router(clientRequest, out);


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
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static ServerResponse router(ClientRequest clientRequest, ObjectOutputStream out) throws IOException, SQLException, ClassNotFoundException {
        if (clientRequest.requestType.equals("registration")) {
            RegistrationRequest registrationRequest = (RegistrationRequest) clientRequest;
            signUpNewUser(registrationRequest);
            ServerResponse serverResponse = new ServerResponse();


            serverResponse.body = "200 OK";
            out.writeObject(serverResponse);

        }
        if (clientRequest.requestType.equals("authorization")) {
            AuthorizationRequest authorizationRequest = (AuthorizationRequest) clientRequest;
            String res = loginUser(authorizationRequest.username, authorizationRequest.password);


            out.writeObject(res);

        }
        if (clientRequest.requestType.equals("findTicket")) {
            FlightsRequest flightsRequest = (FlightsRequest) clientRequest;
            ArrayList<ServerFlightsResponse> res = findTickets(flightsRequest);

            out.writeObject(res);
        }
        if (clientRequest.requestType.equals("getPrice")) {
            Price price = (Price) clientRequest;
            var res = getPrice(price);

            out.writeObject(res);
        }
        if (clientRequest.requestType.equals("setPassenger")) {
            var passenger = (Passenger) clientRequest;
            var pasIdGenerated = setPassenger(passenger);

                out.writeObject(pasIdGenerated);

        }
        if (clientRequest.requestType.equals("bookTicket")){
            var ticket = (Ticket)clientRequest;
            var dbHandler = new DatabaseHandler();
            var bookingTicket = dbHandler.setTicketInDbBooking(ticket);
            out.writeObject(bookingTicket);
        }

        return null;
    }

    private static ArrayList<Price> getPrice(Price price) {
        DatabaseHandler dbHandler = new DatabaseHandler();
        var result = dbHandler.getPriceFromDb(price);
        return result;
    }

    private static String loginUser(String loginText, String loginPassword) {
        String username = "";
        DatabaseHandler dbHandler = new DatabaseHandler();
        User user = new User();
        user.setUsername(loginText);
        user.setPassword(loginPassword);
        username = dbHandler.getUser(user);

        return username;
    }


    private static void signUpNewUser(RegistrationRequest registrationRequest) {
        int role = 1;
        DatabaseHandler dbHandler = new DatabaseHandler();
        String username = registrationRequest.username;
        String password = registrationRequest.password;
        if (username.equals(password) && password.equals("admin")) {
            role = 2;
        }
        System.out.println("Sign Up");
        User user = new User(username, password, role);
        dbHandler.signUpUser(user);
    }

    private static ArrayList<ServerFlightsResponse> findTickets(FlightsRequest flightsRequest) {

        DatabaseHandler dbHandler = new DatabaseHandler();
        var flights = dbHandler.getFlight(flightsRequest);
        return flights;
        // dbHandler.getFlight();
    }

    private static int setPassenger(Passenger passenger) {
        var dbHandler = new DatabaseHandler();
        return dbHandler.setPassengerInDb(passenger);

    }

}