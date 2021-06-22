package com.example.hotelapp.Model;

import java.io.Serializable;

public class Service implements Serializable {
    private int ID;
    private String TenDV;
    private int Gia;
    private int TrangThai;

    public Service(int ID, String tenDV, int gia, int trangThai) {
        this.ID = ID;
        TenDV = tenDV;
        Gia = gia;
        TrangThai = trangThai;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getTenDV() {
        return TenDV;
    }

    public void setTenDV(String tenDV) {
        TenDV = tenDV;
    }

    public int getGia() {
        return Gia;
    }

    public void setGia(int gia) {
        Gia = gia;
    }

    public int getTrangThai() {
        return TrangThai;
    }

    public void setTrangThai(int trangThai) {
        TrangThai = trangThai;
    }
}
