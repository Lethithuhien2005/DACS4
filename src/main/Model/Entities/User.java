package main.Model.Entities;

import java.time.LocalDate;

public class User {
    private int user_id;
    private String account_name;
    private String full_name;
    private String avatar;
    private String email;
    private String password;
    private String gender;
    private String address;
    private String phone;
    private LocalDate date_of_birth;
    private String status;
    private String role; // admin hoac user

    public User() {}

    public User(int user_id, String account_name, String full_name, String avatar, String email, String password, String gender, String address, String phone, LocalDate date_of_birth, String status, String role) {
        this.user_id = user_id;
        this.account_name = account_name;
        this.full_name = full_name;
        this.avatar = avatar;
        this.email = email;
        this.password = password;
        this.gender = gender;
        this.address = address;
        this.phone = phone;
        this.date_of_birth = date_of_birth;
        this.status = status;
        this.role = role;
    }

    public User(String account_name, String email, String password, String role) {
        this.account_name = account_name;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getAccount_name() {
        return account_name;
    }

    public void setAccount_name(String account_name) {
        this.account_name = account_name;
    }

    public String getFull_name() {
        return full_name;
    }

    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public LocalDate getDate_of_birth() {
        return date_of_birth;
    }

    public void setDate_of_birth(LocalDate date_of_birth) {
        this.date_of_birth = date_of_birth;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    @Override
    public String toString() {
        return "User{" +
                "user_id=" + user_id +
                ", account_name='" + account_name + '\'' +
                ", full_name='" + full_name + '\'' +
                ", avatar='" + avatar + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", gender='" + gender + '\'' +
                ", address='" + address + '\'' +
                ", phone='" + phone + '\'' +
                ", date_of_birth=" + date_of_birth +
                ", status='" + status + '\'' +
                ", role='" + role + '\'' +
                '}';
    }
}
