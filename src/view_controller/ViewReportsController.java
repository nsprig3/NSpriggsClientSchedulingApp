package view_controller;
/**
 * @author Natasha Spriggs
 */

import DAO.*;
import javafx.collections.*;
import javafx.event.*;
import javafx.fxml.*;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.*;

import java.io.IOException;
import java.net.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Month;
import java.util.*;

/**
 * This class controls the view reports screen.
 */


public class ViewReportsController implements Initializable {
    ObservableList<Contacts> allContacts = DBContacts.getAllContacts();
    ObservableList<String> listOfContacts = FXCollections.observableArrayList();
    ObservableList<Country> allCountries = DBCountry.getAllCountries();
    ObservableList<String> listOfCountries = FXCollections.observableArrayList();
    ObservableList<String> listOfMonths = FXCollections.observableArrayList();
    Stage stage;
    Parent scene;


    //buttons
    @FXML
    private Button viewReportsLogoutBtn;

    @FXML
    private Button viewReportsMainMenuBtn;

    //table views
    @FXML
    private TableColumn<Reports, Integer> reportScheduleApptIDCol;

    @FXML
    private TableColumn<Reports, Integer> reportScheduleCustomerIDCol;

    @FXML
    private TableColumn<Reports, String> reportScheduleDescriptionCol;

    @FXML
    private TableColumn<Reports, LocalDate> reportScheduleEndDateCol;

    @FXML
    private TableColumn<Reports, LocalTime> reportScheduleEndTimeCol;

    @FXML
    private TableColumn<Reports, LocalDate> reportScheduleStartDateCol;

    @FXML
    private TableColumn<Reports, LocalTime> reportScheduleStartTimeCol;

    @FXML
    private TableColumn<Reports, String> reportScheduleTitleCol;

    @FXML
    private TableColumn<Reports, String> reportScheduleTypeCol;
    @FXML
    private TableView<Reports> ReportsScheduleTableView;
    @FXML
    private TableView<Reports> ReportsTypeTableView;
    @FXML
    private TableColumn<Reports, Integer> reportTypeMonthTotalCol;

    @FXML
    private TableColumn<Reports, String> reportTypeMonthTypeCol;

    @FXML
    private TableView<Reports> reportCustomersByCountryTableView;


    @FXML
    private TableColumn<String, Integer> reportByCountryCustomerTotalCol;

    //combo boxes
    @FXML
    private ComboBox<String> ReportsContactComboBox;

    @FXML
    private ComboBox<String> ReportsCountryComboBox;

    @FXML
    private ComboBox<String> ReportsTypeComboBox;

    /**
     * This method recieves a list of reports and updates the table view to view reports by month and type.
     * @param reportList
     */

    public void updateReportsTypeTableView(ObservableList<Reports> reportList){
        ReportsTypeTableView.getItems().clear();
        ReportsTypeTableView.setItems(reportList);

    }

    /**
     * This method recieves a list of reports and updates the table to view customers by country.
     */

    public void updateCustomersByCountryTableView(ObservableList<Reports> reportList){
        reportCustomersByCountryTableView.getItems().clear();
        reportCustomersByCountryTableView.setItems(reportList);

    }
    /**
     * This method recieves a list of reports and updates the table to view appointments by contact.
     * @param reportList
     */

    public void updateReportsScheduleTableView(ObservableList<Reports> reportList){
        ReportsScheduleTableView.getItems().clear();
        ReportsScheduleTableView.setItems(reportList);

    }

    /**
     * This method retrieves contact from combo box and uses it to show a schedule of appointments for that contact.
     */



    @FXML
    void onActionDisplayByContact(ActionEvent event) {
        ObservableList<Reports> reportsViewByContact;
        String contact = ReportsContactComboBox.getSelectionModel().getSelectedItem();
        if(contact == null){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("No Selection");
            alert.setContentText("Please select a contact");
            alert.showAndWait();

        }
        else {
            int contactID = -1;
            for (Contacts c : allContacts) {
                if (c.getContactName().equals(contact)) {
                    contactID = c.getContact_ID();
                    reportsViewByContact = DBReports.viewScheduleByContact(contactID);
                    if(reportsViewByContact.isEmpty()){
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("No Appointments");
                        alert.setContentText("There are no appointments for this contact");
                        alert.showAndWait();
                    }
                    else {
                        updateReportsScheduleTableView(reportsViewByContact);
                    }

                }
            }
        }
    }

