package com.example.hotelapp.Activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.hotelapp.Adapters.RoomAdapter;
import com.example.hotelapp.R;
import com.example.hotelapp.Model.Room;
import com.google.android.material.appbar.AppBarLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class RoomEdit extends AppCompatActivity {
    EditText edtTenPhong, edtTang, edtGiaPhong;
    CheckBox cbxPhongTrong, cbxPhongDaDung;
    RadioButton rbnTuSua, rbnHoatDong;
    Button btnUpdateRoom, btnDeleteRoom;
    AppBarLayout appBarLayout;
    public static Toolbar toolbar;
    int ID = 0;
    int Status = 0;
    int isRepair = 0;
    ArrayList<Room> arrayRoom;
    RoomAdapter adapter;

    String urlUpdateRoom =  "http://192.168.60.1/serverApp/updateRoom.php";
    String urlDeleteRoom =  "http://192.168.60.1/serverApp/deleteRoom.php";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Window window = getWindow();
        setContentView(R.layout.activity_edit_room);

        if(Build.VERSION.SDK_INT>=19 && Build.VERSION.SDK_INT<21 )
        {
            SetWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, true);
        }
        if(Build.VERSION.SDK_INT>=19)
        {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE |
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }
        if(Build.VERSION.SDK_INT>=21)
        {
            SetWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }

        appBarLayout = (AppBarLayout) findViewById(R.id.appBarUpdateRoom);
        setContentView(R.layout.activity_edit_room);
        toolbar = findViewById(R.id.toolbar_ER);
        toolbar.setTitle("Sửa thông tin phòng");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        Intent intent = getIntent();
        Room room = (Room) intent.getSerializableExtra("dataRoom");
//        Toast.makeText(this, room.getTenPhong(), Toast.LENGTH_SHORT).show();

        getDataRoom();
        ID = room.getId();
        Status = room.getTrangThai();
        edtTenPhong.setText(room.getTenPhong());
        edtTang.setText(""+room.getTang());
        edtGiaPhong.setText(""+room.getGia());
        if (room.getTrangThai() == 0){
            cbxPhongTrong.setChecked(true);
            cbxPhongDaDung.setEnabled(false);
        } else {
            cbxPhongDaDung.setChecked(true);
            cbxPhongTrong.setEnabled(false);
        }
        if(room.getTuSua() == 1){
            rbnTuSua.setChecked(true);
        } else {
            rbnHoatDong.setChecked(true);
        }

        rbnTuSua.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)  {
                    isRepair = 1;
                } else {
                    isRepair = 0;
                }
            }
        });

        btnUpdateRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String tang = edtTang.getText().toString().trim();
                String tenPhong = edtTenPhong.getText().toString().trim();
                String giaPhong = edtGiaPhong.getText().toString().trim();

                if (tang.matches("") || tenPhong.matches("") || giaPhong.length() == 0){
                    Toast.makeText(RoomEdit.this, "Vui long dien day du thong tin!", Toast.LENGTH_SHORT).show();
                } else  {
                    UpdateRoom(urlUpdateRoom);
                    Intent intent = new Intent(RoomEdit.this, Home.class);
                    intent.putExtra("url", "url");
                    startActivity(intent);
                }
            }
        });
        btnDeleteRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmDeleteRoom(room.getTenPhong());
            }
        });
    }


    private void getDataRoom() {
        btnUpdateRoom = (Button) findViewById(R.id.btn_update_room);
        btnDeleteRoom = (Button) findViewById(R.id.btn_delete_room);
        edtTenPhong = (EditText) findViewById(R.id.edtUpdateTenPhong);
        edtTang = (EditText) findViewById(R.id.edtUpdateTang);
        edtGiaPhong = (EditText) findViewById(R.id.edtUpdateGiaPhong);
        cbxPhongTrong = (CheckBox) findViewById(R.id.checkboxEmptyRoom);
        cbxPhongDaDung = (CheckBox) findViewById(R.id.checkboxRoomIsUsed);
        rbnTuSua = (RadioButton) findViewById(R.id.rbnRoomIsFixing);
        rbnHoatDong = (RadioButton) findViewById(R.id.rbnRoomActivity);

    }

    public void UpdateRoom(String url){
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest= new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.trim().equals("success")){
                            Toast.makeText(RoomEdit.this, "Cập nhật thành công!", Toast.LENGTH_SHORT).show();
//                            finish();
//                            RoomEdit.toolbar.setVisibility(View.GONE);
//                            ActionBar actionBar = getSupportActionBar();
//                            actionBar.hide();
//                        toolbar.setTitle("Sơ đồ phòng");
//                            getSupportFragmentManager().beginTransaction().add(R.id.updateRoom,
//                                    new ListRoomFragment()).commit();
//                            Intent intent = new Intent(RoomEdit.this, Home.class);
//                            intent.putExtra("url", "true");
//                            startActivity(intent);
                        } else  {
                            Toast.makeText(RoomEdit.this, "Lỗi cập nhật", Toast.LENGTH_SHORT).show();
//                            adapter.notifyDataSetChanged();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(RoomEdit.this, error.toString(), Toast.LENGTH_SHORT).show();
                    }
                }
        ){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<>();
                params.put("ID", String.valueOf(ID));
                params.put("IDTang",edtTang.getText().toString().trim());
                params.put("TenPhong",edtTenPhong.getText().toString().trim());
                params.put("Gia",edtGiaPhong.getText().toString().trim());
                params.put("TrangThai",String.valueOf(Status));
                params.put("TuSua",String.valueOf(isRepair));
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }

    public void confirmDeleteRoom(String tenPhong){
        AlertDialog.Builder dialogDelRoom = new AlertDialog.Builder(RoomEdit.this);
        dialogDelRoom.setMessage("Xác nhận xóa phòng "+ tenPhong + " ?");
        dialogDelRoom.setNegativeButton("Có", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                 deleteRoom(urlDeleteRoom);
            }
        });
        dialogDelRoom.setPositiveButton("Không", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        dialogDelRoom.show();
    }
    public void deleteRoom(String url){
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest= new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.trim().equals("success")){
                            Toast.makeText(RoomEdit.this, "Xóa thành công!", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(RoomEdit.this, Home.class);
                            intent.putExtra("url", "url");
                            startActivity(intent);

                        } else  {
                            Toast.makeText(RoomEdit.this, "Lỗi xóa đối tượng", Toast.LENGTH_SHORT).show();
                            adapter.notifyDataSetChanged();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(RoomEdit.this, error.toString(), Toast.LENGTH_SHORT).show();
                    }
                }
        ){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
//                params.put("token","eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJJRCI6IjEiLCJ1c2VyTmFtZSI6ImFkbWluIiwiSG9UZW4iOiJhZG1pbiJ9.234-aSxbQPO_Ozd4kcffsavH1FRWBgBx61dga5ZrAWE");
                params.put("ID", String.valueOf(ID));
                return params;
            }

//            @Override
//            public Map<String, String> getHeaders() throws AuthFailureError {
//                Map<String, String> params = new HashMap<String, String>();
//                params.put("Content-Type", "application/json");
//                return params;
//            }
        };
        requestQueue.add(stringRequest);
    }
    private static void SetWindowFlag(RoomEdit roomEdit, final int Bits, Boolean on) {
        Window win =  roomEdit.getWindow();
        WindowManager.LayoutParams Winparams = win.getAttributes();
        if (on) {
            Winparams.flags  |=Bits;
        } else {
            Winparams.flags &= ~Bits;
        }
        win.setAttributes(Winparams);

    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    public void hideKeyBoard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

}