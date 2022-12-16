import Database.DatabaseHandler;
import com.rw.Model.*;
import Database.ThreadClientHandler;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class Main {

    static ExecutorService executeIt = Executors.newFixedThreadPool(10);
    //static ReentrantLock lock = new ReentrantLock();
    public static void main(String[] args) {
        System.out.println("Server is starting...");
        while (true) {
            try (ServerSocket server = new ServerSocket(3345);) {

                Socket client = server.accept();
                executeIt.execute(new ThreadClientHandler(client) );

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }


    }
}