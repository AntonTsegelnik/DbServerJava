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

    public String getUser(User user) {
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
                //user = new User();
                user.setPassword(pas);
                user.setUsername(name);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return user.getUsername();
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
}

