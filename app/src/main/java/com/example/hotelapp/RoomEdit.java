package com.example.hotelapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class RoomEdit extends AppCompatActivity {
    EditText edtTenPhong, edtTang, edtGiaPhong, edtTrangThai, edtTuSua;
    Button btnEditRoom, btnDeleteRoom;
    public static Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Window window = getWindow();
        setContentView(R.layout.activity_home);

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


        setContentView(R.layout.activity_edit_room);
        toolbar = findViewById(R.id.toolbar_ER);
        toolbar.setTitle("Sửa thông tin phòng");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        Intent intent = getIntent();
        Room room = (Room) intent.getSerializableExtra("dataRoom");
        Toast.makeText(this, room.getTenPhong(), Toast.LENGTH_SHORT).show();
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

}