package com.example.generalstore.Modules;

public class Seller {

    String profilePic;
    String userName;
    String mail;
    String password;
    String userid;

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    String phoneNumber;
    String firstName;
    String lastName;


    public int getTotal_earn() {
        return total_earn;
    }

    public void setTotal_earn(int total_earn) {
        this.total_earn = total_earn;
    }

    int total_earn;


    public Seller(String phoneNumber, String firstName, String lastName, int total_earn, String profilePic, String userName, String mail, String password, String userid, String lastMessage) {
        this.profilePic = profilePic;
        this.phoneNumber = phoneNumber;
        this.userName = userName;
        this.total_earn = total_earn;
        this.mail = mail;
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
        this.userid = userid;
    }


    public Seller() {
    }

    public Seller(String userName, String mail, String password) {
        this.userName = userName;
        this.mail = mail;
        this.password = password;
    }

    public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

}
