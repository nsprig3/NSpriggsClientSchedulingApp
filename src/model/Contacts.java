package model;

public class Contacts {
    public int contact_ID;
    public String contactName;
    public String contactEmail;

    @Override
    public String toString(){
        return (this.contact_ID + " " + this.contactName);
    }


    public Contacts(int contact_ID, String contactName, String contactEmail) {
        this.contact_ID = contact_ID;
        this.contactName = contactName;
        this.contactEmail = contactEmail;
    }

    public int getContact_ID() {
        return contact_ID;
    }

    public String getContactName() {
        return contactName;
    }

    public String getContactEmail() {
        return contactEmail;
    }

    public void setContact_ID(int contact_ID) {
        this.contact_ID = contact_ID;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public void setContactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
    }
}

