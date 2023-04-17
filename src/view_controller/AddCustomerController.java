package view_controller;
import DAO.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.*;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.*;

/**
 * @author Natasha Spriggs
 */

/**
 * This class controls the Add Customer screen.
 */
public class AddCustomerController implements Initializable {
    Stage stage;
    Parent scene;
    ObservableList<Country> allCountries = DBCountry.getAllCountries();
    ObservableList<String> listOfCountries = FXCollections.observableArrayList();
    ObservableList<FLDivision> allDivisions = DBFLDivision.getAllFLDivisions();

    /**
     * This method receives new customer data and checks each box for completion. If any of the boxes are empty or have
     *  invalid data, the user will receive a notification detailing the missing data.
     * @param customerName String customerName to validate.
     * @param address String address to validate.
     * @param postalCode String postalCode to validate.
     * @param phoneNumber String phoneNumber to validate.
     * @param country String country to validate.
     * @param division String division to validate.
     * @return boolean returns true if all data is valid. Otherwise an alert is generated detailing errors for user to correct.
     * Will return false if data is invalid.
     */

    public boolean validateCustomer(String customerName, String address, String postalCode, String phoneNumber, String country, String division) {
        boolean isValid;
        StringBuilder missingData = new StringBuilder();
        if (customerName.isEmpty()) {
            missingData.append("Please enter valid customer name.\n");
        }
        if(address.isEmpty()){
            missingData.append("Please enter valid customer address.\n");
        }
        if(postalCode.isEmpty()){
            missingData.append("Please enter valid postal code.\n");
        }
        if(phoneNumber.isEmpty()){
            missingData.append("Please enter valid phone number.\n");
        }
        if(country == null){
            missingData.append("Please select customer country.\n");
        }
        if(division == null){
            missingData.append("Please select customer division.\n");

        }

        if (missingData.length() > 0) {
            isValid = false;
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Incomplete Customer Data");
            alert.setContentText("Please correct the following: " + missingData);
            alert.showAndWait();
        }
        else{
            isValid = true;
        }
        return isValid;
    }

    /**
     * This method separates all divisions by country and then sets the division combo box based on user selection from
     * country combo box.
     * @param countryName String, the name of the country to use to set division combo box data.
     * @throws SQLException
     */

    public void filterDivisionList (String countryName) throws SQLException {
        ObservableList<FLDivision> allDivisions = DBFLDivision.getAllFLDivisions();
        ObservableList<String> divisionUS = FXCollections.observableArrayList();
        ObservableList<String> divisionUK = FXCollections.observableArrayList();
        ObservableList<String> divisionCanada = FXCollections.observableArrayList();

        //separate divisions by country
        for(FLDivision division : allDivisions){
            int countryID = division.getCountry_ID();
            if (countryID == 1){
                divisionUS.add(division.getDivisionName());
            }
            else if (countryID == 2){
                divisionUK.add(division.getDivisionName());
            }
            else if (countryID == 3){
                divisionCanada.add(division.getDivisionName());
            }
        }

        //populate divisions based on country
        if (countryName.equals("U.S")){
            AddCustomerDivisionComboBox.setItems(divisionUS);
        }
        else if (countryName.equals("UK")){
            AddCustomerDivisionComboBox.setItems(divisionUK);
        }
        else if (countryName.equals("Canada")){
            AddCustomerDivisionComboBox.setItems(divisionCanada);
        }
    }

    //textfields
    @FXML
    private TextField AddCustomerIdTxt;
    @FXML
    private TextField AddCustomerNameTxt;

    @FXML
    private TextField AddCustomerAddressTxt;
    @FXML
    private TextField AddCustomerPostalTxt;
    @FXML
    private TextField AddCustomerPhoneTxt;
    //combo boxes
    @FXML
    private ComboBox<String> AddCustomerDivisionComboBox;

    @FXML
    private ComboBox<String> AddCustomerCountryComboBox;

    //buttons
    @FXML
    private Button AddCustomerCancelBtn;

    @FXML
    private Button AddCustomerSaveBtn;

    /**
     * This method collects the country String from country combo box and calls the filterDivisionList method to filter
     * the divisions and set the division combo box based on user selection.
     * @param event
     * @throws SQLException
     */
    @FXML
    void onActionFilterCountries(ActionEvent event) throws SQLException {
       String newCountry = AddCustomerCountryComboBox.getSelectionModel().getSelectedItem();
       filterDivisionList(newCountry);

    }

    @FXML
    void OnActionDisplayMenu(ActionEvent event) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Cancel new customer?");
        Optional<ButtonType> result  = alert.showAndWait();
        if(result.isPresent() && result.get() == ButtonType.OK) {

            this.stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            this.scene = (Parent) FXMLLoader.load(this.getClass().getResource("ViewCustomers.fxml"));
            this.stage.setScene(new Scene(this.scene));
            this.stage.show();
        }

    }

    /**
     * This method receives user input from text boxes and combo boxes, calls methods to validate and then creates a new
     * customer in the database if all data is valid.
     */

    @FXML
    void OnActionSaveDisplayMenu(ActionEvent event) throws IOException {
        try {
            int customerID = -1;
            String customerName = AddCustomerNameTxt.getText();
            String address = AddCustomerAddressTxt.getText();
            String postalCode = AddCustomerPostalTxt.getText();
            String phoneNumber = AddCustomerPhoneTxt.getText();
            String country = AddCustomerCountryComboBox.getValue();
            String division = AddCustomerDivisionComboBox.getValue();
            int divID = -1;
            if (validateCustomer(customerName, address, postalCode, phoneNumber, country, division)) {

            for (FLDivision d : allDivisions) {
                if ((d.getDivisionName().equals(division))) {
                    divID = d.getDivision_ID();
                }
            }
            Customer c = new Customer(customerID, customerName, address, postalCode, phoneNumber, country, division, divID);

                DBCustomer.addCustomer(c);

                this.stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
                this.scene = (Parent) FXMLLoader.load(this.getClass().getResource("ViewCustomers.fxml"));
                this.stage.setScene(new Scene(this.scene));
                this.stage.show();
            }
        } catch(IOException e) {
            e.printStackTrace();

       }

    }

    /**
     * This override of initialize includes the lambda to set the values for the country combo box. This lambda
     * condenses the code and makes it more readable
     */

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if (!(allCountries == null)) {
            //lambda to set values in list of Countries
            allCountries.forEach(contacts -> listOfCountries.add(contacts.getCountry()));

            AddCustomerCountryComboBox.setItems(listOfCountries);
            AddCustomerCountryComboBox.setEditable(false);
        }
    }
}

