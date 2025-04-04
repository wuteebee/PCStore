package DTO;

import java.time.LocalDate;

public class Employee {
    private String id;
    private String name;
    private String position;      // Chức vụ
    private String phoneNumber;   // Số điện thoại
    private String email;         // Email
    private LocalDate dateOfJoining;  // Ngày vào làm
    private double Luong;       // Địa chỉ nhà
    private boolean trangThai;

    public Employee(String id, String name, String position, 
                    String phoneNumber, String email, LocalDate dateOfJoining,double Luong) {
        this.id = id;
        this.name = name;
        this.position = position;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.dateOfJoining = dateOfJoining;
        this.Luong=Luong;

    }
    public Employee(String id, String name, String position, 
    String phoneNumber, String email, LocalDate dateOfJoining,double Luong,boolean trangThai) {
        this.id = id;
        this.name = name;
        this.position = position;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.dateOfJoining = dateOfJoining;
        this.Luong=Luong;
        this.trangThai=trangThai;
            }
    // Constructor không có ID (dùng khi thêm nhân viên mới)   

        public Employee( String name, String position, 
        String phoneNumber, String email, LocalDate dateOfJoining,double Luong) {

    this.name = name;
    this.position = position;

    this.phoneNumber = phoneNumber;
    this.email = email;
    this.dateOfJoining = dateOfJoining;
    this.Luong=Luong;

}
    // Getters & Setters
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    
    public String getPosition() {
        return position;
    }
    public void setPosition(String position) {
        this.position = position;
    }
    

    
    public String getPhoneNumber() {
        return phoneNumber;
    }
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
    
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    
    public LocalDate getDateOfJoining() {
        return dateOfJoining;
    }
    public void setDateOfJoining(LocalDate dateOfJoining) {
        this.dateOfJoining = dateOfJoining;
    }
    
    public Double getLuong() {
        return Luong;
    }
    public void setLuong(Double Luong) {
        this.Luong = Luong;
    }
    public boolean getTrangThai() {
        return trangThai;
    }
    public void setTrangThai(boolean trangThai) {
        this.trangThai = trangThai;
    }
}
