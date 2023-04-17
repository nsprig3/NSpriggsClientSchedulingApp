package DAO;
/**
 * @author Natasha Spriggs
 */

import helper.JDBC;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.*;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
/**
 * This class queries the Countries table of the database and returns all countries.
 */

public class DBCountry {
    public static ObservableList<Country> getAllCountries() {
        ObservableList<Country> countryList = FXCollections.observableArrayList();
        try{
            String sql = "SELECT * from COUNTRIES";

            PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                int countryID = rs.getInt("Country_ID");
                String countryName = rs.getString("Country");

                Country C = new Country(countryID, countryName);
                countryList.add(C);
            }

        }catch (SQLException throwables) {
            throwables.printStackTrace();
        } ;

        return countryList;
    }
}
