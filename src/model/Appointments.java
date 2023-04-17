package model;

import java.sql.Time;
import java.sql.Timestamp;
import java.time.*;

/**
 * This class creates Appointments objects.
 */
public class Appointments {

    private int appointment_ID;
    private String title;
    private String description;
    private String location;
    private String appointmentType;
    private Timestamp startDateTime;
    private Timestamp endDateTime;
    public int customer_ID;
    public int user_ID;
    public int contact_id;


    public Appointments(int appointment_ID, String title, String description, String location,  String appointmentType, Timestamp startDateTime, Timestamp endDateTime, int customer_ID, int user_ID, int contact_id) {
        this.appointment_ID = appointment_ID;
        this.title = title;
        this.description = description;
        this.location = location;
        this.appointmentType = appointmentType;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
        this.customer_ID = customer_ID;
        this.user_ID = user_ID;
        this.contact_id = contact_id;
    }





    public int getAppointment_ID() {
        return appointment_ID;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getLocation() {
        return location;
    }

    public String getAppointmentType() {
        return appointmentType;
    }

    public Timestamp getStartDateTime() {
        return startDateTime;
    }

    public Timestamp getEndDateTime() {
        return endDateTime;
    }


    public int getCustomer_ID() {
        return customer_ID;
    }

    public int getUser_ID() {
        return user_ID;
    }

    public int getContact_id() {
        return contact_id;
    }


    public void setAppointment_ID(int appointment_ID) {
        this.appointment_ID = appointment_ID;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setAppointmentType(String appointmentType) {
        this.appointmentType = appointmentType;
    }

    public void setStartDateTime(Timestamp startDateTime) {
        this.startDateTime = startDateTime;
    }

    public void setEndDateTime(Timestamp endDateTime) {
        this.endDateTime = endDateTime;
    }

    public void setCustomer_ID(int customer_ID) {
        this.customer_ID = customer_ID;
    }

    public void setUser_ID(int user_ID) {
        this.user_ID = user_ID;
    }

    public void setContact_id(int contact_id) {
        this.contact_id = contact_id;
    }
}