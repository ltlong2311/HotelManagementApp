package com.example.hotelapp.Adapters;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.hotelapp.R;
import com.example.hotelapp.Model.User;

import java.util.List;

public class UserAdapter extends BaseAdapter {

    private Context context;
    private int layout;
    private List<User> userList;

    public UserAdapter(Context context, int layout, List<User> userList) {
        this.context = context;
        this.layout = layout;
        this.userList = userList;
    }

    @Override
    public int getCount() {
        return userList.size();
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
        TextView txtTenUser, txtQuyen;
        ImageView imageAvatar, image;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        UserAdapter.ViewHolder holder;

        if(convertView == null) {
            holder = new UserAdapter.ViewHolder();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(layout, null);
            holder.txtTenUser = (TextView) convertView.findViewById(R.id.textViewNameUser);
            holder.txtQuyen = (TextView) convertView.findViewById(R.id.textViewRole);
            holder.imageAvatar = (ImageView) convertView.findViewById(R.id.imageUser);
//              holder.imageDelete = (ImageView) convertView.findViewById(R.id.imageViewDeleteService);
            convertView.setTag(holder);
        } else {
            holder = (UserAdapter.ViewHolder) convertView.getTag();
        }

        User user = userList.get(position);

        holder.txtTenUser.setText(user.getHoTen());
//        holder.txtGia.setText(service.getGia() + "đ");
        if (user.getRole() == 1){
            holder.imageAvatar.setColorFilter(context.getResources().getColor(R.color.color_user));
            holder.txtQuyen.setText("Nhân viên");

        } else if (user.getRole() == 2) {
//            holder.imageAvatar.setColorFilter(context.getResources().getColor(R.color.blue));
            holder.imageAvatar.setImageResource(R.drawable.admin);
            holder.txtQuyen.setText("(Quản lý)");
        }

        return convertView;
    }

}
