package com.example.hotelapp;

import java.io.Serializable;

public class ServiceUsed implements Serializable {
    private String TenDV;
    private int Gia;

    public ServiceUsed(String tenDV, int gia) {
        TenDV = tenDV;
        Gia = gia;
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
}
