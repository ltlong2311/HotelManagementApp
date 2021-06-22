package com.example.hotelapp.Model;

import java.io.Serializable;

public class Customer implements Serializable{
    private String Ten;
    private String CMT;
    private String SDT;
    private String Diachi;

    public Customer(String ten, String CMT, String SDT, String diachi) {
        Ten = ten;
        this.CMT = CMT;
        this.SDT = SDT;
        Diachi = diachi;
    }

    public String getTen() {
        return Ten;
    }

    public void setTen(String ten) {
        Ten = ten;
    }

    public String getCMT() {
        return CMT;
    }

    public void setCMT(String CMT) {
        this.CMT = CMT;
    }

    public String getSDT() {
        return SDT;
    }

    public void setSDT(String SDT) {
        this.SDT = SDT;
    }

    public String getDiachi() {
        return Diachi;
    }

    public void setDiachi(String diachi) {
        Diachi = diachi;
    }
}



