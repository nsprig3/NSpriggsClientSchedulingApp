package DAO;
/**
 * @author Natasha Spriggs
 */

import helper.JDBC;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.*;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * This class queries the Contacts table of the database and returns all contacts.
 */

public class DBContacts {

    public static ObservableList<Contacts> getAllContacts() {
        ObservableList<Contacts> contactsList = FXCollections.observableArrayList();

       try {
           String sql = "SELECT * from Contacts";

           PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);
           ResultSet rs = ps.executeQuery();

           while(rs.next()){
               int contact_id = rs.getInt("Contact_ID");
               String contactName = rs.getString("Contact_Name");
               String email = rs.getString("Email");

               Contacts C = new Contacts(contact_id, contactName, email);
               contactsList.add(C);
           }

       } catch (SQLException throwables) {
           throwables.printStackTrace();
       }
        return contactsList;
    }
}
