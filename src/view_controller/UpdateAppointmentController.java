package view_controller;
/**
 * @author Natasha Spriggs
 */

import DAO.DBAppointments;
import DAO.DBContacts;
import DAO.DBCustomer;
import DAO.DBUsers;
import helper.timeZone;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.stage.*;
import model.Appointments;
import model.Contacts;
import model.Customer;
import model.Users;

import java.io.IOException;
import java.net.URL;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

/**
 * This class controls the Update Appointment screen.
 */

public class UpdateAppointmentController implements Initializable {
    ObservableList<Customer> allCustomers = DBCustomer.getAllCustomers();
    ObservableList<String> listOfCustomers = FXCollections.observableArrayList();
    ObservableList<Users> allUsers = DBUsers.getAllUsers();
    ObservableList<String > listOfUsers = FXCollections.observableArrayList();
    ObservableList<Contacts> allContacts = DBContacts.getAllContacts();
    ObservableList<String> listOfContacts = FXCollections.observableArrayList();
    ObservableList<String > listOfTypes = FXCollections.observableArrayList("De-Briefing", "Planning Session", "OTHER");
    Stage stage;
    Parent scene;

    //labels
    @FXML
    private Label UpdateAppointmentAppointmentIDLbl;
    @FXML
    private Label UpdateAppointmentContactIDLbl;
    @FXML
    private Label UpdateAppointmentCustomerIDLbl;

    @FXML
    private Label UpdateAppointmentDescLbl;
    @FXML
    private Label UpdateAppointmentEndDateLbl;
    @FXML
    private Label UpdateAppointmentEndTimeLbl;
    @FXML
    private Label UpdateAppointmentLocationLbl;
    @FXML
    private Label UpdateAppointmentStartDateLbl;
    @FXML
    private Label UpdateAppointmentStartTimeLbl;

    @FXML
    private Label UpdateAppointmentTitleLbl;
    @FXML
    private Label UpdateAppointmentTypeLbl;
    @FXML
    private Label UpdateAppointmentUserIDLbl;

    //buttons
    @FXML
    private Button UpdateAppointmentCancelBtn;
    @FXML
    private Button UpdateAppointmentSaveBtn;

    //combo boxes
    @FXML
    private ComboBox<String> UpdateAppointmentContactComboBox;
    @FXML
    private ComboBox<String> UpdateAppointmentCustomerIDComboBox;
    @FXML
    private ComboBox<LocalTime> UpdateAppointmentEndTimeComboBox;
    @FXML
    private ComboBox<String> UpdateAppointmentTypeComboBox;
    @FXML
    private ComboBox<String> UpdateAppointmentUserIDComboBox;
    @FXML
    private ComboBox<LocalTime> UpdateAppointmentStartTimeComboBox;

    //text fields
    @FXML
    private TextArea UpdateAppointmentDescTxt;
    @FXML
    private TextField UpdateAppointmentIDTxt;
    @FXML
    private TextField UpdateAppointmentLocationTxt;
    @FXML
    private TextField UpdateAppointmentTitleTxt;

 //date pickers
    @FXML
    private DatePicker UpdateAppointmentEndDatePick;
    @FXML
    private DatePicker UpdateAppointmentStartDatePick;

    /**
     * This method receives the parameters for an appointment, checks to see if the boxes are empty, and alerts user with
     * a detailed string if there is missing data. Also compares start to current time for appointments that are
     * scheduled before the current time, and compares end time to start for logical errors.
     * @param title appointment title, String.
     * @param description appointment description, String.
     * @param location appointment location, String.
     * @param type appointment type, String.
     * @param contactID appointment contact, String.
     * @param ldStart LocalDate for appointment start date.
     * @param ltStart LocalTime for appointment start time.
     * @param ldEnd LocalDate for appointment end date.
     * @param ltEnd LocalTime for appointment end time.
     * @param customerIDString appointment customerID, int.
     * @param userIDString appointment userID, int.
     * @return boolean isValid will return true if the boxes are all completed; false if there is any missing
     * data.
     */

