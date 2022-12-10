package Database;

import com.rw.Model.*;

import javax.swing.*;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;

public class DatabaseHandler extends Configs {
    Connection dbConnection;

    public Connection getDbConnection()
            throws ClassNotFoundException, SQLException {
        String connectionString = "jdbc:mysql://" + dbHost + ":"
                + dbPort + "/" + dbName;
        Class.forName("com.mysql.cj.jdbc.Driver");

        dbConnection = DriverManager.getConnection(connectionString,
                dbUser, dbPass);
        return dbConnection;
    }

    public ArrayList<ServerFlightsResponse> getFlight(FlightsRequest flightsRequest) {
        ResultSet resSet = null;
        var tickets = new ArrayList<ServerFlightsResponse>();

        String select = "SELECT * FROM " + Const.FLIGHTS_TABLE + " WHERE " +
                Const.FLIGHT_DATE + "='%s' AND ".formatted(flightsRequest.getDate()) + Const.RAIL_TO + "='%s' AND ".formatted(flightsRequest.getWhereTo()) +
                Const.RAIL_FROM + "='%s'".formatted(flightsRequest.getWhere());
        try {
            PreparedStatement prSt = getDbConnection().prepareStatement(select);

            resSet = prSt.executeQuery(select);
            while (resSet.next()) {
                String flightCode = resSet.getString(Const.FLIGHT_CODE);
                LocalDate date = LocalDate.parse(resSet.getString(Const.FLIGHT_DATE));
                String from = resSet.getString(Const.RAIL_FROM);
                String to = resSet.getString(Const.RAIL_TO);
                String time = resSet.getString(Const.TIME);
                String timeAr = resSet.getString(Const.TIME_AR);
                int couple = resSet.getInt(Const.NUM_OF_COUPE);
                int reserved = resSet.getInt(Const.NUM_OF_RES);
                int seats = resSet.getInt(Const.NUM_OF_SEATS);
                tickets.add(new ServerFlightsResponse(from, to, date, time, timeAr, flightCode, couple, reserved, seats));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return tickets;
    }

    public void signUpUser(User user) {
        String insert = "INSERT INTO " + Const.USER_TABLE + "(" +
                Const.USERNAME + "," + Const.USER_PASSWORD + "," + Const.USER_ROLE + ")" +
                "VALUES(?,?,?)";
        try {

            PreparedStatement prSt = getDbConnection().prepareStatement(insert);
            prSt.setString(1, user.getUsername());
            prSt.setString(2, user.getPassword());
            prSt.setInt(3, user.getRole());
            prSt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public User getUser(User user) {
        ResultSet resSet = null;
        String result = "";
        String select = "SELECT * FROM " + Const.USER_TABLE + " WHERE " +
                Const.USERNAME + "='%s' AND ".formatted(user.getUsername()) + Const.USER_PASSWORD + "='%s'".formatted(user.getPassword());
        try {
            PreparedStatement prSt = getDbConnection().prepareStatement(select);

            resSet = prSt.executeQuery(select);
            while (resSet.next()) {
                String name = resSet.getString(Const.USERNAME);
                String pas = resSet.getString(Const.USER_PASSWORD);
                int role = resSet.getInt(Const.USER_ROLE);
                //user = new User();
                user.setPassword(pas);
                user.setUsername(name);
                user.setRole(role);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return user;
    }

    public ArrayList<Price> getPriceFromDb(Price price) {
        var prices = new ArrayList<Price>();
        String select = "SELECT * FROM " + Const.PRICE_TABLE + " WHERE " +
                Const.FLIGHT_CODE + "='%s'".formatted(price.getFlightCode());
        try {
            PreparedStatement prSt = getDbConnection().prepareStatement(select);

            var resSet = prSt.executeQuery(select);
            while (resSet.next()) {
                String flcode = resSet.getString("flight_code");
                String seatType = resSet.getString("seat_type");
                Double prc = resSet.getDouble("ticket_price");
                prices.add(new Price(flcode, seatType, prc));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return prices;
    }

    public int setPassengerInDb(Passenger passenger) {
        String insert = "INSERT INTO " + Const.PASSENGER_TABLE + "(" +
                Const.FIRST_NAME + "," + Const.LAST_NAME + ","
                + Const.COUNTRY + "," + Const.PASSPORT_NUM + "," + Const.USERNAME + ")" +
                "VALUES(?,?,?,?,?)";
        try {
            PreparedStatement prSt = getDbConnection().prepareStatement(insert, Statement.RETURN_GENERATED_KEYS);
            // prSt.setInt(1, passenger.getPassId());
            prSt.setString(1, passenger.getFirstName());
            prSt.setString(2, passenger.getLastName());
            prSt.setString(3, passenger.getCountry());
            prSt.setString(4, passenger.getPassportNum());
            prSt.setString(5, passenger.getUsername());
            int affectedRows = prSt.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Creating user failed, no rows affected.");
            }

            try (ResultSet generatedKeys = prSt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    passenger.setPassId(generatedKeys.getInt(1));
                } else {
                    throw new SQLException("Creating user failed, no ID obtained.");
                }


            } catch (SQLException e) {
                throw new RuntimeException(e);
            }


        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return (passenger.getPassId());
    }

    public Ticket setTicketInDbBooking(Ticket ticket) throws SQLException, ClassNotFoundException {
        //To do set in ticket db
        //To do set in booking db
        //To do change num of seats in db
        String insert = "INSERT INTO " + Const.TICKET_TABLE + "(" +
                Const.TRAIN_CAR + "," + Const.SEAT_NUM + "," + Const.SEAT_TYPE + ","
                + Const.PASSENGER_ID + "," + Const.FLIGHT_CODE + ")" +
                "VALUES(?,?,?,?,?)";
        try {
            PreparedStatement prSt = getDbConnection().prepareStatement(insert, Statement.RETURN_GENERATED_KEYS);
            prSt.setInt(1, ticket.getTrainCar());
            prSt.setInt(2, ticket.getSeatNum());
            prSt.setString(3, ticket.getSeatType());
            prSt.setInt(4, ticket.getPassId());
            prSt.setString(5, ticket.getFlightCode());
            // prSt.setInt(6, null);
            int affectedRows = prSt.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Creating ticket failed, no rows affected.");
            }

            try (ResultSet generatedKeys = prSt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    ticket.setTicketCode(generatedKeys.getInt(1));
                } else {
                    throw new SQLException("Creating ticket failed, no ID obtained.");
                }


            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

            ticket.setSeatNum(ticket.getSeatNum() - 1);
            updateNumOfSeats(ticket);
            addInBookingDb(ticket);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
        return ticket;
    }

    private void addInBookingDb(Ticket ticket) {
        String insert = "INSERT INTO " + Const.BOOKING_TABLE + "(" +
                Const.TICKET_CODE + ")" +
                "VALUES(?)";
        try {
            PreparedStatement prSt = getDbConnection().prepareStatement(insert);
            prSt.setInt(1, ticket.getTicketCode());
            // prSt.setInt(6, null);
            prSt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public void updateNumOfSeats(Ticket ticket) throws SQLException, ClassNotFoundException {
        String dbType = "";
        if (ticket.getSeatType().equals("Купейный")) {
            dbType = Const.NUM_OF_COUPE;

        }
        if (ticket.getSeatType().equals("Плацкартный")) {
            dbType = Const.NUM_OF_RES;
        }
        if (ticket.getSeatType().equals("Сидячий")) {
            dbType = Const.NUM_OF_SEATS;
        }


        var query6 = ("""
                UPDATE flights
                SET %s=%d 
                WHERE flight_code='%s' """.formatted(dbType, ticket.getSeatNum(), ticket.getFlightCode()));
        PreparedStatement stmt = getDbConnection().prepareStatement(query6);
        stmt.executeUpdate(query6);


    }

    public ArrayList<Integer> getUsersPassengersId(String username) {
        ResultSet resSet = null;
        ArrayList<Integer> passengersId = new ArrayList<Integer>();
        String result = "";
        String select = "SELECT * FROM " + Const.PASSENGER_TABLE + " WHERE " +
                Const.USERNAME + "='%s' ".formatted(username);
        try {
            PreparedStatement prSt = getDbConnection().prepareStatement(select);

            resSet = prSt.executeQuery(select);
            while (resSet.next()) {
                int pasId = resSet.getInt(Const.PASSENGER_ID);
                System.out.println(pasId);
                passengersId.add(pasId);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return passengersId;
    }

    public ArrayList<Ticket> getTickets(ArrayList<Integer> passengersId) {
        ResultSet resSet = null;
        ArrayList<Ticket> tickets = new ArrayList<Ticket>();
        String result = "";
        for (var item : passengersId
        ) {
            String select = "SELECT * FROM " + Const.TICKET_TABLE + " WHERE " +
                    Const.PASSENGER_ID + "='%s' ".formatted(item);
            try {
                PreparedStatement prSt = getDbConnection().prepareStatement(select);

                resSet = prSt.executeQuery(select);
                while (resSet.next()) {

                    var tr = resSet.getInt(Const.TRAIN_CAR);
                    var sn = resSet.getInt(Const.SEAT_NUM);
                    var st = resSet.getString(Const.SEAT_TYPE);
                    var fc = resSet.getString(Const.FLIGHT_CODE);
                    var tc = resSet.getInt(Const.TICKET_CODE);

                    tickets.add(new Ticket(tr, sn, st, fc, tc));
                    System.out.println();
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }

        }


        return tickets;

    }

    public ArrayList<FlightsRequest> getFlights() {
        ResultSet resSet = null;
        ArrayList<FlightsRequest> flights = new ArrayList<FlightsRequest>();
        String result = "";

        String select = "SELECT * FROM " + Const.FLIGHTS_TABLE;
        try {
            PreparedStatement prSt = getDbConnection().prepareStatement(select);

            resSet = prSt.executeQuery(select);
            while (resSet.next()) {

                var fc = resSet.getString(Const.FLIGHT_CODE);
                var fd = resSet.getDouble(Const.FLIGHT_DISTANCE);
                var dd = LocalDate.parse(resSet.getString(Const.FLIGHT_DATE));
                var ad = LocalDate.parse(resSet.getString(Const.ARR_DATE));
                var nc = resSet.getInt(Const.NUM_OF_COUPE);
                var nr = resSet.getInt(Const.NUM_OF_RES);
                var ns = resSet.getInt(Const.NUM_OF_SEATS);
                var dt = resSet.getString(Const.TIME);
                var at = resSet.getString(Const.TIME_AR);
                var wh = resSet.getString(Const.RAIL_FROM);
                var rt = resSet.getString(Const.RAIL_TO);

                flights.add(new FlightsRequest(wh, rt, dd, dt, at, fc, nc, nr, ns, ad, fd));

            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }


        return flights;

    }

    public void deleteFlight(String flightCode) throws SQLException, ClassNotFoundException {
        ResultSet resSet = null;
        String result = "";

        String delete = "DELETE FROM " + Const.FLIGHTS_TABLE +
                " WHERE " + Const.FLIGHT_CODE + " ='%s' ".formatted(flightCode);

        // PreparedStatement prSt = getDbConnection().prepareStatement(select);
        PreparedStatement stmt = getDbConnection().prepareStatement(delete);
        stmt.executeUpdate(delete);
    }

    public void addFlight(FlightsRequest flight) {
        String insert = "INSERT INTO " + Const.FLIGHTS_TABLE + "(" +
                Const.FLIGHT_CODE + "," + Const.FLIGHT_DISTANCE + ","
                + Const.FLIGHT_DATE + "," + Const.ARR_DATE + ","
                + Const.NUM_OF_COUPE + "," + Const.NUM_OF_RES + ","
                + Const.NUM_OF_SEATS + "," + Const.TIME + ","
                + Const.TIME_AR + ","
                + Const.RAIL_FROM + "," + Const.RAIL_TO + ")" +
                "VALUES(?,?,?,?,?,?,?,?,?,?,?)";
        try {
            PreparedStatement prSt = getDbConnection().prepareStatement(insert);
            prSt.setString(1, flight.getFlightCode());
            prSt.setDouble(2, flight.getFlightDistance());
            prSt.setString(3, flight.getDate().toString());
            prSt.setString(4, flight.getDateAr().toString());
            prSt.setInt(5, flight.getCoupe());
            prSt.setInt(6, flight.getRes());
            prSt.setInt(7, flight.getSeats());
            prSt.setString(8, flight.getTime());
            prSt.setString(9, flight.getTimeAr());
            prSt.setString(10, flight.getWhere());
            prSt.setString(11, flight.getWhereTo());
            prSt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

    }

    public ArrayList<Passenger> getPassengers() {
        ResultSet resSet = null;
        ArrayList<Passenger> passengers = new ArrayList<Passenger>();
        String result = "";

        String select = "SELECT * FROM " + Const.PASSENGER_TABLE;
        try {
            PreparedStatement prSt = getDbConnection().prepareStatement(select);

            resSet = prSt.executeQuery(select);
            while (resSet.next()) {
                var firstname = resSet.getString(Const.FIRST_NAME);
                var lastName = resSet.getString(Const.LAST_NAME);
                var passId = resSet.getInt(Const.PASSENGER_ID);
                var country = resSet.getString(Const.COUNTRY);
                var pasNum = resSet.getString(Const.PASSPORT_NUM);
                var username = resSet.getString(Const.USERNAME);
                passengers.add(new Passenger(firstname,lastName,country,pasNum,passId, username));

            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }


        return passengers;

    }

    public void addPassenger(Passenger passenger) {
        String insert = "INSERT INTO " + Const.PASSENGER_TABLE + "(" +
                Const.PASSENGER_ID + "," + Const.PASSPORT_NUM + ","
                + Const.FIRST_NAME + "," + Const.LAST_NAME + ","
                + Const.COUNTRY + "," + Const.USERNAME + ")" +
                "VALUES(?,?,?,?,?,?)";
        try {
            PreparedStatement prSt = getDbConnection().prepareStatement(insert);

            prSt.setInt(1, passenger.getPassId());
            prSt.setString(2, passenger.getPassportNum());
            prSt.setString(3, passenger.getFirstName());
            prSt.setString(4, passenger.getLastName());
            prSt.setString(5, passenger.getCountry());
            prSt.setString(6, passenger.getUsername());
            prSt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

    }

    public void deletePassenger(int passengerId) throws SQLException, ClassNotFoundException {
        ResultSet resSet = null;
        String result = "";

        String delete = "DELETE FROM " + Const.PASSENGER_TABLE +
                " WHERE " + Const.PASSENGER_ID + " ='%s' ".formatted(passengerId);

        // PreparedStatement prSt = getDbConnection().prepareStatement(select);
        PreparedStatement stmt = getDbConnection().prepareStatement(delete);
        stmt.executeUpdate(delete);
    }

    public ArrayList getTicketsForAdmin() {
        ResultSet resSet = null;
        ArrayList<Ticket> tickets = new ArrayList<Ticket>();
        String result = "";

            String select = "SELECT * FROM " + Const.TICKET_TABLE ;
            try {
                PreparedStatement prSt = getDbConnection().prepareStatement(select);

                resSet = prSt.executeQuery(select);
                while (resSet.next()) {

                    var tr = resSet.getInt(Const.TRAIN_CAR);
                    var sn = resSet.getInt(Const.SEAT_NUM);
                    var st = resSet.getString(Const.SEAT_TYPE);
                    var fc = resSet.getString(Const.FLIGHT_CODE);
                    var tc = resSet.getInt(Const.TICKET_CODE);
                    var pi = resSet.getInt(Const.PASSENGER_ID);

                    tickets.add(new Ticket(tr, sn, st, fc, tc,pi));
                    System.out.println();
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        return tickets;
        }
}

