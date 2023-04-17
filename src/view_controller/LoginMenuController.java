package view_controller;
/**
 * @author Natasha Spriggs
 */

import DAO.*;
import helper.timeZone;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import model.*;

import java.io.*;
import java.net.URL;
import java.sql.*;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * This class controls the login menu.
 */


public class LoginMenuController implements Initializable {
    ZoneId userZone = ZoneId.systemDefault();
    Stage stage;
    Parent scene;
    Locale userLocale;
    ResourceBundle rb;
    @FXML
    private Label LoginZoneIDLbl;
    @FXML
    private Button loginBtn;
    @FXML
    private Label loginPasswordLbl;
    @FXML
    private PasswordField loginPasswordTxt;
    @FXML
    private Label loginUserNameLbl;
    @FXML
    private TextField loginUsernameTxt;
    ObservableList<Appointments> upcomingAppointmentsList = FXCollections.observableArrayList();
    ObservableList<Users> allUsers = DBUsers.getAllUsers();

    /**
     * This boolean method receives a LocalDateTime objects for current time and current time plus 15 minutes. Will compare
     * these values to all upcoming appointments for the user to generate an alert if there is an appointment within 15 minutes
     * of login.
     * @param login LocalDateTime, the time the user successfully logged in.
     * @param loginPlus LocalDateTime, the the the user logged in plus 15 minutes.
     * @param appointment the appointment that login and loginPlus will be compared to.
     * @return boolean hasAppointment, returns true if the appointment occurs between login and loginPlus; otherwise returns false.
     */

    public boolean upcomingAppointment(LocalDateTime login, LocalDateTime loginPlus, Appointments appointment){
        boolean hasAppointment;
        LocalDateTime appointmentLDT = appointment.getStartDateTime().toLocalDateTime();

        if((appointmentLDT.isAfter(login)) && (appointmentLDT.isBefore(loginPlus))) {
            hasAppointment = true;
        }
        else{
            hasAppointment = false;
        }
        return hasAppointment;

    }

    /**
     * This method receives a user and a userName to compare for a match. This is to ensure the database query did not
     * return a false match due to case-insensitivity.
     * @param user User, the user to compare.
     * @param userName String, the string entered by user.
     * @return boolean foundUser returns true if userName exactly matches the user_Name stored in the database. Otherwise
     * returns false.
     */

    public boolean userMatch(Users user, String userName){

        boolean foundUser;
        if(user.getUserName().equals(userName)) {
            foundUser = true;
        }
        else{foundUser = false;}
        return foundUser;
    }

    /**
     * This method receives a user and a password to compare for a match. This is to ensure the database query did not
     * return a false match due to case-insensitivity.
     * @param user User, the user to compare.
     * @param password String, the string entered by user.
     * @return boolean foundUser returns true if password exactly matches the password stored in the database. Otherwise,
     * returns false.
     */
    public boolean passwordMatch(Users user, String password){
        boolean matchingPassword;
        if(user.getUserPassword().equals(password)){
            matchingPassword = true;
        }
        else {
            matchingPassword = false;
        }
        return matchingPassword;
    }

    /**
     * This method receives a username and password from user input and compares this to Users stored in the database.
     * A StringBuilder, printWriter, and FileWriter are used to track valid and invalid login attempts by user. If login
     * is successful, methods are called to determine if this user has any appointments within 15 minutes.
     */