    public boolean validateAppointment(String title, String description, String location, String type,
                                       int contactID, LocalDate ldStart, LocalTime ltStart, LocalDate ldEnd, LocalTime ltEnd, String customerIDString, String userIDString) {

        boolean isValid = false;
        LocalDateTime start = LocalDateTime.of(ldStart, ltStart);
        LocalDateTime end = LocalDateTime.of(ldEnd, ltEnd);
        StringBuilder missingData = new StringBuilder();

        if (title.isEmpty()) {
            missingData.append("Please enter valid Title.\n");
        }
        if (description.isEmpty()) {
            missingData.append("Please enter valid Description.\n");
        }
        if (location.isEmpty()) {
            missingData.append("Please enter valid Location.\n");
        }
        if (type.isEmpty()) {
            missingData.append("Please enter valid Appointment Type .\n");
        }
        if (contactID == -1){
            missingData.append("Please select a valid contact.\n");
        }
        if (ldStart == null) {
            missingData.append("Please select valid start date.\n");
        }
        if (ltStart == null) {
            missingData.append("Please select valid start time.\n");
        }

        if (ldEnd == null) {
            missingData.append("Please select valid end date.\n");
        }
        if (ltEnd == null) {
            missingData.append("Please select valid end time.\n");
        }
        if (customerIDString.isEmpty()) {
            missingData.append("Please select valid Customer ID.\n");
        }
        if (userIDString.isEmpty()) {
            missingData.append("Please select valid User ID.\n");
        }

        if (!(ldStart == null) && !(ltStart == null) && !(ldEnd == null) && !(ltEnd == null)) {
            LocalDateTime userDateTimeNow = LocalDateTime.now();

            if (start.isBefore(userDateTimeNow)) {
                missingData.append("Invalid start: new appointment cannot occur in the past.\n");
            }
            if (end.isBefore(start)) {
                missingData.append("Start date and time must occur before End date and time.\n");

            }

            if (missingData.length() > 0) {
                isValid = false;
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Incomplete Appointment Data");
                alert.setContentText("Please correct the following: " + missingData);
                alert.showAndWait();
            } else {
                isValid = true;
            }
        }
            return isValid;

    }

    @FXML
    void OnActionDisplayMenu (ActionEvent event) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Cancel edit appointment?");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {

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
            int appointmentID = Integer.parseInt(UpdateAppointmentIDTxt.getText());
            String title = UpdateAppointmentTitleTxt.getText();
            String description = UpdateAppointmentDescTxt.getText();
            String location = UpdateAppointmentLocationTxt.getText();
            String type = UpdateAppointmentTypeComboBox.getValue();
            String contact = UpdateAppointmentContactComboBox.getValue();
            //convert local start date and time to EST
            LocalDate ldStart = UpdateAppointmentStartDatePick.getValue();
            LocalTime ltStart = UpdateAppointmentStartTimeComboBox.getValue();
            LocalDate ldEnd = UpdateAppointmentEndDatePick.getValue();
            LocalTime ltEnd = UpdateAppointmentEndTimeComboBox.getValue();
            String customerIDString = UpdateAppointmentCustomerIDComboBox.getValue();
            String userIDString = UpdateAppointmentUserIDComboBox.getValue();
            int contactID = -1;
            if (contact.equals("1 Anika Costa")) {
                contactID = 1;
            } else if (contact.equals("2 Daniel Garcia")) {
                contactID = 2;
            } else if (contact.equals("3 Li Lee")) {
                contactID = 3;
            }

