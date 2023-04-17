package view_controller;
/**
 * @author Natasha Spriggs
 */

import DAO.DBAppointments;
import DAO.DBContacts;
import DAO.DBCustomer;
import DAO.DBUsers;
import helper.JDBC;
import helper.timeZone;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.*;
import model.*;

import java.io.*;
import java.net.*;
import java.sql.*;
import java.util.*;

/**
 * This class controls the View Appointments screen.
 */

public class ViewAppointmentsController implements Initializable {

    Stage stage;
    Parent scene;

    //buttons
    @FXML
    private Button ViewAppointmentsAddAppointmentBtn;
    @FXML
    private Button ViewAppointmentsDeleteAppointmentBtn;
    @FXML
    private Button ViewAppointmentsMainMenuBtn;
    @FXML
    private Button viewAppointmentsViewCustomersBtn;

    //radio
    @FXML
    private RadioButton ViewAppointmentsAllRadio;
    @FXML
    private RadioButton ViewAppointmentsMonthRadio;
    @FXML
    private RadioButton ViewAppointmentsWeekRadio;

    //TableView

    @FXML
    private TableView<Appointments> ViewAppointmentsTableView;

    @FXML
    private TableColumn<Appointments, Integer> ViewAppointmentsTblApptIDCol;

    @FXML
    private TableColumn<Appointments, Integer> ViewAppointmentsTblContactIDCol;

    @FXML
    private TableColumn<Appointments, Integer> ViewAppointmentsTblCustIDCol;

    @FXML
    private TableColumn<Appointments, String> ViewAppointmentsTblDescriptionCol;

    @FXML
    private TableColumn<Appointments, Timestamp> ViewAppointmentsTblEndCol;

    @FXML
    private TableColumn<Appointments, String> ViewAppointmentsTblLocationCol;

    @FXML
    private TableColumn<Appointments, Timestamp> ViewAppointmentsTblStartCol;

    @FXML
    private TableColumn<Appointments, String> ViewAppointmentsTblTitleCol;

    @FXML
    private TableColumn<Appointments, String> ViewAppointmentsTblTypeCol;

    @FXML
    private TableColumn<Appointments, Integer> ViewAppointmentsTblUserIDCol;


    /**
     * This method receives a list of appointments and uses it to update the selected table view.
     */

    public void updateAppointmentsTableView(ObservableList<Appointments> appointmentList){
        ViewAppointmentsTableView.getItems().clear();
        ViewAppointmentsTableView.setItems(appointmentList);
    }


    //handlers
    @FXML
    void onActionDeleteAppointment(ActionEvent event) {
        ObservableList<Appointments> updatedAppointmentsList;
        Appointments a = ViewAppointmentsTableView.getSelectionModel().getSelectedItem();


        if(a == null){
            //user did not select item to delete
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("No Selection");
            alert.setContentText("Please select an appointment to delete");
            alert.showAndWait();

        }
        else {
            int appointmentID = a.getAppointment_ID();
            String appointmentType = a.getAppointmentType();

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Delete Appointment?");
            Optional<ButtonType> result  = alert.showAndWait();
            if(result.isPresent() && result.get() == ButtonType.OK) {
                Alert deletedAppointment = new Alert(Alert.AlertType.INFORMATION);
                deletedAppointment.setTitle("Appointment Deleted");
                deletedAppointment.setContentText("Appointment #" + appointmentID + ", Type: " + appointmentType + ", successfully deleted.");
                deletedAppointment.showAndWait();
            }
            updatedAppointmentsList = DBAppointments.deleteAppointment(a);
            updateAppointmentsTableView(updatedAppointmentsList);
        }
    }

    @FXML
    void onActionViewCustomers(ActionEvent event) throws IOException {
        stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("ViewCustomers.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();

    }

    @FXML
    void onActionDisplayMainMenu(ActionEvent event) throws IOException {
        stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("MainMenu.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }

    @FXML
    void onActionNewAppointment(ActionEvent event) throws IOException {
        stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("AddAppointment.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }

    /**
     * This method retrieves appointment selected by user and send it along with controller to the
     * update appointment screen.
     */
    @FXML
    void onActionUpdateAppointment(ActionEvent event) throws IOException {

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("UpdateAppointment.fxml"));
        loader.load();
        UpdateAppointmentController UAController = loader.getController();

        Appointments appointment = ViewAppointmentsTableView.getSelectionModel().getSelectedItem();
        if(appointment == null){
            //user did not select item to delete
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("No Selection");
            alert.setContentText("Please select an appointment to edit.");
            alert.showAndWait();
        }
        else {
            Appointments selectedAppointment = DBAppointments.selectAppointment(appointment);
            UAController.sendAppointment(selectedAppointment);

            stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            scene = loader.getRoot();
            stage.setScene(new Scene(scene));
            stage.show();
        }
    }

    @FXML
    void viewAllAppointments(ActionEvent event)  {
        ViewAppointmentsAllRadio.setSelected(true);
        ViewAppointmentsMonthRadio.setSelected(false);
        ViewAppointmentsWeekRadio.setSelected(false);
        updateAppointmentsTableView(DBAppointments.getAllAppointments());
    }

    /**
     * This method allows user to filter appointments and view the appointments coming up in the next month.
     */

    @FXML
    void viewByMonth(ActionEvent event) {
        ObservableList<Appointments> appointmentsThisMonth;
        ViewAppointmentsAllRadio.setSelected(false);
        ViewAppointmentsMonthRadio.setSelected(true);
        ViewAppointmentsWeekRadio.setSelected(false);
        appointmentsThisMonth = DBAppointments.viewByMonth();
        updateAppointmentsTableView(appointmentsThisMonth);

    }
    /**
     * This method allows user to filter appointments and view the appointments coming up in the next week.
     */

    @FXML
    void viewByWeek(ActionEvent event) {
        ObservableList<Appointments> appointmentsThisWeek;
        ViewAppointmentsAllRadio.setSelected(false);
        ViewAppointmentsMonthRadio.setSelected(false);
        ViewAppointmentsWeekRadio.setSelected(true);

        appointmentsThisWeek = DBAppointments.viewByWeek();
        updateAppointmentsTableView(appointmentsThisWeek);

    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        updateAppointmentsTableView(DBAppointments.getAllAppointments());
        ViewAppointmentsTblApptIDCol.setCellValueFactory(new PropertyValueFactory<>("Appointment_ID"));
        ViewAppointmentsTblTitleCol.setCellValueFactory(new PropertyValueFactory<>("Title"));
        ViewAppointmentsTblDescriptionCol.setCellValueFactory(new PropertyValueFactory<>("Description"));
        ViewAppointmentsTblLocationCol.setCellValueFactory(new PropertyValueFactory<>("Location"));
        ViewAppointmentsTblTypeCol.setCellValueFactory(new PropertyValueFactory<>("appointmentType"));
        ViewAppointmentsTblStartCol.setCellValueFactory(new PropertyValueFactory<>("startDateTime"));
        ViewAppointmentsTblEndCol.setCellValueFactory(new PropertyValueFactory<>("endDateTime"));
        ViewAppointmentsTblCustIDCol.setCellValueFactory(new PropertyValueFactory<>("Customer_ID"));
        ViewAppointmentsTblUserIDCol.setCellValueFactory(new PropertyValueFactory<>("User_ID"));
        ViewAppointmentsTblContactIDCol.setCellValueFactory(new PropertyValueFactory<>("contact_id"));

    }
}
