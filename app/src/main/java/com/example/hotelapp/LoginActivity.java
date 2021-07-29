package com.example.hotelapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.hotelapp.Activities.Home;
import com.muddzdev.styleabletoast.StyleableToast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {
    String urlTest = "http://192.168.1.4:4000/getData";
    String urlLogin = "http://192.168.60.1/severApp/login";
    EditText edtUsername, edtPassword;
    Button button_login;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

//        getSupportActionBar().hide();
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

        edtUsername = findViewById(R.id.editTextUsername);
        edtPassword = findViewById(R.id.editTextPassword);

        button_login = findViewById(R.id.button_login);
        button_login.setOnClickListener(v -> {
            String username = edtUsername.getText().toString().trim();
            String password = edtPassword.getText().toString().trim();
            if (username.matches("") || password.matches("") ){
                StyleableToast.makeText(LoginActivity.this, "Vui lòng nhập đầy đủ thông tin!", Toast.LENGTH_SHORT, R.style.toastStyle).show();
            } else {
                login(urlLogin);
            }
        });
        getData(urlTest);
    }

    public void openHomePage(){
        Intent intent = new Intent(this, Home.class);
        startActivity(intent);
    }
    private void login(String url) {
        RequestQueue requestQueue = Volley.newRequestQueue(LoginActivity.this);
        StringRequest stringRequest= new StringRequest(Request.Method.POST, url,
                response -> {
                    try {
                        JSONObject obj = new JSONObject(response);
                        String msg = obj.getString("msg");
                        String status = obj.getString("status");


                        if(status.equals("success")){
                            JSONObject data = obj.getJSONObject("data");
                            String token = data.getString("token");
                            SharedPreferences preferences = LoginActivity.this.getApplicationContext().getSharedPreferences("tokenLogin", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = preferences.edit();
                            editor.putString("token", token);
                            editor.apply();
                            openHomePage();
                            StyleableToast.makeText(LoginActivity.this, "Đăng nhập thành công!", Toast.LENGTH_SHORT, R.style.toastBlueLight).show();
                        } else {
                            DialogError(Gravity.CENTER);
                        }
                    } catch (Throwable t) {
                        Toast.makeText(LoginActivity.this, "Could not parse malformed JSON: \"" + response + "\"", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> {
                    Log.d("AAA", "Lỗi:\n" + error.toString());
                    Toast.makeText(LoginActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
                }
        ){
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("username", edtUsername.getText().toString().trim());
                params.put("password", edtPassword.getText().toString().trim());
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }
    private void getData(String url){
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                response -> Toast.makeText(LoginActivity.this, response.toString(), Toast.LENGTH_SHORT).show(),
                error -> {
//                        Toast.makeText(LoginActivity.this, "Đăng nhập!", Toast.LENGTH_SHORT).show();
                }
        );
        requestQueue.add(jsonArrayRequest);
    }

    private void DialogError(int gravity) {
        Dialog dialog = new Dialog(LoginActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.login_error);

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

        Button btnCannel = dialog.findViewById(R.id.btn_ok);
        btnCannel.setOnClickListener(v -> dialog.dismiss());

    }

    private static void SetWindowFlag(Activity activity, final int Bits, Boolean on) {
        Window win =  activity.getWindow();
        WindowManager.LayoutParams Winparams = win.getAttributes();
        if (on) {
            Winparams.flags  |=Bits;
        } else {
            Winparams.flags &= ~Bits;
        }
        win.setAttributes(Winparams);

    }

    public void hideKeyBoard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}