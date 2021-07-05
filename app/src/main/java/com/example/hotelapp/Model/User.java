package com.example.hotelapp.Model;

import java.io.Serializable;

public class User implements Serializable {
    private int ID;
    private String HoTen;
    private String userName;
    private String NgaySinh;
    private String CMT;
    private String DiaChi;
    private int role;

    public User(int ID, String hoTen, String userName, String ngaySinh, String CMT, String diaChi, int role) {
        this.ID = ID;
        HoTen = hoTen;
        this.userName = userName;
        NgaySinh = ngaySinh;
        this.CMT = CMT;
        DiaChi = diaChi;
        this.role = role;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getHoTen() {
        return HoTen;
    }

    public void setHoTen(String hoTen) {
        HoTen = hoTen;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getNgaySinh() {
        return NgaySinh;
    }

    public void setNgaySinh(String ngaySinh) {
        NgaySinh = ngaySinh;
    }

    public String getCMT() {
        return CMT;
    }

    public void setCMT(String CMT) {
        this.CMT = CMT;
    }

    public String getDiaChi() {
        return DiaChi;
    }

    public void setDiaChi(String diaChi) {
        DiaChi = diaChi;
    }

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
    }

}