    @FXML
    void onActionLogin(ActionEvent event) throws IOException {
        LocalDateTime loginTime = LocalDateTime.now();
        LocalDateTime loginTimePlus = loginTime.plusMinutes(15);


        try {
            ObservableList<Users> loginConfirmList;
            String userName = loginUsernameTxt.getText();
            String password = loginPasswordTxt.getText();
            DateTimeFormatter df = DateTimeFormatter.ofPattern("MM-dd-yyyy");
            Timestamp loginAttemptTime = Timestamp.valueOf(LocalDateTime.now());


            FileWriter fw = new FileWriter("login_activity.txt", true);
            PrintWriter outputFile = new PrintWriter(fw);
            StringBuilder userLoginData = new StringBuilder();

            loginConfirmList = DBUsers.userLogin(userName, password, loginAttemptTime);


            //login failed
            if(loginConfirmList.isEmpty()) {
                boolean userFound = false;
                for (Users u : allUsers)
                    if (u.getUserName().equals(userName)) {
                        userFound = true;
                    }
                //userName correct, password incorrect
                if (userFound) {
                    userLoginData.append("UNSUCCESSFUL LOGIN ATTEMPT BY USER: " + userName + " at: " + loginAttemptTime);
                    outputFile.println(userLoginData);

                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle(this.rb.getString("InvalidUser/Password"));
                    alert.setContentText(this.rb.getString("UsernameandPasswordCombination"));
                    alert.showAndWait();

                }

             else {
                 //incorrect username and password
                userLoginData.append("Invalid login attempt, no matching users found: " + loginAttemptTime);
                outputFile.println(userLoginData);

                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle(this.rb.getString("InvalidUser/Password"));
                alert.setContentText(this.rb.getString("UsernameandPasswordCombination"));
                alert.showAndWait();
            }
        }
            else {
                boolean userFound = false;
                boolean passwordFound = false;

                //username match checker for case sensitivity
                for (Users u : allUsers) {
                    if (userMatch(u, userName)) {
                        userFound = true;
                        if (passwordMatch(u, password)) {
                            passwordFound = true;
                        }
                    }
                }
                    if (!userFound) {
                        //incorrect username entered
                        userLoginData.append("Invalid login attempt, no matching users found: " + loginAttemptTime);
                        outputFile.println(userLoginData);

                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle(this.rb.getString("InvalidUser/Password"));
                        alert.setContentText(this.rb.getString("UsernameandPasswordCombination"));
                        alert.showAndWait();
                    }

                    if (userFound) {
                        //userName correct, password incorrect
                        if (!passwordFound) {
                            userLoginData.append("UNSUCCESSFUL LOGIN ATTEMPT BY USER: " + userName + " at: " + loginAttemptTime);
                            outputFile.println(userLoginData);

                            Alert alert = new Alert(Alert.AlertType.ERROR);
                            alert.setTitle(this.rb.getString("InvalidUser/Password"));
                            alert.setContentText(this.rb.getString("UsernameandPasswordCombination"));
                            alert.showAndWait();
                        }
                        //matching username and password
                        if(passwordFound){
                            try {
                                //login was successful
                                userLoginData.append("SUCCESSFUL LOGIN BY USER: " + userName + " at: " + loginAttemptTime);
                                outputFile.println(userLoginData);
                                ObservableList<Appointments> appointmentsList = null;

                                //get list of all appointments for this user
                                for (Users user : allUsers) {
                                    if (user.getUserName().equals(userName)) {
                                        appointmentsList = DBAppointments.upcomingAppointmentsByUser(user);
                                    }

                                }
                                if (appointmentsList == null) {
                                    //user has no appointments
                                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                                    alert.setTitle(this.rb.getString("NoUpcomingAppointmentsHeader"));
                                    alert.setContentText(this.rb.getString("NoAppointments"));
                                    alert.showAndWait();

                                    stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
                                    scene = FXMLLoader.load(getClass().getResource("MainMenu.fxml"));
                                    stage.setScene(new Scene(scene));
                                    stage.show();
                                }
                                //user has appointments
                                else {

                                    int appointmentID = -1;
                                    LocalDate appointmentDate = null;
                                    LocalTime appointmentTime = null;


                                    //look through user appointments for upcoming appointment within 15 minutes

                                    for (Appointments a : appointmentsList) {
                                        //appointment within 15 minutes
                                        if (upcomingAppointment(loginTime, loginTimePlus, a)) {
                                            appointmentID = a.getAppointment_ID();
                                            appointmentDate = a.getStartDateTime().toLocalDateTime().toLocalDate();
                                            appointmentTime = a.getStartDateTime().toLocalDateTime().toLocalTime();

                                            upcomingAppointmentsList.add(a);
                                            break;

                                        }
                                    }
                                    if (upcomingAppointmentsList.isEmpty()) {
                                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                                        alert.setTitle(this.rb.getString("NoUpcomingAppointmentsHeader"));
                                        alert.setContentText(this.rb.getString("NoAppointments"));
                                        alert.showAndWait();

                                    } else {
                                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                                        alert.setTitle(this.rb.getString("UpcomingAppointmentHeader"));
                                        alert.setContentText(this.rb.getString("UpcomingAppointment") + appointmentID + " at "
                                                + appointmentTime + " on " + appointmentDate.format(df));
                                        alert.showAndWait();
                                    }


                                    stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
                                    scene = FXMLLoader.load(getClass().getResource("MainMenu.fxml"));
                                    stage.setScene(new Scene(scene));
                                    stage.show();
                                }
                            }catch (SQLException e) {
                                throw new RuntimeException(e);
                            }


                        }
                    }
                }
            outputFile.close();
            }

        catch(IOException ioException) {
            ioException.printStackTrace();
        }

    }
    @FXML
    void handleDisplayTime(InputMethodEvent event) {

    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //collects user local from system Default and sets language accordingly.
        this.userLocale = Locale.getDefault();
        this.rb = ResourceBundle.getBundle("Lang", this.userLocale);


        loginUserNameLbl.setText(this.rb.getString("username"));
        loginPasswordLbl.setText(this.rb.getString("password"));
        LoginZoneIDLbl.setText(this.rb.getString("time_zone")+ userZone.toString());



    }
}
