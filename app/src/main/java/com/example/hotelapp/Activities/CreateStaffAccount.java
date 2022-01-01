package com.example.hotelapp.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.hotelapp.API.BaseUrl;
import com.example.hotelapp.R;
import com.google.android.material.appbar.AppBarLayout;
import com.muddzdev.styleabletoast.StyleableToast;

import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class CreateStaffAccount extends AppCompatActivity {
    BaseUrl baseUrl = new BaseUrl();
    EditText edtUsername, edtPassword, edtTen, edtCMT, edtNgaySinh, edtDiaChi;
    Button btnCreate;
    AppBarLayout appBarLayout;
    DatePickerDialog.OnDateSetListener setListener;
    String dateData;
    Toolbar toolbar;
    String urlCreateUser = baseUrl.getBaseURL() + "/createUsers";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_staff_account);

        if (Build.VERSION.SDK_INT >= 19 && Build.VERSION.SDK_INT < 21) {
            SetWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, true);
        }
        if (Build.VERSION.SDK_INT >= 19) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE |
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }
        if (Build.VERSION.SDK_INT >= 21) {
            SetWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }

        appBarLayout = findViewById(R.id.appBarUpdateService);
        toolbar = findViewById(R.id.toolbar_ER);
        toolbar.setTitle("Thêm tài khoản");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);
        edtUsername = findViewById(R.id.editTextUsernameS);
        edtPassword = findViewById(R.id.editTextPasswordS);
        edtTen = findViewById(R.id.editTextStaffName);
        edtCMT = findViewById(R.id.editTextCMTS);
        edtDiaChi = findViewById(R.id.editTextDiaChiS);
        edtNgaySinh = findViewById(R.id.editTextNgaySinhS);
        btnCreate = findViewById(R.id.btn_create_account);
        edtNgaySinh.setOnClickListener((View v) -> {
            DatePickerDialog datePickerDialog = new DatePickerDialog(this, android.R.style.Theme_Holo_Light_Dialog_MinWidth
                    , setListener, year, month, day);
            datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            datePickerDialog.show();
        });
        setListener = (view, year1, month1, dayOfMonth) -> {
            month1 = month1 + 1;
            String date = dayOfMonth + "/" + month1 + "/" + year1;
            dateData = year1 + "-" + month1 + "-" + dayOfMonth;
            edtNgaySinh.setText(date);
        };
        btnCreate.setOnClickListener(v -> {
            String username = edtUsername.getText().toString().trim();
            String password = edtPassword.getText().toString().trim();
            String ten = edtPassword.getText().toString().trim();
            String cmt = edtPassword.getText().toString().trim();
            String diachi = edtPassword.getText().toString().trim();
            String ngaysinh = edtPassword.getText().toString().trim();

            if (username.matches("") || password.matches("") || ten.matches("") || diachi.matches("") ||
                    cmt.length() == 0 || ngaysinh.matches("")) {
                StyleableToast.makeText(CreateStaffAccount.this, "Vui lòng điền đẩy đủ thông tin!", Toast.LENGTH_SHORT, R.style.toastStyle).show();
            } else if (cmt.length() > 11) {
                StyleableToast.makeText(CreateStaffAccount.this, "Chứng minh thư không quá 11 số!", Toast.LENGTH_SHORT, R.style.toastStyle).show();
            } else {
                SharedPreferences preferences = CreateStaffAccount.this.getApplicationContext().getSharedPreferences("tokenLogin", Context.MODE_PRIVATE);
                String token = preferences.getString("token", "");
                createUser(urlCreateUser, token);
            }
        });
    }

    private void createUser(String urlCreateUser, String token) {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, urlCreateUser,
                response -> {
                    try {
                        JSONObject obj = new JSONObject(response);
                        String status = obj.getString("status");
                        String msg = obj.getString("msg");
                        if (status.equals("success")) {
                            StyleableToast.makeText(CreateStaffAccount.this, "Thêm tài khoản thành công!", Toast.LENGTH_SHORT, R.style.toastBlueLight).show();
                            Intent intent = new Intent(CreateStaffAccount.this, Home.class);
                            intent.putExtra("manage", "manage");
                            startActivity(intent);
                        } else {
                            StyleableToast.makeText(CreateStaffAccount.this, msg, Toast.LENGTH_SHORT, R.style.toastStyle).show();
                        }
                    } catch (Throwable t) {
                        StyleableToast.makeText(CreateStaffAccount.this, "Kiểm tra lại quyền!", Toast.LENGTH_SHORT, R.style.toastStyle).show();
                    }
                },
                error -> StyleableToast.makeText(CreateStaffAccount.this, error.toString(), Toast.LENGTH_SHORT, R.style.toastError).show()
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("token", token);
                params.put("HoTen", edtTen.getText().toString().trim());
                params.put("username", edtUsername.getText().toString().trim());
                params.put("password", edtPassword.getText().toString().trim());
                params.put("NgaySinh", dateData);
                params.put("CMT", edtCMT.getText().toString().trim());
                params.put("DiaChi", edtDiaChi.getText().toString().trim());
                params.put("level", "1");
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }

    private static void SetWindowFlag(CreateStaffAccount createStaffAccount, final int Bits, Boolean on) {
        Window win = createStaffAccount.getWindow();
        WindowManager.LayoutParams Winparams = win.getAttributes();
        if (on) {
            Winparams.flags |= Bits;
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