package view_controller;
/**
 * @author Natasha Spriggs
 */

import DAO.*;
import javafx.collections.*;
import javafx.event.*;
import javafx.fxml.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.stage.*;
import model.*;

import java.io.IOException;
import java.net.*;
import java.sql.*;
import java.util.*;

/**
 * This class controls the Update Customer screen.
 */

public class UpdateCustomerController implements Initializable {
    Stage stage;
    Parent scene;
    ObservableList<Country> allCountries = DBCountry.getAllCountries();
   public static ObservableList<String> listOfCountries = FXCollections.observableArrayList();
    ObservableList<FLDivision> allDivisions = DBFLDivision.getAllFLDivisions();

    /**
     * This method separates all divisions by country and then sets the division combo box based on user selection from
     * country combo box.
     * @param countryName String, the name of the country to use to set division combo box data.
     * @throws SQLException
     */
    public void filterDivisionList (String countryName) throws SQLException {

        ObservableList<String> divisionUS = FXCollections.observableArrayList();
        ObservableList<String> divisionUK = FXCollections.observableArrayList();
        ObservableList<String> divisionCanada = FXCollections.observableArrayList();

        //separate divisions by country
        for (FLDivision division : allDivisions) {
            int countryID = division.getCountry_ID();
            if (countryID == 1) {
                divisionUS.add(division.getDivisionName());
            } else if (countryID == 2) {
                divisionUK.add(division.getDivisionName());
            } else if (countryID == 3) {
                divisionCanada.add(division.getDivisionName());
            }
        }
        if (countryName.equals("U.S")){
            UpdateCustomerDivisionCBox.setItems(divisionUS);
            UpdateCustomerDivisionCBox.getSelectionModel().selectFirst();

        }
        else if (countryName.equals("UK")){
            UpdateCustomerDivisionCBox.setItems(divisionUK);
            UpdateCustomerDivisionCBox.getSelectionModel().selectFirst();

        }
        else if (countryName.equals("Canada")){
            UpdateCustomerDivisionCBox.setItems(divisionCanada);
            UpdateCustomerDivisionCBox.getSelectionModel().selectFirst();

        }
    }
    /**
     * This method separates all divisions by country and then sets the division combo box based on user selection from
     * country combo box.
     * @param countryID int, the ID of the country to use to set division combo box data.
     */
    public void filterDivisionList(int countryID){
        ObservableList<String> divisionUS = FXCollections.observableArrayList();
        ObservableList<String> divisionUK = FXCollections.observableArrayList();
        ObservableList<String> divisionCanada = FXCollections.observableArrayList();


        //separate divisions by country
        for (FLDivision division : allDivisions) {
            if (division.getCountry_ID() == 1) {
                divisionUS.add(division.getDivisionName());

            } else if (division.getCountry_ID() == 2) {
                divisionUK.add(division.getDivisionName());
            } else if (division.getCountry_ID() == 3) {
                divisionCanada.add(division.getDivisionName());
            }
        }
        if (countryID == 1) {
            UpdateCustomerCountryComboBox.setValue("U.S");
            UpdateCustomerDivisionCBox.getItems().clear();
            UpdateCustomerDivisionCBox.setItems(divisionUS);
        }
        else if (countryID == 2){
            UpdateCustomerCountryComboBox.setValue("UK");
            UpdateCustomerDivisionCBox.getItems().clear();
            UpdateCustomerDivisionCBox.setItems(divisionUK);
        }
        else if (countryID == 3){
            UpdateCustomerCountryComboBox.setValue("Canada");
            UpdateCustomerDivisionCBox.getItems().clear();
            UpdateCustomerDivisionCBox.setItems(divisionCanada);
        }

    }
    /**
     * This method receives customer data to update and checks each box for completion. If any of the boxes are empty or have
     * invalid data, the user will receive a notification detailing the missing data.
     * @param customerName String customerName to validate.
     * @param address String address to validate.
     * @param postalCode String postalCode to validate.
     * @param phoneNumber String phoneNumber to validate.
     * @param country String country to validate.
     * @param division String division to validate.
     * @return boolean returns true if all data is valid. Otherwise, an alert is generated detailing errors for user to correct.
     * Will return false if data is invalid.
     */