            //checks for empty boxes
            if (validateAppointment(title, description, location, type, contactID, ldStart, ltStart, ldEnd, ltEnd, customerIDString, userIDString)) {
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

            System.out.println(contactID);

            //compares appointment time to EST business hours
            Appointments a = new Appointments(appointmentID, title, description, location, type, startTimeDateStamp, endTimeDateStamp, customerID, userID, contactID);

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
                    System.out.println("no appointments");

                    DBAppointments.updateAppointment(a);

                    this.stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
                    this.scene = (Parent) FXMLLoader.load(this.getClass().getResource("ViewAppointments.fxml"));
                    this.stage.setScene(new Scene(this.scene));
                    this.stage.show();


                }
                //appointments exist for customer
                else {
                    System.out.println("appointments exist");
                    ObservableList<Appointments> overlappingAppointments = FXCollections.observableArrayList();

                    for (Appointments checkAppointment : validateCustomerAppointmentList) {
                        LocalDateTime oldAppointmentStartTime = checkAppointment.getStartDateTime().toLocalDateTime();
                        LocalDateTime oldAppointmentEndTime = checkAppointment.getEndDateTime().toLocalDateTime();

                        //checks for overlapping appointments
                        if (((newAppointmentEnd.isBefore(oldAppointmentEndTime)) && (newAppointmentEnd.isAfter(oldAppointmentStartTime)))  //NEW APPOINTMENT ENDS BETWEEN OLD APPOINTMENT START AND END
                                || ((newAppointmentStart.isBefore(oldAppointmentEndTime)) && (newAppointmentStart.isAfter(oldAppointmentStartTime))) //NEW APPOINTMENT STARTS BETWEEN OLD APPOINTMENT START AND END
                                || ((newAppointmentEnd.isAfter(oldAppointmentEndTime)) && (newAppointmentStart.isBefore(oldAppointmentStartTime)))  //OLD APPOINTMENT START AND END IS BETWEEN NEW START AND END
                                || (newAppointmentStart.isEqual(oldAppointmentStartTime)) || (newAppointmentEnd).isEqual(oldAppointmentEndTime)) {
                            System.out.println("overlapping appointment found");
                            overlappingAppointments.add(checkAppointment);
                        }
                    }
                    //empty list, no overlapping appointment found
                    if(overlappingAppointments.isEmpty()) {
                        DBAppointments.updateAppointment(a);
                        System.out.println("No overlapping appointment found");
                        this.stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
                        this.scene = (Parent) FXMLLoader.load(this.getClass().getResource("ViewAppointments.fxml"));
                        this.stage.setScene(new Scene(this.scene));
                        this.stage.show();
                    }
                    //list is not empty, overlapping appointment found
                    else {
                        //matching appointmentID means it found the appointment that user is trying to update
                        boolean sameAppointment = false;
                        for(Appointments appointment: overlappingAppointments) {
                            if (appointment.getAppointment_ID() == a.getAppointment_ID()) {
                                sameAppointment = true;
                                System.out.println("updating this appointment!");
                                DBAppointments.updateAppointment(a);
                                this.stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
                                this.scene = (Parent) FXMLLoader.load(this.getClass().getResource("ViewAppointments.fxml"));
                                this.stage.setScene(new Scene(this.scene));
                                this.stage.show();
                                break;
                            }
                        }
                        //different appointmentID means overlapping appointment
                        if(!sameAppointment){
                           System.out.println ("overlapping appointment found :(");
                            Alert alert = new Alert(Alert.AlertType.ERROR);
                            alert.setTitle("Overlapping Appointment");
                            alert.setContentText("An appointment already exists for this customer at scheduled time.");
                            alert.showAndWait();
                            }
                        }
                    }
                }
            }
        }
              catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * This method retrieves the data from the appointment that the user would like to edit and fills the text fields,
     * date pickers and combo boxes.
     * It includes a lambda for filling the contact combo box. This lambda condenses the for:each loop and makes it more
     * readable.
     */

    public void sendAppointment(Appointments appointment){
        UpdateAppointmentIDTxt.setText(String.valueOf(appointment.getAppointment_ID()));
        UpdateAppointmentTitleTxt.setText(appointment.getTitle());
        UpdateAppointmentDescTxt.setText(appointment.getDescription());
        UpdateAppointmentLocationTxt.setText(appointment.getLocation());
        UpdateAppointmentTypeComboBox.setValue(appointment.getAppointmentType());
        LocalDateTime startLDT = appointment.getStartDateTime().toLocalDateTime();
        LocalDate sendLDStart = startLDT.toLocalDate();
        LocalTime sendLTStart= startLDT.toLocalTime();
        UpdateAppointmentStartDatePick.setValue(sendLDStart);
        UpdateAppointmentStartTimeComboBox.setValue(sendLTStart);
        LocalDateTime endLDT = appointment.getEndDateTime().toLocalDateTime();
        LocalDate sendLDEnd = endLDT.toLocalDate();
        LocalTime sendLTEnd = endLDT.toLocalTime();
        UpdateAppointmentEndDatePick.setValue(sendLDEnd);
        UpdateAppointmentEndTimeComboBox.setValue(sendLTEnd);
        UpdateAppointmentCustomerIDComboBox.setValue(String.valueOf(appointment.getCustomer_ID()));
        UpdateAppointmentUserIDComboBox.setValue(String.valueOf(appointment.getUser_ID()));
        //lamda for contact combo box
        int cID = appointment.getContact_id();
       allContacts.forEach(contacts -> {
           if(contacts.getContact_ID() == cID)
           UpdateAppointmentContactComboBox.setValue(contacts.toString());
               }
               );
    }

    /**
     * This override of initialize includes lambdas to populate userID, contact, and customer combo boxes. These
     * lambdas condense the code and makes it more readable
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //populate contacts combobox

        //lamdas to fill combo boxes
        allContacts.forEach(contacts -> listOfContacts.add(contacts.toString()));
        UpdateAppointmentContactComboBox.setItems(listOfContacts);

        //populate userID combo box
        //lamda
        allUsers.forEach(users -> listOfUsers.add(String.valueOf(users.getUser_ID())));
        UpdateAppointmentUserIDComboBox.setItems(listOfUsers);

        //populate customer ID combobox
        allCustomers.forEach(customer -> listOfCustomers.add(String.valueOf(customer.getCustomer_ID())));
        UpdateAppointmentCustomerIDComboBox.setItems(listOfCustomers);

        //populate type combobox

        UpdateAppointmentTypeComboBox.setItems(listOfTypes);

        //populate start and end times combo boxes
        LocalTime firstAvailableTime = LocalTime.of(4,0)    ;
        LocalTime lastAvailableTime = LocalTime.of(23, 0);

        while (firstAvailableTime.isBefore(lastAvailableTime.plusSeconds(1))){
            UpdateAppointmentStartTimeComboBox.getItems().add(firstAvailableTime);
            UpdateAppointmentEndTimeComboBox.getItems().add(firstAvailableTime);
            firstAvailableTime = firstAvailableTime.plusMinutes(15);
        }



    }
}

