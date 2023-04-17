package model;
/**
 * This class creates Country objects.
 */

public class Country {
    private int country_id;
    private String countryName;

    public Country(int country_id) {
        this.country_id = country_id;
    }

    public Country(int country_id, String countryName) {
        this.country_id = country_id;
        this.countryName = countryName;
    }

    public void setCountry_id(int country_id) {
        this.country_id = country_id;
    }

    public int getCountry_id() {
        return country_id;
    }

    public void setCountry(String countryName) {
       this.countryName = countryName;
    }


    public String getCountry() {
        return countryName;
    }

   }
