package com.example.hotelapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class MenuAdapter extends BaseAdapter {

    private Context context;
    private int layout;
    private List<ItemMenu> list;

    public MenuAdapter(Context context, int layout, List<ItemMenu> list) {
        this.context = context;
        this.layout = layout;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
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
        TextView textView;
        ImageView imageView;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(layout,null);
            viewHolder = new ViewHolder();

            viewHolder.textView = (TextView) convertView.findViewById(R.id.textViewItem);
            viewHolder.imageView = (ImageView) convertView.findViewById(R.id.image_icon);

            convertView.setTag(viewHolder);

        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.textView.setText(list.get(position).name);
        viewHolder.imageView.setImageResource(list.get(position).icon);
        return convertView;
    }
}
