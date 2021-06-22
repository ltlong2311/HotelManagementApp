package com.example.hotelapp.Model;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.List;

public class Invoice implements Serializable{
    private int ID;
    private int IDPhong;
    private int IDUser;
//    private Customer InfoKhach;
//    private List<ServiceUsed> DichVu;
    private JSONObject InfoKhach;
    private JSONArray DichVu;
    private int ThanhToan;
    private String createDate;

//    public Invoice(int ID, int IDPhong, int IDUser, Customer infoKhach, List<ServiceUsed> dichVu, int thanhToan, String createDate) {
//        this.ID = ID;
//        this.IDPhong = IDPhong;
//        this.IDUser = IDUser;
//        InfoKhach = infoKhach;
//        DichVu = dichVu;
//        ThanhToan = thanhToan;
//        this.createDate = createDate;
//    }


    public Invoice(int ID, int IDPhong, int IDUser, JSONObject infoKhach, JSONArray dichVu, int thanhToan, String createDate) {
        this.ID = ID;
        this.IDPhong = IDPhong;
        this.IDUser = IDUser;
        InfoKhach = infoKhach;
        DichVu = dichVu;
        ThanhToan = thanhToan;
        this.createDate = createDate;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public int getIDPhong() {
        return IDPhong;
    }

    public void setIDPhong(int IDPhong) {
        this.IDPhong = IDPhong;
    }

    public int getIDUser() {
        return IDUser;
    }

    public void setIDUser(int IDUser) {
        this.IDUser = IDUser;
    }

//    public Customer getInfoKhach() {
//        return InfoKhach;
//    }
//
//    public void setInfoKhach(Customer infoKhach) {
//        InfoKhach = infoKhach;
//    }
//
//    public List<ServiceUsed> getDichVu() {
//        return DichVu;
//    }
//
//    public void setDichVu(List<ServiceUsed> dichVu) {
//        DichVu = dichVu;
//    }


    public JSONObject getInfoKhach() {
        return InfoKhach;
    }

    public void setInfoKhach(JSONObject infoKhach) {
        InfoKhach = infoKhach;
    }

    public JSONArray getDichVu() {
        return DichVu;
    }

    public void setDichVu(JSONArray dichVu) {
        DichVu = dichVu;
    }

    public int getThanhToan() {
        return ThanhToan;
    }

    public void setThanhToan(int thanhToan) {
        ThanhToan = thanhToan;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

}

