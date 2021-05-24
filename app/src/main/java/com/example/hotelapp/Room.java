package com.example.hotelapp;

public class Room {
    private int id;
    private int tang;
    private String tenPhong;
    private int gia;
    private int trangThai;
    private int tuSua;

    public Room(int id, int tang, String tenPhong, int gia, int trangThai, int tuSua) {
        this.id = id;
        this.tang = tang;
        this.tenPhong = tenPhong;
        this.gia = gia;
        this.trangThai = trangThai;
        this.tuSua = tuSua;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTang() {
        return tang;
    }

    public void setTang(int tang) {
        this.tang = tang;
    }

    public String getTenPhong() {
        return tenPhong;
    }

    public void setTenPhong(String tenPhong) {
        this.tenPhong = tenPhong;
    }

    public int getGia() {
        return gia;
    }

    public void setGia(int gia) {
        this.gia = gia;
    }

    public int getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(int trangThai) {
        this.trangThai = trangThai;
    }

    public int getTuSua() {
        return tuSua;
    }

    public void setTuSua(int tuSua) {
        this.tuSua = tuSua;
    }
}
