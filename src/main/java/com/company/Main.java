package com.company;

import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Main {
    static final String DB_URL = "jdbc:postgresql://localhost:5432/telegram_bot";
    //jdbc:postgresql://localhost:5432/telegram_bot
    static final String USER = "telegram_bot_user";
    static final String PASS = "password";

    public static void main(String[] args) {
        System.out.println(System.getenv("TOKEN"));
        // Open a connection
//        try(Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
//            Statement stmt = conn.createStatement();
//        ) {
//            String sql = "CREATE DATABASE STUDENTS";
//            stmt.executeUpdate(sql);
//            System.out.println("Database created successfully...");
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }

//        Connection c = null;
//        Statement stmt = null;
//        try {
//            Class.forName("org.postgresql.Driver");
//            c = DriverManager
//                    .getConnection("jdbc:postgresql://localhost:5432/telegram_bot",
//                            "telegram_bot_user", "password");
//            System.out.println("Opened database successfully");
//
//            c.setAutoCommit(false);
//
//            stmt = c.createStatement();
//
//            stmt.close();
//            c.commit();
//            c.close();
//        } catch ( Exception e ) {
//            System.err.println( e.getClass().getName()+": "+ e.getMessage() );
//            System.exit(0);
//        }
//        System.out.println("Table created successfully");

        try{
            //register new bot
            TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
            telegramBotsApi.registerBot(new Bot());
        }catch(TelegramApiException exception){
            exception.printStackTrace();
        }


    }
}
