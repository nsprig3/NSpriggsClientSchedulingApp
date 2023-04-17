package DAO;
/**
 * @author Natasha Spriggs
 */


import helper.JDBC;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.*;
import java.sql.*;
import java.time.*;
import java.time.format.DateTimeFormatter;

/**
 * This class queries the Appointments, Customers, and Countries tables of the database and selects requested appointments.
 */
public class DBReports {
    public static ObservableList<Reports> viewByTypeMonth(int monthInt) {
        ObservableList<Reports> reportsList = FXCollections.observableArrayList();
        try {
            String sql = "SELECT Type AS Appointment_Type, COUNT(Start) AS Monthly_Total FROM appointments WHERE YEAR(Start) = 2023 and MONTH(Start) = ? GROUP BY Type";
            PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);
            ps.setInt(1, monthInt);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                String appointmentType = rs.getString("Appointment_Type");
                int appointmentTotal = rs.getInt("Monthly_Total");
                Reports report = new Reports(appointmentType, appointmentTotal);
                reportsList.add(report);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return reportsList;
    }

    public static ObservableList<Reports> viewScheduleByContact(int contactID){
        ObservableList<Reports> schedulesList = FXCollections.observableArrayList();
        try{
            String sql = "SELECT Appointment_ID, Title, Type, Description, Start, End, Customer_ID FROM appointments WHERE Contact_ID = ? ORDER BY Start";
            PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);
            ps.setInt(1, contactID);
            ResultSet rs = ps.executeQuery();

            while(rs.next()){
                int appointmentID = rs.getInt("Appointment_ID");
                String title = rs.getString("Title");
                String type = rs.getString("Type");
                String description = rs.getString("Description");
                Timestamp start = rs.getTimestamp("Start");
                Timestamp end = rs.getTimestamp("End");
                int customerID = rs.getInt("Customer_ID");

                //convert start and end times to UTC and to localDate, localTime variables
                DateTimeFormatter df = DateTimeFormatter.ofPattern("MM-dd-yyyy");
                String formattedStart = start.toLocalDateTime().toLocalDate().format(df);
                //LocalDate startDate = LocalDate.parse(formattedStart);
                LocalTime startTime = start.toLocalDateTime().toLocalTime();
                String formattedEnd = end.toLocalDateTime().toLocalDate().format(df);
                LocalTime endTime = end.toLocalDateTime().toLocalTime();


                Reports report = new Reports(appointmentID, title, type, description, formattedStart, startTime, formattedEnd, endTime, customerID);
                schedulesList.add(report);

            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }


        return schedulesList;


    }

    public static ObservableList<Reports> viewCustomersByCountry (int countryID) {
        ObservableList<Reports> customersList = FXCollections.observableArrayList();
       try {
           String sql = "SELECT count(Customer_ID) AS Customer_Total FROM countries, customers inner join first_level_divisions where (customers.Division_ID = first_level_divisions.Division_ID) AND (first_level_divisions.Country_ID = countries.Country_ID) AND countries.Country_ID = ?";

           PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);
           ps.setInt(1, countryID);
           ResultSet rs = ps.executeQuery();

           while(rs.next()){
               int customerTotal = rs.getInt("Customer_Total");

               Reports report = new Reports(customerTotal);
               customersList.add(report);

           }

       } catch (SQLException throwables) {
           throwables.printStackTrace();
       }

        return customersList;
    }


}

