package view_controller;
/**
 * @author Natasha Spriggs
 */

import DAO.*;
import helper.timeZone;
import javafx.collections.*;
import javafx.event.ActionEvent;
import javafx.fxml.*;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.*;

import java.io.IOException;
import java.net.*;
import java.sql.*;
import java.time.*;
import java.util.*;

/**
 * This class controls the Add Appointment screen.
 */

public class AddAppointmentController implements Initializable {
    ObservableList<Customer> allCustomers = DBCustomer.getAllCustomers();
    ObservableList<String> listOfCustomers = FXCollections.observableArrayList();
    ObservableList<Users> allUsers = DBUsers.getAllUsers();
    ObservableList<String > listOfUsers = FXCollections.observableArrayList();

    ObservableList<Contacts> allContacts = DBContacts.getAllContacts();
    ObservableList<String> listOfContacts = FXCollections.observableArrayList();
    ObservableList<String > listOfTypes = FXCollections.observableArrayList("De-Briefing", "Planning Session", "OTHER");
    Stage stage;
    Parent scene;



    //buttons
    @FXML
    private Button AddAppointmentCancclBtn;
    @FXML
    private Button AddAppointmentSaveBtn;

    //combo boxes
    @FXML
    private ComboBox<String> AddAppointmentContactComboBox;
    @FXML
    private ComboBox<LocalTime> AddAppointmentEndTimeComboBox;
    @FXML
    private ComboBox<String> AddAppointmentTypeComboBox;
    @FXML
    private ComboBox<String> AddAppointmentUserIDComboBox;
    @FXML
    private ComboBox<String> AddAppointmentCustomerIDComboBox;
    @FXML
    private ComboBox<LocalTime> AddAppointmentStartTimeComboBox;

    //text fields
    @FXML
    private TextField AddAppointmentTitleTxt;

    @FXML
    private TextField AddAppointmentTxt;

    @FXML
    private TextField AddAppointmentCustomerIDTxt;
    @FXML
    private TextField AddAppointmentLocationTxt;
    @FXML
    private TextArea AddAppointmentDescTxt;

    //date pickers
    @FXML
    private DatePicker AddAppointmentEndDatePick;

    @FXML
    private DatePicker AddAppointmentStartDatePick;

    /**
     * This method receives the parameters for an appointment, checks to see if the boxes are empty, and alerts user with
     * a detailed string if there is missing data. Also compares start to current time for appointments that are
     * scheduled before the current time, and compares end time to start for logical errors.
     * @param title appointment title, String.
     * @param description appointment description, String.
     * @param location appointment location, String.
     * @param type appointment type, String.
     * @param contact appointment contact, String.
     * @param startDate LocalDate for appointment start date.
     * @param startTime LocalTime for appointment start time.
     * @param endDate LocalDate for appointment end date.
     * @param endTime LocalTime for appointment end time.
     * @param customerID appointment customerID, int.
     * @param userID appointment userID, int.
     * @return boolean isValid will return true if the boxes are all completed; false if there is any missing
     * data.
     */




    public boolean validateAppointment(String title, String description, String location, String type, String contact,
                                       LocalDate startDate, LocalTime startTime, LocalDate endDate, LocalTime endTime,
                                       String customerID, String userID){
        boolean isValid;
        StringBuilder missingData = new StringBuilder();
        if(title.isEmpty()){
            missingData.append("Please enter valid Title.\n");
        }
        if(description.isEmpty()){
            missingData.append("Please enter valid Description.\n");
        }
        if(location.isEmpty()){
            missingData.append("Please enter valid Location.\n");
        }
        if(type == null){
            missingData.append("Please enter valid Appointment Type .\n");
        }
        if(contact == null) {
            missingData.append("Please select valid contact.\n");

        }
        if(startDate == null){
            missingData.append("Please select valid start date.\n");
        }
        if(startTime == null){
            missingData.append("Please select valid start time.\n");
        }

        if(endDate == null){
            missingData.append("Please select valid end date.\n");
        }
        if(endTime== null){
            missingData.append("Please select valid end time.\n");
        }
        if(customerID == null){
            missingData.append("Please select valid Customer ID.\n");
        }
        if(userID == null){
            missingData.append("Please select valid User ID.\n");
        }
        if (!(startDate == null) && !(startTime == null) && !(endDate == null) && !(endTime == null)) {
            LocalDateTime userDateTimeNow = LocalDateTime.now();
            LocalDateTime start = LocalDateTime.of(startDate, startTime);
            LocalDateTime end = LocalDateTime.of(endDate, endTime);

            if (start.isBefore(userDateTimeNow)) {
                missingData.append("Invalid start: new appointment cannot occur in the past.\n");
            }
            if (end.isBefore(start)) {
                missingData.append("Start date and time must occur before End date and time.\n");
            }
        }

        if(missingData.length() > 0){
            isValid = false;
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Incomplete Appointment Data");
            alert.setContentText("Please correct the following: " + missingData);
            alert.showAndWait();
        }
        else{
            isValid = true;
        }
        return isValid;
    }


