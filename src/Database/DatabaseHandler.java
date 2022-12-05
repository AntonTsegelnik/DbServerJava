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
                Const.FLIGHT_DATE +"='%s' AND ".formatted(flightsRequest.getDate())  + Const.RAIL_TO +"='%s' AND ".formatted(flightsRequest.getWhereTo()) +
                Const.RAIL_FROM + "='%s'".formatted(flightsRequest.getWhere());
        try {
            PreparedStatement prSt = getDbConnection().prepareStatement(select);

            resSet = prSt.executeQuery(select);
            while (resSet.next()) {
                String flightCode = resSet.getString(Const.FLIGHT_CODE);
                LocalDate date   = LocalDate.parse(resSet.getString(Const.FLIGHT_DATE)) ;
                String from = resSet.getString( Const.RAIL_FROM);
                String to = resSet.getString(Const.RAIL_TO);
                String time = resSet.getString(Const.TIME);
                String timeAr = resSet.getString(Const.TIME_AR);
                int couple = resSet.getInt(Const.NUM_OF_COUPE);
                int reserved = resSet.getInt(Const.NUM_OF_RES);
                int seats = resSet.getInt(Const.NUM_OF_SEATS);
                 tickets.add(new ServerFlightsResponse(from,to,date,time, timeAr,flightCode,couple,reserved,seats));
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

    public ResultSet getUser(User user) {
        ResultSet resSet = null;
        String select = "SELECT * FROM " + Const.USER_TABLE + " WHERE " +
                Const.USERNAME + "='%s' AND ".formatted(user.getUsername()) + Const.USER_PASSWORD + "='%s'".formatted(user.getPassword());
        try {
            PreparedStatement prSt = getDbConnection().prepareStatement(select);

            resSet = prSt.executeQuery(select);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return resSet;
    }

    public ArrayList<Price> getPriceFromDb(Price price) {
        var prices = new ArrayList<Price>();
        String select = "SELECT * FROM " + Const.PRICE_TABLE + " WHERE " +
                Const.FLIGHT_CODE + "='%s'".formatted(price.getFlightCode());
        try {
            PreparedStatement prSt = getDbConnection().prepareStatement(select);

           var resSet = prSt.executeQuery(select);
           while (resSet.next()){
               String flcode = resSet.getString("flight_code");
               String seatType = resSet.getString("seat_type");
               Double prc = resSet.getDouble("ticket_price");
                prices.add(new Price(flcode,seatType,prc)) ;
           }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return prices;
    }
}
