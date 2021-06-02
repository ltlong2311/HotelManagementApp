package com.example.hotelapp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class  RoomAdapter extends BaseAdapter {

    private Context context;
    private int layout;
    private List<Room> RoomList;

    public RoomAdapter(Context context, int layout, List<Room> roomList) {
        this.context = context;
        this.layout = layout;
        RoomList = roomList;
    }

    @Override
    public int getCount() {
        return RoomList.size();
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
        TextView txtTenPhong, txtTang, txtTrangThai, txtGiaPhong;
        ImageView imageDelete, imageEdit;
        RelativeLayout layoutRoom;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;
        if (convertView == null){
            holder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(layout,null);
            holder.txtTenPhong = (TextView) convertView.findViewById(R.id.textViewTenPhongCustom);
            holder.txtTang = (TextView) convertView.findViewById(R.id.textViewTangCustom);
            holder.txtTrangThai = (TextView) convertView.findViewById(R.id.textViewTrangThaiCustom);
            holder.txtGiaPhong = (TextView) convertView.findViewById(R.id.textViewGiaPhongCustom);
            holder.imageEdit = (ImageView) convertView.findViewById(R.id.imageViewEditRoom);
//            holder.imageDelete = (ImageView) convertView.findViewById(R.id.imageViewDeleteRoom);
            holder.layoutRoom = convertView.findViewById(R.id.layoutRoom);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        Room room = RoomList.get(position);
        holder.txtTenPhong.setText(room.getTenPhong());
        holder.txtTang.setText("Tầng " + room.getTang());
        holder.txtGiaPhong.setText(room.getGia() + "");


        if (room.getTrangThai() == 0){
            holder.txtTrangThai.setText("Trống");
//            holder.layoutRoom.setBackgroundColor(Color.parseColor("#DBF1FB"));
            GradientDrawable gradientDrawable = (GradientDrawable) holder.layoutRoom.getBackground().mutate();
            gradientDrawable.setColor(Color.parseColor("#DBF1FB"));
        } else {
            holder.txtTrangThai.setText("Đã Dùng");
//            holder.layoutRoom.setBackgroundColor(Color.parseColor("#a9ffeb"));
            GradientDrawable gradientDrawable = (GradientDrawable) holder.layoutRoom.getBackground().mutate();
            gradientDrawable.setColor(Color.parseColor("#ffdce4"));
        }

        if(room.getTuSua() == 1){
            holder.txtTrangThai.setText("Đang sửa!");
//            holder.layoutRoom.setBackgroundColor(Color.parseColor("#65c6bb"));
            GradientDrawable gradientDrawable = (GradientDrawable) holder.layoutRoom.getBackground().mutate();
            gradientDrawable.setColor(Color.parseColor("#8ce3da"));
        }

        holder.imageEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(context, room.getId(), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(context, RoomEdit.class);
                intent.putExtra("dataRoom", room);
//                intent.putExtra("TenPhong", room.getTenPhong());
                context.startActivity(intent);
            }
        });

        return convertView;
    }
}
