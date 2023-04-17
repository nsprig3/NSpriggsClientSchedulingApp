package DAO;
/**
 * @author Natasha Spriggs
 */

import helper.JDBC;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.*;

import java.sql.*;

/**
 * This class queries the Users table of the database and selects requested appointments.
 */


public class DBUsers {
    public static ObservableList<Users> getAllUsers() {
        ObservableList<Users> usersList = FXCollections.observableArrayList();
        try{
            String sql = "SELECT * FROM USERS";

            PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while(rs.next()){
                int user_id = rs.getInt("User_ID");
                String userName = rs.getString("User_Name");
                String password = rs.getString("Password");


               Users user = new Users(user_id, userName, password);
               usersList.add(user);

            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return usersList;
    }

    public static ObservableList<Users> userLogin(String userName, String password, Timestamp loginTime){
        ObservableList<Users> validLoginList = FXCollections.observableArrayList();
        try{
            String sql = "SELECT * FROM Users WHERE User_Name = ? AND Password = ?";
            PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);

            ps.setString(1, userName);
            ps.setString(2, password);

            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                int userID = rs.getInt("User_ID");

                Users user = new Users(userID,userName, password, loginTime);

                validLoginList.add(user);
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return validLoginList;

    }




}