    public boolean validateCustomer(String customerName,  String address, String postalCode, String phoneNumber, String country, String division) {
        boolean isValid;
        StringBuilder missingData = new StringBuilder();
        if (customerName.isEmpty() ) {
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
        if(country== null){
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

    //labels
    @FXML
    private Label UpdateCustomerAddressLbl;
    @FXML
    private Label UpdateCustomerCustomerIDLbl;
    @FXML
    private Label UpdateCustomerFirstLevelLbl;
    @FXML
    private Label UpdateCustomerNameLbl;
    @FXML
    private Label UpdateCustomerCountryLbl;
    @FXML
    private Label UpdateCustomerPhoneLbl;
    @FXML
    private Label UpdateCustomerPostalLbl;

    //buttons
    @FXML
    private Button UpdateCustomerCancelBtn;
    @FXML
    private Button UpdateCustomerSaveBtn;

    //combo boxes
    @FXML
    private ComboBox<String> UpdateCustomerCountryComboBox;
    @FXML
    private ComboBox<String> UpdateCustomerDivisionCBox;

    //text fields
    @FXML
    private TextField UpdateCustomerAddressTxt;
    @FXML
    private TextField UpdateCustomerIdTxt;
    @FXML
    private TextField UpdateCustomerNameTxt;
    @FXML
    private TextField UpdateCustomerPhoneTxt;
    @FXML
    private TextField UpdateCustomerPostalTxt;

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
     * This method retrieves the country String from the country combo box and sends it to a
     * method that will filter the division combo box based on the country
     */
    @FXML
    void onActionUpdateCountryDivisionsComboBox(ActionEvent event) throws SQLException {
        String country = UpdateCustomerCountryComboBox.getValue();
        filterDivisionList(country);

    }

    /**
     * This method receives user input from text boxes and combo boxes, calls methods to validate and then creates a new
     * customer in the database if all data is valid.
     */

    @FXML
    void OnActionSaveDisplayMenu(ActionEvent event) throws IOException {
      try{
        int customerID = Integer.parseInt(UpdateCustomerIdTxt.getText());
        String customerName = UpdateCustomerNameTxt.getText();
        String address = UpdateCustomerAddressTxt.getText();
        String postalCode = UpdateCustomerPostalTxt.getText();
        String phoneNumber = UpdateCustomerPhoneTxt.getText();
        String country = UpdateCustomerCountryComboBox.getValue();
        String division = UpdateCustomerDivisionCBox.getValue();
        int divID = -1;
        for(FLDivision d : allDivisions){
            if((d.getDivisionName().equals(division))){
                divID = d.getDivision_ID();
            }
        }


        if(validateCustomer(customerName, address, postalCode,  phoneNumber, country, division)){
            Customer customer = new Customer(customerID, customerName, address, postalCode,  phoneNumber, country, division, divID);

            DBCustomer.updateCustomer(customer);

            this.stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            this.scene = (Parent) FXMLLoader.load(this.getClass().getResource("ViewCustomers.fxml"));
            this.stage.setScene(new Scene(this.scene));
            this.stage.show();
        }


    }catch(IOException e) {
          e.printStackTrace();
      }
    }

    /**
     * This method retrieves the data from the customer the user selected to edit and uses
     * it to populate the boxes with the existing customer data.
     * @param customer
     */

    public void sendCustomer (Customer customer){
        UpdateCustomerIdTxt.setText(String.valueOf(customer.getCustomer_ID()));
        UpdateCustomerNameTxt.setText(customer.getCustomer_name());
        UpdateCustomerAddressTxt.setText(customer.getAddress());
        UpdateCustomerPostalTxt.setText(customer.getPostalCode());
        UpdateCustomerPhoneTxt.setText(customer.getPhoneNumber());
        int divisionID = customer.getDivision_ID();
        for (Country C: allCountries){
            listOfCountries.add(C.getCountry());
        }
        UpdateCustomerCountryComboBox.getItems().clear();
        UpdateCustomerCountryComboBox.setItems(listOfCountries);

        for(FLDivision div : allDivisions) {
            if(div.getDivision_ID() == divisionID){
                int countryID = div.getCountry_ID();
                filterDivisionList(countryID);
                UpdateCustomerDivisionCBox.setValue(div.getDivisionName());
            }
        }


    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {


    }
}