    @FXML
    void OnActionDisplayMenu(ActionEvent event) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Cancel new appointment?");
        Optional<ButtonType> result  = alert.showAndWait();
        if(result.isPresent() && result.get() == ButtonType.OK) {

            this.stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            this.scene = (Parent) FXMLLoader.load(this.getClass().getResource("ViewAppointments.fxml"));
            this.stage.setScene(new Scene(this.scene));
            this.stage.show();
        }

    }

    /**
     * This method retrieves data from text fields, date pickers, and combo boxes, validates for completion and compares
     * to EST business hours. It then checks for any overlapping customer appointments based on customerID selected by
     * user. It then creates new appointment if there are no issues.
     */

    @FXML
    void OnActionSaveDisplayMenu(ActionEvent event) throws IOException {
        try {
            int appointmentID = -1;
            String title = AddAppointmentTitleTxt.getText();
            String description = AddAppointmentDescTxt.getText();
            String location = AddAppointmentLocationTxt.getText();
            String type = AddAppointmentTypeComboBox.getValue();
            String contact = AddAppointmentContactComboBox.getValue();
            LocalDate ldStart = AddAppointmentStartDatePick.getValue();
            LocalTime ltStart = AddAppointmentStartTimeComboBox.getValue();
            LocalDate ldEnd = AddAppointmentEndDatePick.getValue();
            LocalTime ltEnd = AddAppointmentEndTimeComboBox.getValue();
            String customerIDString = AddAppointmentCustomerIDComboBox.getValue();
            String userIDString = AddAppointmentUserIDComboBox.getValue();
            int contactID = -1;


            //checks for empty boxes
            if (validateAppointment(title, description, location, type, contact, ldStart, ltStart, ldEnd, ltEnd, customerIDString, userIDString)) {
                if (contact.equals("1 Anika Costa")) {
                    contactID = 1;
                } else if (contact.equals("2 Daniel Garcia")) {
                    contactID = 2;
                } else if (contact.equals("3 Li Lee")) {
                    contactID = 3;
                }
            /*
                if (!(title.isEmpty()) && !(description.isEmpty()) && !(location.isEmpty()) && !(type.isEmpty()) && !(AddAppointmentContactComboBox.getValue() == null)
                    && !(AddAppointmentStartDatePick.getValue() == null) && !(AddAppointmentStartTimeComboBox.getValue() == null)
                    && !(AddAppointmentEndDatePick.getValue() == null) && !(AddAppointmentEndTimeComboBox.getValue() == null)
                    && !(AddAppointmentCustomerIDComboBox.getValue() == null) && !(AddAppointmentUserIDComboBox.getValue() == null)
                    && !(AddAppointmentUserIDComboBox.getValue() == null)) {


             */

                int customerID = Integer.parseInt(customerIDString);
                int userID = Integer.parseInt(userIDString);


                //create localDateTime object for start, convert to EST in order to compare to business hours
                LocalDateTime ldtStart = LocalDateTime.of(ldStart, ltStart);
                LocalDateTime ldtStartEST = timeZone.toEST(ldtStart);

                //create localDateTime object for end, convert to EST in order to compare to business hours
                LocalDateTime ldtEnd = LocalDateTime.of(ldEnd, ltEnd);
                LocalDateTime ldtEndEST = timeZone.toEST(ldtEnd);

                //create timestamps for db
                Timestamp startTimeDateStamp = Timestamp.valueOf(ldtStart);
                Timestamp endTimeDateStamp = Timestamp.valueOf(ldtEnd);


                Appointments appointment = new Appointments(appointmentID, title, description, location, type, startTimeDateStamp, endTimeDateStamp, customerID, userID, contactID);

                //compares appointment time to EST business hours
                if (!timeZone.isValidTime(ldStart, ldtStartEST, ldtEndEST)) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Outside of Business Hours");
                    alert.setContentText("Business scheduling hours are daily from 8:00AM to 10:00 PM EST");
                    alert.showAndWait();
                }
                //appointment falls within business hours, check for existing customer appointments
                else {
                    ObservableList<Appointments> validateCustomerAppointmentList = DBAppointments.customerAppointmentChecker(customerID);
                    LocalDateTime newAppointmentStart = ldtStart;
                    LocalDateTime newAppointmentEnd = ldtEnd;

                    //returns empty list if there are no appointments for this customer
                    if (validateCustomerAppointmentList.isEmpty()) {
                        DBAppointments.addAppointment(appointment);

                        this.stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
                        this.scene = (Parent) FXMLLoader.load(this.getClass().getResource("ViewAppointments.fxml"));
                        this.stage.setScene(new Scene(this.scene));
                        this.stage.show();

                    }


                    //appointments exist for customer
                    else {
                        ObservableList<Appointments> overlappingAppointments = FXCollections.observableArrayList();

                        for (Appointments checkAppointment : validateCustomerAppointmentList) {
                            LocalDateTime oldAppointmentStartTime = checkAppointment.getStartDateTime().toLocalDateTime();
                            LocalDateTime oldAppointmentEndTime = checkAppointment.getEndDateTime().toLocalDateTime();


                            //checks for overlapping appointments
                            if (((newAppointmentEnd.isBefore(oldAppointmentEndTime)) && (newAppointmentEnd.isAfter(oldAppointmentStartTime)))  //NEW APPOINTMENT ENDS BETWEEN OLD APPOINTMENT START AND END
                                    || ((newAppointmentStart.isBefore(oldAppointmentEndTime)) && (newAppointmentStart.isAfter(oldAppointmentStartTime))) //NEW APPOINTMENT STARTS BETWEEN OLD APPOINTMENT START AND END
                                    || ((newAppointmentEnd.isAfter(oldAppointmentEndTime)) && (newAppointmentStart.isBefore(oldAppointmentStartTime)))  //OLD APPOINTMENT START AND END IS BETWEEN NEW START AND END
                                    || (newAppointmentStart.isEqual(oldAppointmentStartTime)) || (newAppointmentEnd).isEqual(oldAppointmentEndTime)) {

                                overlappingAppointments.add(checkAppointment);
                            }
                        }

                            //list is not empty, overlapping appointment found
                            if(!overlappingAppointments.isEmpty()){
                                System.out.println("overlapping appointments");
                            Alert alert = new Alert(Alert.AlertType.ERROR);
                            alert.setTitle("Overlapping Appointment");
                            alert.setContentText("An appointment already exists for this customer at scheduled time.");
                            alert.showAndWait();
                            }

                            //no overlapping appointment found
                            else {
                                DBAppointments.addAppointment(appointment);

                                this.stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
                                this.scene = (Parent) FXMLLoader.load(this.getClass().getResource("ViewAppointments.fxml"));
                                this.stage.setScene(new Scene(this.scene));
                                this.stage.show();
                            }
                        }
                    }
                }

        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
    }

    /**
     * override of initialize method includes lambda to fill the contacts, userID, and customerID combo boxes. This
     * lambda condenses the code and makes it more readable
     */


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //populate contacts combobox
        //lambdas to fill contacts and countries lists
        allContacts.forEach(contacts -> listOfContacts.add(contacts.toString()));
        AddAppointmentContactComboBox.setItems(listOfContacts);

        //populate userID combo box
        allUsers.forEach(users -> listOfUsers.add(String.valueOf(users.getUser_ID())));
        AddAppointmentUserIDComboBox.setItems(listOfUsers);

        //populate customer ID combobox
        allCustomers.forEach(customer -> listOfCustomers.add(String.valueOf(customer.getCustomer_ID())));
        AddAppointmentCustomerIDComboBox.setItems(listOfCustomers);


        //populate type combobox

        AddAppointmentTypeComboBox.setItems(listOfTypes);
        //populate start and end times combo boxes

        AddAppointmentStartTimeComboBox.getItems().clear();
        AddAppointmentEndTimeComboBox.getItems().clear();
        LocalTime firstAvailableTime = LocalTime.of(4,0);
        LocalTime lastAvailableTime = LocalTime.of(23,0);

        while (firstAvailableTime.isBefore(lastAvailableTime.plusSeconds(1))){
            AddAppointmentStartTimeComboBox.getItems().add(firstAvailableTime);
            AddAppointmentEndTimeComboBox.getItems().add(firstAvailableTime);
            firstAvailableTime = firstAvailableTime.plusMinutes(15);
        }



    }
    }


