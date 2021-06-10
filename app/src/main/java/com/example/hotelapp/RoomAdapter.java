package com.example.hotelapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class  RoomAdapter extends BaseAdapter {

    private Context context;
    private int layout;
    private List<Room> RoomList;

    int vt= -1;

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

    public List<Room> getData() {
        return RoomList;
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
        holder.txtTang.setText("" + room.getTang());
        holder.txtGiaPhong.setText(room.getGia() + "");


        if (room.getTrangThai() == 0){
            holder.txtTrangThai.setText("Trống");
//            holder.layoutRoom.setBackgroundColor(Color.parseColor("#DBF1FB"));
            GradientDrawable gradientDrawable = (GradientDrawable) holder.layoutRoom.getBackground().mutate();
            gradientDrawable.setColor(Color.parseColor("#F9FEFF"));
        } else {
            holder.txtTrangThai.setText("Đã Dùng");
//            holder.layoutRoom.setBackgroundColor(Color.parseColor("#a9ffeb"));
            GradientDrawable gradientDrawable = (GradientDrawable) holder.layoutRoom.getBackground().mutate();
            gradientDrawable.setColor(Color.parseColor("#F4DFDF"));
        }

        if(room.getTuSua() == 1){
            holder.txtTrangThai.setText("Tạm dừng!");
//            holder.layoutRoom.setBackgroundColor(Color.parseColor("#65c6bb#8ce3da#AEEAE4"));
            GradientDrawable gradientDrawable = (GradientDrawable) holder.layoutRoom.getBackground().mutate();
            gradientDrawable.setColor(Color.parseColor("#C3EFEB"));
        }



//        holder.imageEdit.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(context, RoomEdit.class);
//                intent.putExtra("dataRoom", room);
//                context.startActivity(intent);
////                ((Activity)context).finish();
//            }
//        });
         holder.imageEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                PopupMenu popup = new PopupMenu(context, v, Gravity.RIGHT);
                PopupMenu popup = null;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
                    popup = new PopupMenu(context, v, Gravity.RIGHT);
                }
                popup.inflate(R.menu.menu_manager_room);
                popup.show();
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        int id = item.getItemId();
                        switch (id){
                            case R.id.checkin:
                                Toast.makeText(context, "Test", Toast.LENGTH_SHORT).show();
                                return true;
                            case R.id.checkout:
                                Toast.makeText(context, "Thông tin ứng dụng", Toast.LENGTH_SHORT).show();
                                return true;
                        }
                        return true;
                    }
                });
            }
        });


        return convertView;
    }
}
