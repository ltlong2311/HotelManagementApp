package com.example.hotelapp;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

public class CustomerAdapter extends BaseAdapter {
    private Context context;
    private int layout;
    private List<Customer> customerList;

    @Override
    public int getCount() {
        return customerList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return null;
    }
}
