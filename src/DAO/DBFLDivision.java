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
 * This class queries the First_Level_Divisions table of the database and returns all first level divisions.
 */


public class DBFLDivision {
    public static ObservableList<FLDivision> getAllFLDivisions() {
        ObservableList<FLDivision> FLDivisionList = FXCollections.observableArrayList();
       try {
           String sql = "SELECT * FROM first_level_divisions";

           PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);
           ResultSet rs = ps.executeQuery();

           while (rs.next()) {
               int division_id = rs.getInt("Division_ID");
               String division = rs.getString("Division");
               int country_id = rs.getInt("Country_ID");


               FLDivision fld = new FLDivision(division_id, division, country_id);
               FLDivisionList.add(fld);

           }
       }catch (SQLException throwables) {
           throwables.printStackTrace();
       }


        return FLDivisionList;
    }



}
