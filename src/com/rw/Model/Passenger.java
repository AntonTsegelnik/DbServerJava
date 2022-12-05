package com.rw.Model;

public class Passenger {
    private int PassId;

    private String FirstName;
    private String Country;
    private String PassportNum;
    private String Username;

    public Passenger(int passId, String firstName, String lastName, String country, String passportNum, String username) {
        PassId = passId;
        FirstName = firstName;
        Country = country;
        PassportNum = passportNum;
        Username = username;
    }

    public int getPassId() {
        return PassId;
    }

    public void setPassId(int passId) {
        PassId = passId;
    }

    public String getFirstName() {
        return FirstName;
    }

    public void setFirstName(String firstName) {
        FirstName = firstName;
    }

    public String getCountry() {
        return Country;
    }

    public void setCountry(String country) {
        Country = country;
    }

    public String getPassportNum() {
        return PassportNum;
    }

    public void setPassportNum(String passportNum) {
        PassportNum = passportNum;
    }

    public String getUsername() {
        return Username;
    }

    public void setUsername(String username) {
        Username = username;
    }
}
