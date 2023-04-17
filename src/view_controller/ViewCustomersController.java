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
import javafx.scene.control.cell.*;
import javafx.stage.*;
import model.*;

import java.io.*;
import java.net.*;
import java.util.*;

/**
 * This class controls the View Customers screen.
 */

public class ViewCustomersController implements Initializable {
    Stage stage;
    Parent scene;

    //Buttons
    @FXML
    private Button viewCustomersAddCustomerBtn;
    @FXML
    private Button viewCustomersEditCustomersBtn;
    @FXML
    private Button viewCustomersMainMenuButton;
    @FXML
    private Button viewCustomersDeleteCustomerBtn;
    @FXML
    private Button viewCustomersViewAppointmentsBtn;

    //TableView
    @FXML
    private TableView<Customer> viewCustomersTableView;
    @FXML
    private TableColumn<Customer, Integer> viewCustomersIDCol;
    @FXML
    private TableColumn<Customer, String> viewCustomersNameCol;
    @FXML
    private TableColumn<Customer, String> viewCustomersAddressCol;
    @FXML
    private TableColumn<Customer, String > viewCustomersCountryCol;
    @FXML
    private TableColumn<Customer, String> viewCustomersDivisionCol;
    @FXML
    private TableColumn<Customer, String> viewCustomersPostalCodeCol;

    @FXML
    private TableColumn<Customer, String> viewCustomersPhoneCol;

    /**
     * This method recieves a list of customers and updates the selected tableview.
     * @param customers
     */

    public void updateCustomerTableView(ObservableList<Customer> customers){
        viewCustomersTableView.getItems().clear();
        viewCustomersTableView.setItems(customers);
    }

//Handlers
    @FXML
    void onActionAddCustomer(ActionEvent event) throws IOException {
        stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("AddCustomer.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }

    @FXML
    void onActionDeleteCustomer(ActionEvent event) throws IOException {
        ObservableList<Customer> updatedCustomerList;
        Customer c = viewCustomersTableView.getSelectionModel().getSelectedItem();
        if(c == null){
            //user did not select item to delete
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("No Selection");
            alert.setContentText("Please select a customer to delete");
            alert.showAndWait();


        }
        else {
            int customerToDelete = c.getCustomer_ID();
            ObservableList<Appointments> customerAppointments = DBAppointments.customerAppointmentChecker(customerToDelete);
            if (customerAppointments.isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Delete Customer?");
                Optional<ButtonType> result = alert.showAndWait();
                if (result.isPresent() && result.get() == ButtonType.OK) {
                    Alert deletedCustomer = new Alert(Alert.AlertType.WARNING);
                    deletedCustomer.setTitle("Customer Deleted");
                    deletedCustomer.setContentText("Customer #" + customerToDelete + " successfully deleted.");
                    deletedCustomer.showAndWait();

                    updatedCustomerList = DBCustomer.deleteCustomer(c);
                    updateCustomerTableView(updatedCustomerList);
                }

            }
            else{
                //customer has appointments scheduled
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Existing Appointment");
                alert.setContentText("Customer cannot be deleted until existing appointments have been completed or cancelled.");
                alert.showAndWait();
            }
        }


    }
    @FXML
    void onActionViewAppointments(ActionEvent event) throws IOException {
        stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("ViewAppointments.fxml"));
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
    void onActionUpdateCustomer(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("UpdateCustomer.fxml"));
        loader.load();
        UpdateCustomerController UCController = loader.getController();

        Customer selectedCustomer = viewCustomersTableView.getSelectionModel().getSelectedItem();
        if(selectedCustomer == null){
            //user did not select item to update
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("No Selection");
            alert.setContentText("Please select a customer to edit.");
            alert.showAndWait();
        }
        else {

            UCController.sendCustomer(selectedCustomer);
            stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            scene = loader.getRoot();
            stage.setScene(new Scene(scene));
            stage.show();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        updateCustomerTableView(DBCustomer.getAllCustomers());
        viewCustomersIDCol.setCellValueFactory(new PropertyValueFactory<>("customer_ID"));
        viewCustomersNameCol.setCellValueFactory(new PropertyValueFactory<>("customer_name"));
        viewCustomersAddressCol.setCellValueFactory(new PropertyValueFactory<>("address"));
        viewCustomersPostalCodeCol.setCellValueFactory(new PropertyValueFactory<>("postalCode"));
        viewCustomersPhoneCol.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
        viewCustomersDivisionCol.setCellValueFactory(new PropertyValueFactory<>("division_ID"));
    }
}
