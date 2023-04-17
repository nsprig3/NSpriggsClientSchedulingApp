package DAO;
/**
 * @author Natasha Spriggs
 */
import helper.JDBC;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.*;

import javax.imageio.plugins.jpeg.JPEGImageReadParam;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
/**
 * This class queries the Customers table of the database and selects, updates, and deletes selected customers.
 */

public class DBCustomer {
    public static ObservableList<Customer> getAllCustomers()  {
        ObservableList<Customer> customersList = FXCollections.observableArrayList();
        try{
            String sql = "SELECT * FROM CUSTOMERS";

            PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while(rs.next()){
                int customer_id = rs.getInt("Customer_ID");
                String customerName = rs.getString("Customer_Name");
                String address = rs.getString("Address");
                String postalCode = rs.getString("Postal_Code");
                String phone = rs.getString("Phone");
                int divID = rs.getInt("Division_ID");

                Customer C = new Customer(customer_id, customerName, address, postalCode, phone, divID);
                customersList.add(C);

            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return customersList;
    }


    public static void addCustomer(Customer customer) {

        try{
            String sql = "INSERT INTO CUSTOMERS (Customer_Name, Address, Postal_Code, Phone, Division_ID) VALUES (?,?,?,?,?)";
            PreparedStatement ps = JDBC.getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            ps.setString(1, customer.getCustomer_name());
            ps.setString(2, customer.getAddress());
            ps.setString(3, customer.getPostalCode());
            ps.setString(4, customer.getPhoneNumber());
            ps.setInt(5, customer.getDivision_ID());

            ps.execute();
            try {
                ResultSet rs = ps.getGeneratedKeys();
                while (rs.next()) {
                    customer.setCustomer_ID(rs.getInt(1));
                }
            }catch (SQLException s){
                s.printStackTrace();
            }

            } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }


    }

    public static ObservableList<Customer> deleteCustomer(Customer customer) {

        try {
            String sql = "DELETE FROM CUSTOMERS WHERE Customer_ID = ?";
            PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);

            ps.setInt(1, customer.getCustomer_ID());

            ps.execute();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        ObservableList<Customer>  updatedCustomerList = DBCustomer.getAllCustomers();

    return updatedCustomerList;
    }

    public static void updateCustomer(Customer customer){
        try{
            String sql = "UPDATE customers SET Customer_Name = ?, Address = ?, Postal_Code = ?, Phone = ?, Division_ID = ? WHERE Customer_ID = ?";
            PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);

            ps.setString(1,customer.getCustomer_name());
            ps.setString(2, customer.getAddress());
            ps.setString(3, customer.getPostalCode());
            ps.setString(4, customer.getPhoneNumber());
            ps.setInt(5, customer.getDivision_ID());
            ps.setInt(6, customer.getCustomer_ID());
            ps.execute();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }

}


