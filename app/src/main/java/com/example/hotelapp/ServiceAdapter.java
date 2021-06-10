package com.example.hotelapp;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

public class ServiceAdapter extends BaseAdapter {

    private Context context;
    private int layout;
    private List<Service> serviceList;

    public ServiceAdapter(Context context, int layout, List<Service> serviceList) {
        this.context = context;
        this.layout = layout;
        this.serviceList = serviceList;
    }

    @Override
    public int getCount() {
        return serviceList.size();
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
        TextView txtTenDV, txtGia, txtTrangThai;
        ImageView imageDelete, imageEdit;
        RelativeLayout layoutService;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

          ServiceAdapter.ViewHolder holder;

          if(convertView == null) {
              holder = new ServiceAdapter.ViewHolder();
              LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
              convertView = inflater.inflate(layout, null);
              holder.txtTenDV = (TextView) convertView.findViewById(R.id.textViewTenDichVuCustom);
              holder.txtGia = (TextView) convertView.findViewById(R.id.textViewGiaDichVuCustom);
              holder.txtTrangThai = (TextView) convertView.findViewById(R.id.textViewTrangThaiDichVuCustom);
//              holder.imageEdit = (ImageView) convertView.findViewById(R.id.imageViewEditService);
//              holder.imageDelete = (ImageView) convertView.findViewById(R.id.imageViewDeleteService);
              holder.layoutService = convertView.findViewById(R.id.layoutService);
              convertView.setTag(holder);
          } else {
              holder = (ServiceAdapter.ViewHolder) convertView.getTag();
          }

          Service service = serviceList.get(position);

          holder.txtTenDV.setText(service.getTenDV());
          holder.txtGia.setText(service.getGia() + " đ");
          if (service.getTrangThai() == 0){
              holder.txtTrangThai.setText("Ngừng phục vụ");
              holder.txtTrangThai.setTextColor(Color.parseColor("#F6100A"));
          } else {
              holder.txtTrangThai.setText("Hiện có");
              holder.txtTrangThai.setTextColor(Color.parseColor("#069779"));
          }

          return convertView;
    }
}
