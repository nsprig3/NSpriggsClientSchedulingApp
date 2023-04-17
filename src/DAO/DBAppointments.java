package DAO;
/**
 * @author Natasha Spriggs
 */

import helper.JDBC;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.*;
import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * This class queries the Appointments table of the database and selects, updates, and deletes selected appointments.
 */
public class DBAppointments {

    /**
     * This method returns all stored appointments.
     */
    public static ObservableList<Appointments> getAllAppointments() {
        ObservableList<Appointments> appointmentList = FXCollections.observableArrayList();
        try{
            String sql = "SELECT * from APPOINTMENTS";

            PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while(rs.next()){
                int appointment_id = rs.getInt("Appointment_ID");
                String title = rs.getString("Title");
                String description = rs.getString("Description");
                String location = rs.getString("Location");
                String type = rs.getString("Type");
                Timestamp appointmentStartTime = rs.getTimestamp("Start");
               // appointmentStartTime = Appointments.toSystemDefaultTime(appointmentStartTime);
                Timestamp appointmentEndTime = rs.getTimestamp("End");
                //Need to convert to local time
                int customer_ID = rs.getInt("Customer_ID");
                int user_ID = rs.getInt("User_ID");
                int contact_ID = rs.getInt("Contact_ID" );

                Appointments appointment = new Appointments(appointment_id, title, description, location, type, appointmentStartTime, appointmentEndTime, customer_ID, user_ID, contact_ID);
                appointmentList.add(appointment);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return appointmentList;
    }
    /**
     * This method receives an appointment and updates it based on the Appointment ID.
     */

    public static ObservableList<Appointments> updateAppointment(Appointments appointment) {
        ObservableList<Appointments> appointmentList = FXCollections.observableArrayList();
        try {
            String sql = "UPDATE appointments SET Title = ?, Description = ?, Location = ?, Type = ?, Start = ?, End = ?, Customer_ID = ?, User_ID = ?, Contact_ID = ? WHERE Appointment_ID = ?";
            PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);

            ps.setString(1, appointment.getTitle());
            ps.setString(2, appointment.getDescription());
            ps.setString(3, appointment.getLocation());
            ps.setString(4, appointment.getAppointmentType());
            ps.setTimestamp(5, appointment.getStartDateTime());
            ps.setTimestamp(6, appointment.getEndDateTime());
            ps.setInt(7, appointment.getCustomer_ID());
            ps.setInt(8, appointment.getUser_ID());
            ps.setInt(9, appointment.getContact_id());
            ps.setInt(10, appointment.getAppointment_ID());

            ps.execute();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return appointmentList;
    }
    /**
     * This method receives an appointment and it to the database.
     */

    public static void addAppointment(Appointments appointment) throws IOException {
        try {
            String sql = "INSERT INTO APPOINTMENTS (Title, Description, Location, Type, Start, End, Customer_ID, User_ID, Contact_ID) values (?,?,?,?,?,?,?,?,?)";
            PreparedStatement ps = JDBC.getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            ps.setString(1, appointment.getTitle());
            ps.setString(2, appointment.getDescription());
            ps.setString(3, appointment.getLocation());
            ps.setString(4, appointment.getAppointmentType());
            ps.setTimestamp(5, appointment.getStartDateTime());
            ps.setTimestamp(6, appointment.getEndDateTime());
            ps.setInt(7, appointment.getCustomer_ID());
            ps.setInt(8, appointment.getUser_ID());
            ps.setInt(9, appointment.getContact_id());

            ps.execute();
            try{
                ResultSet rs = ps.getGeneratedKeys();
                while (rs.next()) {
                    appointment.setAppointment_ID(rs.getInt(1));
                }
            }
            catch(SQLException ex){
                ex.printStackTrace();
            }


        }catch (SQLException throwables){
            throwables.printStackTrace();
        }

        }
    /**
     * This method receives an appointment and deletes it from the database based on the Appointment ID.
     */
    public static ObservableList<Appointments> deleteAppointment(Appointments appointments){
        try{
            String sql = "DELETE FROM APPOINTMENTS WHERE Appointment_ID = ?";
            PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);

            ps.setInt(1, appointments.getAppointment_ID());
            ps.execute();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        ObservableList<Appointments> updatedAppointmentList = DBAppointments.getAllAppointments();

        return updatedAppointmentList;
    }
    /**
     * This method uses the current date and the date plus one week to populate the selected table view with the appointments
     * for the week.
     */


    public static ObservableList<Appointments>  viewByWeek(){
        ObservableList<Appointments> appointmentsThisWeek = FXCollections.observableArrayList();
        Timestamp currentDateTime = Timestamp.valueOf(LocalDateTime.now());
        LocalDate current = currentDateTime.toLocalDateTime().toLocalDate();

        try {
            String sql = "SELECT * FROM appointments WHERE start >= ? AND end <= ?";

            PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);
            ps.setDate(1, Date.valueOf(current));
            ps.setDate(2, Date.valueOf(current.plusDays(8)));

            ResultSet rs = ps.executeQuery();

            while(rs.next()) {
                int appointment_id = rs.getInt("Appointment_ID");
                String title = rs.getString("Title");
                String description = rs.getString("Description");
                String location = rs.getString("Location");
                String type = rs.getString("Type");
                Timestamp appointmentStartTime = rs.getTimestamp("Start");
                Timestamp appointmentEndTime = rs.getTimestamp("End");
                int customer_ID = rs.getInt("Customer_ID");
                int user_ID = rs.getInt("User_ID");
                int contact_ID = rs.getInt("Contact_ID");

                Appointments appointment = new Appointments(appointment_id, title, description, location, type, appointmentStartTime, appointmentEndTime, customer_ID, user_ID, contact_ID);
                appointmentsThisWeek.add(appointment);
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return appointmentsThisWeek;
    }

    /**
     * This method uses the current date and the date plus one week to populate the selected table view with the appointments
     * for the month.
     */

    public static ObservableList<Appointments> viewByMonth (){
        ObservableList<Appointments> appointmentsThisMonth = FXCollections.observableArrayList();
        Timestamp currentDateTime = Timestamp.valueOf(LocalDateTime.now());
        LocalDate current = currentDateTime.toLocalDateTime().toLocalDate();

        try {
            String sql = "SELECT * FROM appointments WHERE start >= ? AND start <= ?";

            PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);

            ps.setDate(1, Date.valueOf(current));
            ps.setDate(2, Date.valueOf(current.plusMonths(1)));

            ResultSet rs = ps.executeQuery();

            while(rs.next()) {
                int appointment_id = rs.getInt("Appointment_ID");
                String title = rs.getString("Title");
                String description = rs.getString("Description");
                String location = rs.getString("Location");
                String type = rs.getString("Type");
                Timestamp appointmentStartTime = rs.getTimestamp("Start");
                // appointmentStartTime = Appointments.toSystemDefaultTime(appointmentStartTime);
                Timestamp appointmentEndTime = rs.getTimestamp("End");
                //Need to convert to local time
                int customer_ID = rs.getInt("Customer_ID");
                int user_ID = rs.getInt("User_ID");
                int contact_ID = rs.getInt("Contact_ID");

                Appointments appointment = new Appointments(appointment_id, title, description, location, type, appointmentStartTime, appointmentEndTime, customer_ID, user_ID, contact_ID);
                appointmentsThisMonth.add(appointment);
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return appointmentsThisMonth;
    }

    public static ObservableList<Appointments> customerAppointmentChecker(int customerID){
        ObservableList<Appointments> appointmentsByCustomerID = FXCollections.observableArrayList();
        try{
            String sql = "SELECT * FROM appointments WHERE Customer_ID = ?";
            PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);

            ps.setInt(1, customerID);
            ResultSet rs = ps.executeQuery();
            while (rs.next()){
                int appointment_id = rs.getInt("Appointment_ID");
                String title = rs.getString("Title");
                String description = rs.getString("Description");
                String location = rs.getString("Location");
                String type = rs.getString("Type");
                Timestamp appointmentStartTime = rs.getTimestamp("Start");
                Timestamp appointmentEndTime = rs.getTimestamp("End");
                int customer_ID = rs.getInt("Customer_ID");
                int user_ID = rs.getInt("User_ID");
                int contact_ID = rs.getInt("Contact_ID");

                Appointments a = new Appointments(appointment_id, title, description, location, type, appointmentStartTime, appointmentEndTime, customer_ID, user_ID, contact_ID);
                appointmentsByCustomerID.add(a);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return appointmentsByCustomerID;
    }

    /**
     * This method receives an appointment and uses the appointment ID to return the appointment if it exists in the database.
     * @param appointment the Appointment user is searching for.
     * @return sendAppointment, if the appointment exists in the database; Otherwise, returns null.
     */

    public static Appointments selectAppointment(Appointments appointment){
        Appointments sendAppointment = null;
        try {
            String sql = "SELECT * FROM appointments WHERE Appointment_ID = ?";

            PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);
            ps.setInt(1, appointment.getAppointment_ID());

            ResultSet rs = ps.executeQuery();

            while(rs.next()) {
                int appointment_id = rs.getInt("Appointment_ID");
                String title = rs.getString("Title");
                String description = rs.getString("Description");
                String location = rs.getString("Location");
                String type = rs.getString("Type");
                Timestamp appointmentStartTime = rs.getTimestamp("Start");
                Timestamp appointmentEndTime = rs.getTimestamp("End");
                int customer_ID = rs.getInt("Customer_ID");
                int user_ID = rs.getInt("User_ID");
                int contact_ID = rs.getInt("Contact_ID");

                Appointments a = new Appointments(appointment_id, title, description, location, type, appointmentStartTime, appointmentEndTime, customer_ID, user_ID, contact_ID);
                sendAppointment = a;
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return sendAppointment;
    }

    /**
     * This method receives a user and searches the database for a matching userID.
     * @param user User, the user to search for.
     * @return a list of all appointments for the userID.
     */


    public static ObservableList<Appointments> upcomingAppointmentsByUser(Users user) throws SQLException {
        ObservableList<Appointments> upcomingAppointmentsList = FXCollections.observableArrayList();
        String sql = "SELECT  * FROM appointments WHERE User_ID = ?";

        PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);
        ps.setInt(1, user.getUser_ID());

        ResultSet rs = ps.executeQuery();
        while(rs.next()){
            int appointment_id = rs.getInt("Appointment_ID");
            String title = rs.getString("Title");
            String description = rs.getString("Description");
            String location = rs.getString("Location");
            String type = rs.getString("Type");
            Timestamp appointmentStartTime = rs.getTimestamp("Start");
            Timestamp appointmentEndTime = rs.getTimestamp("End");
            int customer_ID = rs.getInt("Customer_ID");
            int user_ID = rs.getInt("User_ID");
            int contact_ID = rs.getInt("Contact_ID");

            Appointments appointment = new Appointments(appointment_id, title, description, location, type, appointmentStartTime, appointmentEndTime, customer_ID, user_ID, contact_ID);
            upcomingAppointmentsList.add(appointment);
        }

        return upcomingAppointmentsList;
    }

}
