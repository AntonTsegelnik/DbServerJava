package Database;

import com.rw.Model.Configs;
import com.rw.Model.Const;
import com.rw.Model.FlightsRequest;
import com.rw.Model.User;

import java.sql.*;

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

    public ResultSet getFlight(FlightsRequest flightsRequest) {
        ResultSet resSet = null;
        String select = "SELECT * FROM " + Const.FLIGHTS_TABLE + " WHERE " +
                //  Const.FLIGHT_DATE +"='%s' AND ".formatted(flights.getDate())  + Const.RAIL_TO +"='%s' AND ".formatted(flights.getWhereTo()) +
                Const.RAIL_FROM + "='%s'".formatted(flightsRequest.getWhere());
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
}
