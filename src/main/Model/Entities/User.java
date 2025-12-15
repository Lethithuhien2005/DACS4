package main.Model.Entities;

import org.bson.types.ObjectId;

import java.time.LocalDate;
import java.util.Date;

public class User {
    private int user_id;
    private String accountName;
    private String fullName;
    private String avatar;
    private String email;
    private String password;
    private String role; // admin hoac user
    private String gender; // male - female
    private String phone;
    private String address;
    private String status;
    private LocalDate dob;
    private Date updatedAt;
    private Date createdAt;
    private String username; // Thêm thuộc tính này


    private ObjectId userId;  // hoặc _id
    public String getUserIdHex() {
        return userId == null ? null : userId.toHexString();
    }


    public User() {
    }

    public User(String username,String fullName, String email, String password, String role) {
        this.username = username;
        this.fullName = fullName;
        this.password = password;
    }
    public User(String fullName, String email, String password, String role) {
        this.fullName = fullName;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    public User(String accountName, String fullName, String email, String password, String role, String gender, String phone, String address, LocalDate dob) {
        this.accountName = accountName;
        this.fullName = fullName;
        this.email = email;
        this.password = password;
        this.role = role;
        this.gender = gender;
        this.phone = phone;
        this.address = address;
        this.dob = dob;
    }

    public ObjectId getUserId() {
        return userId;
    }

    public void setUserId(ObjectId userId) {
        this.userId = userId;
    }


    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public User(int user_id, String accountName, String fullName, String avatar, String email, String password, String role, String gender, String phone, String address, String status, LocalDate dob) {
        this.user_id = user_id;
        this.accountName = accountName;
        this.fullName = fullName;
        this.avatar = avatar;
        this.email = email;
        this.password = password;
        this.role = role;
        this.gender = gender;
        this.phone = phone;
        this.address = address;
        this.status = status;
        this.dob = dob;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
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

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }
    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }


    public Date getCreatedAt() { return createdAt; }
    public void setCreatedAt(Date createdAt) { this.createdAt = createdAt; }

    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", role='" + role + '\'' +
                '}';
    }
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDate getDob() {
        return dob;
    }

    public void setDob(LocalDate dob) {
        this.dob = dob;
    }
}
