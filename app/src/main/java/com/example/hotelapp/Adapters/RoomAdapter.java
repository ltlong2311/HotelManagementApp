package com.example.hotelapp.Adapters;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
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
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.hotelapp.Activities.Home;
import com.example.hotelapp.Activities.ServiceEdit;
import com.example.hotelapp.Fragment.listRoom.CheckInFragment;
import com.example.hotelapp.Fragment.listRoom.ListRoomFragment;
import com.example.hotelapp.Fragment.service.AddServiceFragment;
import com.example.hotelapp.Fragment.service.ServiceFragment;
import com.example.hotelapp.LoginActivity;
import com.example.hotelapp.R;
import com.example.hotelapp.Model.Room;
import com.muddzdev.styleabletoast.StyleableToast;

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
    String urlGetInvoiceInfo = "http://192.168.60.1/severApp/pays/";
    String tokenCheck = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJJRCI6IjEiLCJ1c2VyTmFtZSI6ImFkbWluIiwiSG9UZW4iOiJhZG1pbiJ9.234-aSxbQPO_Ozd4kcffsavH1FRWBgBx61dga5ZrAWE";
    public static final String SHARED_ID_INVOICE = "idInvoice";
    int vt= -1;
    EditText maPhongEdt, tenEdt, cmtEdt, sdtEdt, diachiEdt, maPhongEdtCO, tenEdtCO, cmtEdtCO, sdtEdtCO, diachiEdtCO, thanhtoanEdt;
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
        ImageView imageEdit;
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

