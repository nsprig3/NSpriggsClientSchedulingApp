package model;
/**
 * This class creates FLDivision objects.
 */

public class FLDivision {
    private int division_ID;
    private String divisionName;
    public int country_ID;

    public FLDivision(int division_ID, String divisionName, int country_ID){
        this.division_ID = division_ID;
        this.divisionName = divisionName;
        this.country_ID = country_ID;
    }

    public void setDivision_ID(int division_ID) {
        this.division_ID = division_ID;
    }

    public void setDivisionName(String divisionName) {
        this.divisionName = divisionName;
    }

    public void setCountry_ID(int country_ID) {
        this.country_ID = country_ID;
    }

    public int getDivision_ID() {
        return division_ID;
    }

    public String getDivisionName() {
        return divisionName;
    }

    public int getCountry_ID() {
        return country_ID;
    }
}
