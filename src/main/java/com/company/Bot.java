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


            try{
                int input = Integer.parseInt(message);
                String response = "";
                int base = 2;
                int curr, power;
                boolean hasResult = false;
                while(base < 10 ){
                    curr = base;
                    power = 1;
                    while(curr <= input){
                        if(input - curr < 10){
                            hasResult = true;
                            String result = input + " = " + base + " ^ " + power + " + " + (input-curr) + "\n";
                            response += result;
                            execute(new SendMessage(chatID, result));
                        }
                        curr *= base;
                        power++;
                    }
                    base++;
                }
                if(!hasResult)
                    execute(new SendMessage(chatID, "没有理想结果"));

            }catch(Exception exception){
                exception.printStackTrace();
            }
        }
    }
}