//        SharedPreferences preferences = context.getApplicationContext().getSharedPreferences(SHARED_ID_INVOICE, Context.MODE_PRIVATE);
//        SharedPreferences.Editor editor = preferences.edit();
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
                                    DialogCheckOut(Gravity.CENTER, room);
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
                ten = tenEdt.getText().toString().trim();
                cmt = cmtEdt.getText().toString().trim();
                sdt = sdtEdt.getText().toString().trim();
                diachi = diachiEdt.getText().toString().trim();
                if (ten.matches("") || cmt.length() == 0 || sdt.length() == 0 || diachi.matches("")){
                    StyleableToast.makeText(context, "Vui lòng nhập đầy đủ thông tin!", Toast.LENGTH_SHORT, R.style.toastStyle).show();
                } else  {
                    checkIn(urlCheckIn, room.getId(), dialog);
                }
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

    private void DialogCheckOut(int gravity, Room room) {
        Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.check_out_room);

        Window window = dialog.getWindow();
        if (window == null){
            return;
        }
        int idPhong = room.getId();
        SharedPreferences preferences = context.getApplicationContext().getSharedPreferences(SHARED_ID_INVOICE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.apply();
        int idInvoice = preferences.getInt(String.valueOf(idPhong), 0);
        StyleableToast.makeText(context, String.valueOf(idInvoice), Toast.LENGTH_SHORT, R.style.toastSuccess2).show();

        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        WindowManager.LayoutParams windowAttributes = window.getAttributes();
        windowAttributes.gravity = gravity;
        window.setAttributes(windowAttributes);
        dialog.show();

        maPhongEdtCO = dialog.findViewById(R.id.editTextSoPhongCheckOut);
        tenEdtCO = dialog.findViewById(R.id.editTextTenKhachCheckOut);
        cmtEdtCO = dialog.findViewById(R.id.editTextCMTCheckOut);
        diachiEdtCO = dialog.findViewById(R.id.editTextDiaChiCheckOut);
        sdtEdtCO = dialog.findViewById(R.id.editTextSDTCheckOut);
        thanhtoanEdt = dialog.findViewById(R.id.editTextThanhToanCheckOut);

//        String payments = NumberFormat.getNumberInstance(Locale.US).format(room.getGia());

        maPhongEdtCO.setText(room.getTenPhong());
//        thanhtoanEdt.setText(payments+ " VND");
        getInvoiceInfo(urlGetInvoiceInfo+ idInvoice);

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
                ten = tenEdtCO.getText().toString().trim();
                cmt = cmtEdtCO.getText().toString().trim();
                sdt = sdtEdtCO.getText().toString().trim();
                diachi = diachiEdtCO.getText().toString().trim();
                if (ten.matches("") || cmt.length() == 0 || sdt.length() == 0 || diachi.matches("")){
                    StyleableToast.makeText(context, "Không thể để trống thông tin checkout!", Toast.LENGTH_SHORT, R.style.toastStyle).show();
                } else  {
                    confirmCheckOut(dialog, idInvoice, idPhong);
                }
            }
        });

    }

    public void confirmCheckOut(Dialog dialogCheckOut, int idInvoice, int idPhong){
        AlertDialog.Builder dialogDelInvoice = new AlertDialog.Builder(context);
        dialogDelInvoice.setMessage("Xác nhận đã thanh toán, thực hiện lưu hóa đơn?");
        dialogDelInvoice.setNegativeButton("Xác nhận", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                CheckOut(urlCheckOut, idInvoice, idPhong, dialogCheckOut);
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
        builder.setMessage("Phòng đã được sử dụng!")
                .setNegativeButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .show();
    }
    public void AlertRoomIsEmpty(){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
         builder.setMessage("Phòng trống!")
                .setNegativeButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

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
                        try {
                            JSONObject obj = new JSONObject(response);
                            String msg = obj.getString("msg");
                            String status = obj.getString("status");
                            JSONObject dataID = obj.getJSONObject("data");
                            int idInvoice = dataID.getInt("ID");
                            SharedPreferences preferences = context.getApplicationContext().getSharedPreferences(SHARED_ID_INVOICE, Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = preferences.edit();
                            editor.putInt(String.valueOf(idPhong), idInvoice);
                            editor.apply();
                            int value = preferences.getInt(String.valueOf(idPhong), 0);
                            StyleableToast.makeText(context, String.valueOf(value), Toast.LENGTH_SHORT, R.style.toastSuccess2).show();
                            if(status.equals("success")){
                                Toast.makeText(context, msg.toString(), Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                                ListRoomFragment optionsFrag = new ListRoomFragment ();
                                ((Home)context).getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment, optionsFrag,"").addToBackStack(null).commit();
                            } else {
                                Toast.makeText(context, msg.toString(), Toast.LENGTH_SHORT).show();
                            }

                        } catch (Throwable t) {
                            Toast.makeText(context, "Could not parse malformed JSON: \"" + response + "\"", Toast.LENGTH_SHORT).show();
                        }

//                        ListRoomFragment optionsFrag = new ListRoomFragment ();
//                        ((Home)context).getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment, optionsFrag,"").addToBackStack(null).commit();
//                        dialog.dismiss();
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
                params.put("DiaChi",diachiEdt.getText().toString().trim());
                params.put("SDT",sdtEdt.getText().toString().trim());
                params.put("DichVu","5");
                params.put("Option","TheoPhong");
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }
    public void getInvoiceInfo(String url){
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONObject json = response.getJSONObject("data");
                            String ThanhToan = json.getString("ThanhToan");
                            thanhtoanEdt.setText(ThanhToan);
                            JSONObject infoKhach = response.getJSONObject("infoKhach");
                            tenEdtCO.setText(infoKhach.getString("Ten"));
                            cmtEdtCO.setText(infoKhach.getString("CMT"));
                            sdtEdtCO.setText(infoKhach.getString("SDT"));
                            diachiEdtCO.setText(infoKhach.getString("DiaChi"));
                            Toast.makeText(context, infoKhach.toString(), Toast.LENGTH_SHORT).show();
                            Toast.makeText(context, json.toString(), Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        Toast.makeText(context, error.toString(), Toast.LENGTH_SHORT).show();
                    }
                }
        );
        requestQueue.add(request);
    }

    private void CheckOut(String url, int idInvoice, int idPhong, Dialog dialog) {
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        StringRequest stringRequest= new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            String msg = obj.getString("msg");
                            String status = obj.getString("status");
                            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
                            if(status.equals("success")){
                                StyleableToast.makeText(context, msg, Toast.LENGTH_SHORT, R.style.toastSuccess2).show();
                                SharedPreferences preferences = context.getApplicationContext().getSharedPreferences(SHARED_ID_INVOICE, Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = preferences.edit();
                                editor.remove(String.valueOf(idPhong));
                                editor.apply();
                                dialog.dismiss();
                                ListRoomFragment optionsFrag = new ListRoomFragment ();
                                ((Home)context).getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment, optionsFrag,"").addToBackStack(null).commit();
                            } else {
                                Toast.makeText(context, msg.toString(), Toast.LENGTH_SHORT).show();
                            }

                        } catch (Throwable t) {
                            Toast.makeText(context, "Could not parse malformed JSON: \"" + response + "\"", Toast.LENGTH_SHORT).show();
                        }

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
                params.put("ID", String.valueOf(idInvoice));
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }
}
