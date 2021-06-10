package com.example.hotelapp;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

public class InvoiceAdapter extends BaseAdapter {
    private Context context;
    private int layout;
    private List<Invoice> invoiceList;

    public InvoiceAdapter(Context context, int layout, List<Invoice> invoiceList) {
        this.context = context;
        this.layout = layout;
        this.invoiceList = invoiceList;
    }

    @Override
    public int getCount() {
        return invoiceList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    private class ViewHolder{
        TextView txtTenPhong, txtKhachHang, txtMa, textNgayTao, txtThanhToan; // Mã hóa đơn = tenKH + so cmnd
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {





        return null;
    }
}
