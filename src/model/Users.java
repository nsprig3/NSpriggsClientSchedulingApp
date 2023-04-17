package model;

import java.sql.*;

public class Users  {
    public int user_ID;
    public String userName;
    public String userPassword;

    public Timestamp loginTime;


    public Users(int user_ID, String userName, String userPassword) {
        this.user_ID = user_ID;
        this.userName = userName;
        this.userPassword = userPassword;
    }

    public Users(String userName, String userPassword){
        this.userName = userName;
        this.userPassword   = userPassword;
    }

    public Users(int user_ID, String userName, String userPassword, Timestamp loginTime){
        this.user_ID = user_ID;
        this.userName = userName;
        this.userPassword = userPassword;
        this.loginTime = loginTime;
    }


    public int getUser_ID() {
        return user_ID;
    }

    public String getUserName() {
        return userName;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUser_ID(int user_ID) {
        this.user_ID = user_ID;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public Timestamp getLoginTime() {
        return loginTime;
    }

    public void setLoginTime(Timestamp loginTime) {
        this.loginTime = loginTime;
    }
}
