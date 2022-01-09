package com.company;

import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DataBase {
    static final String DB_URL = "jdbc:postgresql://localhost:5432/telegram_bot";
    //jdbc:postgresql://localhost:5432/telegram_bot
    static final String USER = "telegram_bot_user";
    static final String PASS = "password";


    public DataBase() throws SQLException {

    }



    public void operate(Operation operation) {



        try{
            Class.forName("org.postgresql.Driver");
            Connection c = DriverManager.getConnection(DB_URL,USER, PASS);
            c.setAutoCommit(false);
            Statement stmt = c.createStatement();

            if(operation.equals(Operation.INSERT)){

            }

            stmt.close();
            c.commit();
            c.close();
        }catch(Exception e ){
            System.err.println( e.getClass().getName()+": "+ e.getMessage() );
            System.exit(0);
        }

    }
}

enum Operation{
    INSERT;
}