    /**
     * This method retrieves a country from the combo box and uses selected country to display the number of customers
     * in that country.
     */
    @FXML
    void onActionDisplayByCountry(ActionEvent event) {
        ObservableList<Reports> viewByCountry = null;
        String country = ReportsCountryComboBox.getSelectionModel().getSelectedItem();
        ObservableList<String> customerTotalList;
        if (country == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("No Selection");
            alert.setContentText("Please select country");
            alert.showAndWait();

        } else {

            int countryID = -1;
            for (Country c : allCountries) {
                if (c.getCountry().equals(country)) {
                    countryID = c.getCountry_id();
                    viewByCountry = DBReports.viewCustomersByCountry(countryID);
                    updateCustomersByCountryTableView(viewByCountry);

                }
            }
            }
        }




    /**
     * This method retrieves a month from the combo box and uses it to count the number of appointments for the selected
     * month.
     * @param event
     */

    @FXML
    void onActionDisplayByMonth(ActionEvent event) {
        ObservableList<Reports> reportsViewByTypeMonth = FXCollections.observableArrayList();
        String monthString = ReportsTypeComboBox.getSelectionModel().getSelectedItem();
        if(monthString == null){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("No Selection");
            alert.setContentText("Please select a month");
            alert.showAndWait();
        }
        else {
            int monthInt = -1;

            for (Month m : Month.values()) {
                if (monthString.equals(m.toString())) {
                    monthInt = m.getValue();
                    reportsViewByTypeMonth = DBReports.viewByTypeMonth(monthInt);
                    updateReportsTypeTableView(reportsViewByTypeMonth);
                    break;
                }

            }
        }

          if(reportsViewByTypeMonth.isEmpty()){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("No Appointments");
                alert.setContentText("There are no appointments for month " + monthString);
                alert.showAndWait();
            }

            }

    @FXML
    void onActionDisplayMainMenu(ActionEvent event) throws IOException {
        stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("MainMenu.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }

    @FXML
    void onActionExit(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Do you want to exit?");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            System.exit(0);
        }

    }

    /**
     * override of initialize includes lambdas for filling the contacts and country combo boxes. These lambdas
     * condense the code and makes it more readable
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //lambdas to fill contacts and countries lists
        allContacts.forEach(contacts -> listOfContacts.add(contacts.getContactName()));
        allCountries.forEach(country -> listOfCountries.add(country.getCountry()));


        for(Month m: Month.values()){
            listOfMonths.add(m.toString());
        }


        ReportsContactComboBox.setItems(listOfContacts);
        ReportsTypeComboBox.setItems(listOfMonths);
        ReportsCountryComboBox.setItems(listOfCountries);



        ReportsTypeTableView.getItems().clear();
        reportTypeMonthTypeCol.setCellValueFactory(new PropertyValueFactory<>("appointmentType"));
        reportTypeMonthTotalCol.setCellValueFactory(new PropertyValueFactory<>("appointmentTotal"));


        ReportsScheduleTableView.getItems().clear();
        reportScheduleApptIDCol.setCellValueFactory(new PropertyValueFactory<>("appointmentID"));
        reportScheduleTitleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
        reportScheduleTypeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
        reportScheduleDescriptionCol.setCellValueFactory(new PropertyValueFactory<>("description"));
        reportScheduleStartDateCol.setCellValueFactory(new PropertyValueFactory<>("startDate"));
        reportScheduleStartTimeCol.setCellValueFactory(new PropertyValueFactory<>("startTime"));
        reportScheduleEndDateCol.setCellValueFactory(new PropertyValueFactory<>("endDate"));
        reportScheduleEndTimeCol.setCellValueFactory(new PropertyValueFactory<>("endTime"));
        reportScheduleCustomerIDCol.setCellValueFactory(new PropertyValueFactory<>("customerID"));

        reportCustomersByCountryTableView.getItems().clear();
        reportByCountryCustomerTotalCol.setCellValueFactory(new PropertyValueFactory<>("customerTotal"));

    }
}

