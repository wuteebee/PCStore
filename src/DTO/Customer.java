package DTO;

import java.time.LocalDate;

public class Customer {
    private String id;
    private String name;
    private String phoneNumber; // Số điện thoại
    private String email;       // Email
    private LocalDate dateOfJoining;  // Ngày vào làm
    private boolean trangThai;  // Trạng thái (true: đang hoạt động, false: không hoạt động)

    public Customer(){

    }
    public Customer(String id, String name, String phoneNumber, String email,LocalDate dateOfJoining ,boolean trangThai) {
        this.id = id;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.dateOfJoining = dateOfJoining;
        this.trangThai = trangThai;

    }

    public Customer(String name, String phoneNumber, String email,LocalDate dateOfJoining , boolean trangThai) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.trangThai = trangThai;
        this.dateOfJoining = dateOfJoining;
    }

    public Customer(String name, String phoneNumber, String email,LocalDate dateOfJoining) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.dateOfJoining = dateOfJoining;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public void setId(String id) {
        this.id = id;
    }
    public void setName(String name) {
        this.name = name;
    }public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
    public void setTrangThai(boolean trangThai) {
        this.trangThai = trangThai;
    }
    public String getEmail() {
        return email;
    }
    public String getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public String getPhoneNumber() {
        return phoneNumber;
    }
    public boolean getTrangThai() {
        return trangThai;
    }
    public void setDateOfJoining(LocalDate dateOfJoining) {
        this.dateOfJoining = dateOfJoining;
    }
    public LocalDate getDateOfJoining() {
        return dateOfJoining;
    }

}
