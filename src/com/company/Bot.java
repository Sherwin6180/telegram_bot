package com.company;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.sql.*;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

public class Bot extends TelegramLongPollingBot {
    final String botUsername = "my_Kurisu_Makise_bot";
    final String botToken = "5053869817:AAFBYLYaFkzjV6aynBXcS-Fm-LSGJVLjwUg";

    @Override
    public String getBotUsername() {
        return botUsername;
    }

    @Override
    public String getBotToken() {
        return botToken;
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
                c = DriverManager
                        .getConnection("jdbc:postgresql://localhost:5432/telegram_bot",
                                "telegram_bot_user", "password");
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
