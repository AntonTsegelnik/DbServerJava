package com.rw.Model;

public class Passenger extends ClientRequest{
    private int PassId;

    private String FirstName;
    private String LastName;
    private String Country;
    private String PassportNum;
    private String Username;
    public Passenger(){}
    public Passenger( String firstName, String lastName, String country, String passportNum, String username) {

        FirstName = firstName;
        LastName = lastName;
        Country = country;
        PassportNum = passportNum;
        Username = username;
    }
    public Passenger( String firstName, String lastName, String country, String passportNum, int PassId, String username) {

        FirstName = firstName;
        LastName = lastName;
        Country = country;
        PassportNum = passportNum;
        this.PassId = PassId;
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

    public String getLastName() {
        return LastName;
    }

    public void setLastName(String lastName) {
        LastName = lastName;
    }
}
