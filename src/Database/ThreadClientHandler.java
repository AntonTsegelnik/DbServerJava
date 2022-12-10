package Database;

import com.rw.Model.*;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ThreadClientHandler implements Runnable {
    public  Socket clientDialog;
    public static Lock lock = new ReentrantLock();


    public ThreadClientHandler(Socket client) {
            clientDialog = client;
            //this.lock = lock;
    }

    @Override
    public void run() {
        try  {
            lock.lock();

// становимся в ожидание подключения к сокету под именем - "client" на серверной стороне


// после хэндшейкинга сервер ассоциирует подключающегося клиента с этим сокетом-соединением
            System.out.print("Connection accepted.");

// инициируем каналы общения в сокете, для сервера

            // канал чтения из сокета
            ObjectInputStream in = new ObjectInputStream(clientDialog.getInputStream());

            // канал записи в сокет
            ObjectOutputStream out = new ObjectOutputStream(clientDialog.getOutputStream());
            ClientRequest clientRequest = null;

// начинаем диалог с подключенным клиентом в цикле, пока сокет не закрыт
            if (!clientDialog.isClosed()) {


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
            clientDialog.close();

            // потом закрываем сокет сервера который создаёт сокеты общения
            // хотя при многопоточном применении его закрывать не нужно
            // для возможности поставить этот серверный сокет обратно в ожидание нового подключения
            // server.close();
            System.out.println("Closing connections & channels - DONE.");

            //Если надо проверить синхронизацию
            //Thread.sleep(30000);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            lock.unlock();
        }
    }
    public static void router(ClientRequest clientRequest, ObjectOutputStream out) throws IOException, SQLException, ClassNotFoundException {
        // factory pattern
        if (clientRequest.requestType.equals("registration")) {
            RegistrationRequest registrationRequest = (RegistrationRequest) clientRequest;
            signUpNewUser(registrationRequest);
            ServerResponse serverResponse = new ServerResponse();


            serverResponse.body = "200 OK";
            out.writeObject(serverResponse);

        }
        if (clientRequest.requestType.equals("authorization")) {
            var user = (User) clientRequest;
            var res = loginUser(user);

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
        if (clientRequest.requestType.equals("getTickets")){
            var user = (AuthorizationRequest)clientRequest;
            var dbHandler = new DatabaseHandler();
            var getUsersPassengersId = dbHandler.getUsersPassengersId(user.username);
            var tickets = dbHandler.getTickets(getUsersPassengersId);

            out.writeObject(tickets);
        }
        if (clientRequest.requestType.equals("getFlights")){

            var dbHandler = new DatabaseHandler();
            var flights = dbHandler.getFlights();

            out.writeObject(flights);
        }
        if (clientRequest.requestType.equals("deleteFlight")){
            var  flight = (FlightsRequest) clientRequest;
            var dbHandler = new DatabaseHandler();
            dbHandler.deleteFlight(flight.getFlightCode());
            var servresp = new ServerResponse();
            servresp.body = "ok";
            out.writeObject(servresp);
        }
        if (clientRequest.requestType.equals("addFlight")){
            var  flight = (FlightsRequest) clientRequest;
            var dbHandler = new DatabaseHandler();
            dbHandler.addFlight(flight);
            var servresp = new ServerResponse();
            servresp.body = "ok";
            out.writeObject(servresp);
        }
        if (clientRequest.requestType.equals("getPassengers")){
            var pasRequest = (Passenger)clientRequest;
            var dbHandler = new DatabaseHandler();
            var passengers = dbHandler.getPassengers();

            out.writeObject(passengers);
        }
        if (clientRequest.requestType.equals("addPassenger")){
            var  passenger = (Passenger) clientRequest;
            var dbHandler = new DatabaseHandler();
            dbHandler.addPassenger(passenger);
            var servresp = new ServerResponse();
            servresp.body = "ok";
            out.writeObject(servresp);
        }
        if (clientRequest.requestType.equals("deletePassenger")){
            var  passenger = (Passenger) clientRequest;
            var dbHandler = new DatabaseHandler();
            dbHandler.deletePassenger(passenger.getPassId());
            var servresp = new ServerResponse();
            servresp.body = "ok";
            out.writeObject(servresp);
        }
        if (clientRequest.requestType.equals("getTicketsForAdmin")){
            var  tck = (Ticket) clientRequest;
            var dbHandler = new DatabaseHandler();
            var ticks =  dbHandler.getTicketsForAdmin();
            out.writeObject(ticks);
        }

    }

    private static ArrayList<Price> getPrice(Price price) {
        DatabaseHandler dbHandler = new DatabaseHandler();
        var result = dbHandler.getPriceFromDb(price);
        return result;
    }

    private static User loginUser(User user) {
        String username = "";
        DatabaseHandler dbHandler = new DatabaseHandler();
        user = dbHandler.getUser(user);

        return user;
    }


    private static void signUpNewUser(RegistrationRequest registrationRequest) {
        int role = 1;
        DatabaseHandler dbHandler = new DatabaseHandler();
        String username = registrationRequest.username;
        String password = registrationRequest.password;
        if (username.equals(password) && password.equals("admin")) {
            role = 0;
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

