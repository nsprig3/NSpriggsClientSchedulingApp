package model;


import helper.JDBC;
import javafx.application.Application;
import javafx.fxml.*;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.*;
import java.sql.*;
import java.util.*;

/**
 * This is the main method and starting point for the application.
 */


public class LoginMenu extends Application implements Initializable {


    @Override
         public void start(Stage stage) throws IOException {

            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view_controller/LoginMenu.fxml"));

            Scene scene = new Scene(fxmlLoader.load(), 400, 300);
            stage.setTitle("Appointment Scheduler");
            stage.setScene(scene);
            stage.show();

    }



    public static void main(String[] args) throws SQLException {

        JDBC.openConnection();
/*
        Connection connection = JDBC.getConnection();
        DBQuery.setStatement(connection);
        Statement statement = DBQuery.getStatement();

*/


        launch(args);
        JDBC.closeConnection();


    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {


    }
}