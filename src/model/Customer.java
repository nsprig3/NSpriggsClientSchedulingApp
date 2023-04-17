package model;

import javafx.fxml.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.stage.*;

public class Customer {
    private int customer_ID;
    private String customer_name;
    private String address;
    private String postalCode;
    private String phoneNumber;



    private String country;
    private String flDivision;

    public int division_ID;

    public Customer(int customer_ID, String customer_name, String address, String postalCode, String phoneNumber, int division_ID) {
        this.customer_ID = customer_ID;
        this.customer_name = customer_name;
        this.address = address;
        this.postalCode = postalCode;
        this.phoneNumber = phoneNumber;
        this.division_ID = division_ID;
    }

    public Customer(int customer_ID, String customer_name, String address, String postalCode, String phoneNumber, String country, String flDivision, int division_ID) {
        this.customer_ID = customer_ID;
        this.customer_name = customer_name;
        this.address = address;
        this.postalCode = postalCode;
        this.phoneNumber = phoneNumber;
        this.country = country;
        this.flDivision = flDivision;
        this.division_ID = division_ID;
    }



    public int getCustomer_ID() {
        return customer_ID;
    }

    public String getCustomer_name() {
        return customer_name;
    }

    public String getAddress() {
        return address;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public int getDivision_ID() {
        return division_ID;
    }

    public void setCustomer_ID(int customer_ID) {
        this.customer_ID = customer_ID;
    }

    public void setCustomer_name(String customer_name) {
        this.customer_name = customer_name;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setDivision_ID(int division_ID) {
        this.division_ID = division_ID;
    }
    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getFlDivision() {
        return flDivision;
    }

    public void setFlDivision(String flDivision) {
        this.flDivision = flDivision;
    }
}

