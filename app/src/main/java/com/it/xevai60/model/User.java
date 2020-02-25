package com.it.xevai60.model;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class User {

    @SerializedName("UserID")
    @Expose
    private String userID;
    @SerializedName("PassWord")
    @Expose
    private String passWord;

    /**
     * No args constructor for use in serialization
     *
     */
    public User() {
    }

    /**
     *
     * @param passWord
     * @param userID
     */
    public User(String userID, String passWord) {
        super();
        this.userID = userID;
        this.passWord = passWord;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getPassWord() {
        return passWord;
    }

    public void setPassWord(String passWord) {
        this.passWord = passWord;
    }

}