package com.example.hotelapp.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.hotelapp.Model.Invoice;
import com.example.hotelapp.R;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

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
        TextView txtMaPhong, txtKhachHang, txtMaHD, textNgayTao, txtThanhToan; 
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        InvoiceAdapter.ViewHolder holder;

        if (convertView == null){
            holder = new InvoiceAdapter.ViewHolder();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(layout,null);
            holder.txtMaPhong = (TextView) convertView.findViewById(R.id.textViewMaPhongHD);
            holder.txtKhachHang = (TextView) convertView.findViewById(R.id.textViewTenKhachHangHD);
            holder.txtMaHD = (TextView) convertView.findViewById(R.id.textViewMaHoaDon);
            holder.textNgayTao = (TextView) convertView.findViewById(R.id.textViewNgayTaoHD);
            holder.txtThanhToan = (TextView) convertView.findViewById(R.id.textViewThanhToanHD);
            convertView.setTag(holder);
        } else {
            holder = (InvoiceAdapter.ViewHolder) convertView.getTag();
        }
        Invoice invoice = invoiceList.get(position);
        holder.txtMaPhong.setText("Phòng "+invoice.getIDPhong());
        if (invoice.getIDUser() == 1){
            holder.txtKhachHang.setText("Người tạo: Quản lý");
        } else {
            holder.txtKhachHang.setText("Người tạo: Nhân viên " + invoice.getIDUser());
        }
        if (invoice.getID() == 1){
            holder.txtMaHD.setText("MãHD: admin7348");
        } else {
            holder.txtMaHD.setText("MãHD: "+ invoice.getID());
        }

        holder.textNgayTao.setText(""+invoice.getCreateDate());
        int cost = invoice.getThanhToan();
        holder.txtThanhToan.setText("Thanh toán: "+ NumberFormat.getNumberInstance(Locale.US).format(cost)+ " đ");

        return convertView;
    }
}
