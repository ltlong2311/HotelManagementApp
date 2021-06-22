package com.example.hotelapp.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.hotelapp.Model.Invoice;
import com.example.hotelapp.R;

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
        TextView txtMaPhong, txtKhachHang, txtMaHD, textNgayTao, txtThanhToan; // Mã hóa đơn = tenKH + so cmnd
                                                                             //string = string.replaceAll("\\s+","")
    }

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
        holder.txtThanhToan.setText("Thanh toán: "+invoice.getThanhToan());

//        JSONObject json = invoice.getInfoKhach();
//        JSONObject jsonAddress = null;
//        try {
//            String cmt = json.getString("CMT");
//            holder.txtThanhToan.setText(""+ cmt);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }

//        JSONArray arr = invoice.getDichVu();
//        String dv = "";
//        int gia = 0;
//        ArrayList<String> listTenDV
//                = new ArrayList<String>();
//        for (int i = 0; i < arr.length(); i++){
//            try {
//                JSONObject dichvu = arr.getJSONObject(i);
//                //dv += dichvu.getString("TenDV");
//                listTenDV.add(dichvu.getString("TenDV"));
//                dv = listTenDV.toString();
//                dv = dv.replace("[", "")
//                        .replace("]", "");
//
//                gia += dichvu.getInt("Gia");
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//        }
//        holder.txtThanhToan.setText(""+ gia);


        return convertView;
    }
}
