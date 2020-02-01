package com.mobilecomputing.one_sec.model;

public class LoginInfo {
    private int _id;
    private String name;
    private String password;
    private String email;
    private String mobNum;
    private String dob;

    public LoginInfo() {

    }

    public LoginInfo(String name, String email, String password, String mobNum, String dob) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.mobNum = mobNum;
        this.dob = dob;
    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobNum() {
        return mobNum;
    }

    public void setMobNum(String mobNum) {
        this.mobNum = mobNum;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }
}