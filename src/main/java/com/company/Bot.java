package com.company;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.net.URI;
import java.net.URISyntaxException;
import java.sql.*;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

public class Bot extends TelegramLongPollingBot {
    final String botUsername = "my_Kurisu_Makise_bot";
    final String botToken = System.getenv("TOKEN");
    final String DATABASE_URL = System.getenv("DATABASE_URL");
    final String USER = System.getenv("USER");
    final String PASSWORD = System.getenv("PASSWORD");

//    final String botToken = "5053869817:AAFBYLYaFkzjV6aynBXcS-Fm-LSGJVLjwUg";

    @Override
    public String getBotUsername() {
        return botUsername;
    }

    @Override
    public String getBotToken() {
        return botToken;
    }

    private static Connection getConnection() throws URISyntaxException, SQLException {
        URI dbUri = new URI(System.getenv("DATABASE_URL"));

        String username = dbUri.getUserInfo().split(":")[0];
        String password = dbUri.getUserInfo().split(":")[1];
        String dbUrl = "jdbc:postgresql://" + dbUri.getHost() + ':' + dbUri.getPort() + dbUri.getPath();

        return DriverManager.getConnection(dbUrl, username, password);
    }

    @Override
    public void onUpdateReceived(Update update) {
        if(update.getMessage().hasText()){
            String chatID = update.getMessage().getChatId().toString();
            String message = update.getMessage().getText().toString();
//            String text = "Hello, my friend!";

            //store the message in the database
            Connection c = null;
            Statement stmt = null;
            try {
                Class.forName("org.postgresql.Driver");

                String MODE = System.getenv("MODE");
                if(MODE.equals("DEV")){
                    c = DriverManager
                            .getConnection(DATABASE_URL,
                                    USER, PASSWORD);
                }
                else if(MODE.equals("PROD")){
                    c = getConnection();
                }


                System.out.println("Opened database successfully");

                c.setAutoCommit(false);
                stmt = c.createStatement();


                if(!c.getMetaData().getTables(null, null, "messages", null).next()){
                    String sql = "CREATE TABLE MESSAGES " +
                            "(ID SERIAL NOT NULL PRIMARY KEY ," +
                            " MESSAGE           TEXT    NOT NULL, " +
                            " DATE            TEXT     NOT NULL)";

                    stmt.execute(sql);
                }

                String date = Instant.now().atZone(ZoneOffset.UTC).format(DateTimeFormatter.ISO_LOCAL_DATE);
                String query = "INSERT INTO MESSAGES(MESSAGE, DATE) VALUES(?, ?)";
                PreparedStatement pst = c.prepareStatement(query);
                pst.setString(1, message);
                pst.setString(2, date);
                pst.executeUpdate();


//                String sql = "INSERT INTO MESSAGES (MESSAGE,DATE) "
//                        + "VALUES (message, date );";
//                stmt.executeUpdate(sql);



                stmt.close();
                c.commit();
                c.close();
            } catch ( Exception e ) {
                System.err.println( e.getClass().getName()+": "+ e.getMessage() );
                System.exit(0);
            }
            System.out.println("Table created successfully");

            try{
                execute(new SendMessage(chatID, message));
            }catch(Exception exception){
                exception.printStackTrace();
            }
        }
    }
}
