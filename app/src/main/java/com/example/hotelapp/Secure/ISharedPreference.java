package com.example.hotelapp.Secure;

public interface ISharedPreference {
    String get(String name);
    void put(String name, String value);
    void remove(String name);
}
