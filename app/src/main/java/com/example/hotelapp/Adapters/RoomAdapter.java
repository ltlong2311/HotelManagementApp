package com.example.hotelapp.Adapters;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.hotelapp.Activities.Home;
import com.example.hotelapp.Fragment.listRoom.CheckInFragment;
import com.example.hotelapp.Fragment.listRoom.ListRoomFragment;
import com.example.hotelapp.Fragment.service.AddServiceFragment;
import com.example.hotelapp.Fragment.service.ServiceFragment;
import com.example.hotelapp.R;
import com.example.hotelapp.Model.Room;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class  RoomAdapter extends BaseAdapter {

    private Context context;
    private int layout;
    private List<Room> RoomList;

    String urlCheckIn = "http://192.168.60.1/severApp/createPay";
    String urlCheckOut = "http://192.168.60.1/severApp/checkoutPay";
    String tokenCheck = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJJRCI6IjEiLCJ1c2VyTmFtZSI6ImFkbWluIiwiSG9UZW4iOiJhZG1pbiJ9.234-aSxbQPO_Ozd4kcffsavH1FRWBgBx61dga5ZrAWE";

    int vt= -1;
    EditText maPhongEdt, tenEdt, cmtEdt, sdtEdt, diachiEdt;
    String maPhong, ten, cmt, sdt, diachi;
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
                popup.inflate(R.menu.menu_manage_room);
                popup.show();
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        int id = item.getItemId();
                        switch (id){
                            case R.id.checkin:
                                if (room.getTuSua() == 1){
                                    AlertRoomIsFixed();
                                } else if (room.getTrangThai() == 1){
                                    AlertRoomIsUsed();
                                } else {
                                    DialogCheckIn(Gravity.CENTER, room);
                                }
//                                CheckInFragment checkIn = new CheckInFragment();
//                                FragmentManager fragmentManager = ((FragmentActivity) context).getSupportFragmentManager();
//                                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//                                fragmentTransaction.replace(R.id.drawer_layout, checkIn);
//                                fragmentTransaction.addToBackStack(null);
//                                fragmentTransaction.commit();
                                return true;
                            case R.id.checkout:
                                if (room.getTuSua() == 1){
                                    AlertRoomIsFixed();
                                } else if (room.getTrangThai() == 0){
                                    AlertRoomIsEmpty();
                                }  else {
                                    DialogCheckOut(Gravity.CENTER);
                                }
                                return true;
                            case R.id.addService:
                                if (room.getTuSua() == 1){
                                    AlertRoomIsFixed();
                                } else if (room.getTrangThai() == 0){
                                    AlertRoomIsEmpty();
                                }  else {
                                    DialogAddService(Gravity.CENTER);
                                }
                                return true;
                        }
                        return true;
                    }
                });
            }
        });


        return convertView;
    }
    private void DialogCheckIn(int gravity, Room room) {
        Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.check_in_room);

        Window window = dialog.getWindow();
        if (window == null){
            return;
        }

        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        WindowManager.LayoutParams windowAttributes = window.getAttributes();
        windowAttributes.gravity = gravity;
        window.setAttributes(windowAttributes);
        dialog.show();


        Button btnAdd = dialog.findViewById(R.id.btn_submit_checkIn);
        Button btnCannel = dialog.findViewById(R.id.btn_cancel_checkIn);

        maPhongEdt = dialog.findViewById(R.id.editTextSoPhongCheckIn);
        tenEdt = dialog.findViewById(R.id.editTextTenKhachCheckIn);
        cmtEdt = dialog.findViewById(R.id.editTextCMTCheckIn);
        sdtEdt = dialog.findViewById(R.id.editTextSDTCheckIn);
        diachiEdt = dialog.findViewById(R.id.editTextDiaChiCheckIn);

        maPhongEdt.setText(room.getTenPhong());

        btnCannel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkIn(urlCheckIn, room.getId(), dialog);
            }
        });

    }


    private void DialogAddService(int gravity) {
        Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.add_service_to_room);

        Window window = dialog.getWindow();
        if (window == null){
            return;
        }

        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        WindowManager.LayoutParams windowAttributes = window.getAttributes();
        windowAttributes.gravity = gravity;
        window.setAttributes(windowAttributes);
        dialog.show();

        Button btnAdd = dialog.findViewById(R.id.btn_add_service_room);
        Button btnCannel = dialog.findViewById(R.id.btn_cancel_add_service);
        btnCannel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }

    private void DialogCheckOut(int gravity) {
        Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.check_out_room);

        Window window = dialog.getWindow();
        if (window == null){
            return;
        }

        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        WindowManager.LayoutParams windowAttributes = window.getAttributes();
        windowAttributes.gravity = gravity;
        window.setAttributes(windowAttributes);
        dialog.show();



