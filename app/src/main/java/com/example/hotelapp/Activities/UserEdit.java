package com.example.hotelapp.Activities;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.hotelapp.API.BaseUrl;
import com.example.hotelapp.Model.Room;
import com.example.hotelapp.Model.Service;
import com.example.hotelapp.Model.User;
import com.example.hotelapp.R;
import com.google.android.material.appbar.AppBarLayout;
import com.muddzdev.styleabletoast.StyleableToast;

import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class UserEdit extends AppCompatActivity {
    BaseUrl baseUrl = new BaseUrl();
    TextView textMaNV,textUsername, textTenNV, textNgaySinhNV, textCMT, textDiaChiNV;
    Button btnUpdateUserInfo, btnAdd, btnCannel;
    AppBarLayout appBarLayout;
    Toolbar toolbar;
    EditText nameEdt, cmtEdt, dateOfBirthEdt, addressEdt;
    DatePickerDialog.OnDateSetListener setListener;
    String dateData, token;

    String urlUpdateUser =  baseUrl.getBaseURL()+ "/updateUsers";

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_edit);

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

        appBarLayout = findViewById(R.id.appBarUpdateService);
        toolbar = findViewById(R.id.toolbar_ER);
        toolbar.setTitle("Thông tin nhân viên");

        SharedPreferences preferences = UserEdit.this.getApplicationContext().getSharedPreferences("tokenLogin", Context.MODE_PRIVATE);
        token = preferences.getString("token", "");

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        Intent intent = getIntent();
        User user = (User) intent.getSerializableExtra("dataUser");

        textMaNV = findViewById(R.id.txtIdUser);
        textUsername = findViewById(R.id.txtUserNameInfo);
        textTenNV = findViewById(R.id.textViewTenNVInfo);
        textDiaChiNV = findViewById(R.id.textViewDiaChiInfo);
        textCMT = findViewById(R.id.textViewCMTInfo);
        textNgaySinhNV = findViewById(R.id.textNgaySinhInfo);
        btnUpdateUserInfo = findViewById(R.id.btn_change_info);

        String maNV = "(ID:" + user.getID()+ ")";
        textMaNV.setText(maNV);
        textUsername.setText(user.getUserName());
        textTenNV.setText(user.getHoTen());
        textDiaChiNV.setText(user.getDiaChi());
        textCMT.setText(user.getCMT());
        @SuppressLint("SimpleDateFormat")
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Date date;
        try {
            date = df.parse(user.getNgaySinh());
            @SuppressLint("SimpleDateFormat")
            String dateDMY = new SimpleDateFormat("dd-MM-yyyy").format(date);
            textNgaySinhNV.setText(dateDMY);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        btnUpdateUserInfo.setOnClickListener(v -> {
             DialogUpdate(user);
        });
    }

    private void DialogUpdate(User user) {
        Dialog dialog = new Dialog(UserEdit.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.update_user_info);

        Window window = dialog.getWindow();
        if (window == null){
            return;
        }

        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        WindowManager.LayoutParams windowAttributes = window.getAttributes();
        windowAttributes.gravity = Gravity.CENTER;
        window.setAttributes(windowAttributes);
        dialog.show();

        int idUser = user.getID();

        btnAdd = dialog.findViewById(R.id.btnSubmitUpdateUser);
        btnCannel = dialog.findViewById(R.id.btnCancelUpdateUser);

        nameEdt = dialog.findViewById(R.id.editTextStaffNameUpdate);
        cmtEdt = dialog.findViewById(R.id.editTextCMTUpdate);
        dateOfBirthEdt = dialog.findViewById(R.id.editTextNgaySinhUpdate);
        addressEdt = dialog.findViewById(R.id.editTextDiaChiUpdate);

        Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);

        dateOfBirthEdt.setOnClickListener((View v) ->{
            DatePickerDialog datePickerDialog = new DatePickerDialog(this, android.R.style.Theme_Holo_Light_Dialog_MinWidth
                    ,setListener,year,month,day);
            datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            datePickerDialog.show();
        });
        setListener = (view, year1, month1, dayOfMonth) -> {
            month1 = month1 + 1;
            String date = dayOfMonth+"/"+ month1 +"/"+year1;
            dateData =  year1+"-"+ month1 +"-"+dayOfMonth;
            dateOfBirthEdt.setText(date);
        };
        btnCannel.setOnClickListener(v -> dialog.dismiss());

        btnAdd.setOnClickListener(v -> {
            String name = nameEdt.getText().toString().trim();
            String cmt = cmtEdt.getText().toString().trim();
            String dateOfBirth = dateOfBirthEdt.getText().toString().trim();
            String address = addressEdt.getText().toString().trim();
            if (name.matches("") || cmt.length() == 0 || dateOfBirth.matches("") || address.matches("")){
                StyleableToast.makeText(UserEdit.this, "Vui lòng nhập đầy đủ thông tin!", Toast.LENGTH_SHORT, R.style.toastStyle).show();
            } else  {
                updateUser(urlUpdateUser, idUser, dialog);
            }
        });

    }

    private void updateUser(String urlUpdateUser, int idUser, Dialog dialog) {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest= new StringRequest(Request.Method.POST, urlUpdateUser,
                response -> {
                    try {
                        JSONObject obj = new JSONObject(response);
                        String status = obj.getString("status");
                        String msg = obj.getString("msg");
                        if(status.equals("success")){
                            StyleableToast.makeText(UserEdit.this, "Cập nhật thông tin thành công!", Toast.LENGTH_SHORT, R.style.toastBlueLight).show();
                            Intent intent = new Intent(UserEdit.this, Home.class);
                            intent.putExtra("manage", "manage");
                            startActivity(intent);
                            dialog.dismiss();
                        } else {
                            dialog.dismiss();
                            StyleableToast.makeText(UserEdit.this, msg, Toast.LENGTH_SHORT, R.style.toastStyle).show();
                        }
                    } catch (Throwable t) {
                        StyleableToast.makeText(UserEdit.this, "Kiểm tra lại quyền!", Toast.LENGTH_SHORT, R.style.toastStyle).show();
                        dialog.dismiss();
                    }
                },
                error -> StyleableToast.makeText(UserEdit.this, error.toString(), Toast.LENGTH_SHORT, R.style.toastError).show()
        ){
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("token",token);
                params.put("HoTen",nameEdt.getText().toString().trim());
                params.put("NgaySinh",dateData);
                params.put("CMT",cmtEdt.getText().toString().trim());
                params.put("DiaChi",addressEdt.getText().toString().trim());
                params.put("ID",String.valueOf(idUser));
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }


    private static void SetWindowFlag(UserEdit userEdit, final int Bits, Boolean on) {
        Window win =  userEdit.getWindow();
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