//        EditText editPhone = dialog.findViewById(R.id.edtPhone);
        Button btnSubmitCheckOut = dialog.findViewById(R.id.btn_submit_checkout);
        Button btnCancel = dialog.findViewById(R.id.btn_cancel_checkout);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        btnSubmitCheckOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmCheckOut();
            }
        });

    }


    public void confirmCheckOut(){
        AlertDialog.Builder dialogDelInvoice = new AlertDialog.Builder(context);
        dialogDelInvoice.setMessage("Có xác nhận lưu hóa đơn?");
        dialogDelInvoice.setNegativeButton("Có", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                CheckOut();
            }
        });
        dialogDelInvoice.setPositiveButton("Không", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        dialogDelInvoice.show();
    }
    public void AlertRoomIsUsed(){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
//        builder.setTitle("Thông báo")
        builder.setMessage("Phòng đã được sử dụng!")
                .setNegativeButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        CheckOut();
                    }
                })
                .show();
    }
    public void AlertRoomIsEmpty(){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
//        builder.setTitle("Thông báo")
         builder.setMessage("Phòng trống!")
                .setNegativeButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        CheckOut();
                    }
                });
        builder.show();
    }
    public void AlertRoomIsFixed(){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Thông báo")
                .setMessage("Phòng đang tạm dừng hoạt động!")
                .setNegativeButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        CheckOut();
                    }
                });
        builder.show();
    }

    private void checkIn(String url, int idPhong, Dialog dialog) {
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        StringRequest stringRequest= new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
//                        try {
//                            JSONObject obj = new JSONObject(response);
//                            String msg = obj.getString("msg");
//                            String status = obj.getString("status");
//                            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
//                            if(status.equals("success")){
//                                Toast.makeText(context, msg.toString(), Toast.LENGTH_SHORT).show();
//                                dialog.dismiss();
//                                ListRoomFragment optionsFrag = new ListRoomFragment ();
//                                ((Home)context).getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment, optionsFrag,"").addToBackStack(null).commit();
//                            } else {
//                                Toast.makeText(context, msg.toString(), Toast.LENGTH_SHORT).show();
//                            }
//
//                        } catch (Throwable t) {
//                            Toast.makeText(context, "Could not parse malformed JSON: \"" + response + "\"", Toast.LENGTH_SHORT).show();
//                        }

                        ListRoomFragment optionsFrag = new ListRoomFragment ();
                        ((Home)context).getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment, optionsFrag,"").addToBackStack(null).commit();
                        dialog.dismiss();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("AAA", "Lỗi:\n" + error.toString());
                        Toast.makeText(context, error.toString(), Toast.LENGTH_SHORT).show();
                    }
                }
        ){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("token",tokenCheck);
                params.put("IDPhong", String.valueOf(idPhong));
                params.put("Ten",tenEdt.getText().toString().trim());
                params.put("CMT",cmtEdt.getText().toString().trim());
                params.put("SDT",cmtEdt.getText().toString().trim());
                params.put("DichVu","5");
                params.put("Option","TheoGio");
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }

    private void CheckOut() {

    }
}
