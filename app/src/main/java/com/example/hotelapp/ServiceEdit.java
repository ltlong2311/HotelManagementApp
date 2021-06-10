package com.example.hotelapp;

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
import com.google.android.material.appbar.AppBarLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ServiceEdit extends AppCompatActivity {
    EditText edtTenDV, edtGiaDV;
    RadioButton rbnPhucVu, rbnNgungPhucVu;
    Button btnUpdateService, btnDeleteService;
    AppBarLayout appBarLayout;

    public static Toolbar toolbar;
    int ID = 0;
    int isActive = 0;
    ArrayList<Service> arrayService;
    ServiceAdapter adapter;


    String tokenAdmin = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJJRCI6IjEiLCJ1c2VyTmFtZSI6ImFkbWluIiwiSG9UZW4iOiJhZG1pbiJ9.234-aSxbQPO_Ozd4kcffsavH1FRWBgBx61dga5ZrAWE";
    String urlUpdateService =  "http://192.168.60.1/severApp/updateServices";
    String urlDeleteService =  "http://192.168.60.1/severApp/deleteServices";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window window = getWindow();
        setContentView(R.layout.activity_service_edit);

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

        appBarLayout = (AppBarLayout) findViewById(R.id.appBarUpdateService);
        setContentView(R.layout.activity_service_edit);
        toolbar = findViewById(R.id.toolbar_ER);
        toolbar.setTitle("Thông tin dịch vụ");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        Intent intent = getIntent();
        Service service = (Service) intent.getSerializableExtra("dataService");


        getDataService();
        ID = service.getID();
//        Status = service.getTrangThai();
        Toast.makeText(this, String.valueOf(service.getID()), Toast.LENGTH_SHORT).show();
        edtTenDV.setText(service.getTenDV());
        edtGiaDV.setText(""+service.getGia());
        if(service.getTrangThai() == 1){
            rbnPhucVu.setChecked(true);
        } else {
            rbnNgungPhucVu.setChecked(true);
        }

        rbnPhucVu.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)  {
                    isActive = 1;
                } else {
                    isActive = 0;
                }
            }
        });

        btnUpdateService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String tenDV = edtTenDV.getText().toString().trim();
                String giaDV = edtGiaDV.getText().toString().trim();

                if (tenDV.matches("") || giaDV.length() == 0){
                    Toast.makeText(ServiceEdit.this, "Vui lòng điền đẩy đủ thông tin!", Toast.LENGTH_SHORT).show();
                } else  {
                    UpdateService(urlUpdateService);
                    Intent intent = new Intent(ServiceEdit.this, Home.class);
                    intent.putExtra("service", "service");
                    startActivity(intent);
                }
            }
        });
        btnDeleteService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmDeleteService(service.getTenDV());
            }
        });
    }

    private void getDataService() {
        btnUpdateService = (Button) findViewById(R.id.btn_update_service);
        btnDeleteService = (Button) findViewById(R.id.btn_delete_service);
        edtTenDV = (EditText) findViewById(R.id.edtUpdateTenDV);
        edtGiaDV = (EditText) findViewById(R.id.edtUpdateGiaDV);
        rbnPhucVu = (RadioButton) findViewById(R.id.rbnServiceIsActive);
        rbnNgungPhucVu = (RadioButton) findViewById(R.id.rbnServiceIsStopServing);
    }

    public void UpdateService(String url){
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest= new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

//                        if (response.trim().equals("401")){
//                            Toast.makeText(ServiceEdit.this, "Lỗi cập nhật", Toast.LENGTH_SHORT).show();
//                        } else  {
//                            Toast.makeText(ServiceEdit.this, "Cập nhật thành công!", Toast.LENGTH_SHORT).show();
//                            finish();
//                        }
                          Toast.makeText(ServiceEdit.this, response.toString(), Toast.LENGTH_SHORT).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(ServiceEdit.this, error.toString(), Toast.LENGTH_SHORT).show();
                    }
                }
        ){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<>();
                params.put("token",tokenAdmin);
                params.put("ID", String.valueOf(ID));
                params.put("TenDV",edtTenDV.getText().toString().trim());
                params.put("Gia",edtGiaDV.getText().toString().trim());
                params.put("TrangThai",String.valueOf(isActive));
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }

    public void confirmDeleteService(String tenDV){
        AlertDialog.Builder dialogDelService = new AlertDialog.Builder(ServiceEdit.this);
        dialogDelService.setMessage("Xác nhận xóa dịch vụ "+ tenDV + " ?");
        dialogDelService.setNegativeButton("Có", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deleteService(urlDeleteService);
            }
        });
        dialogDelService.setPositiveButton("Không", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        dialogDelService.show();
    }
    public void deleteService(String url){
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest= new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.trim().equals("404")){
                            Toast.makeText(ServiceEdit.this, "Lỗi xóa đối tượng", Toast.LENGTH_SHORT).show();
                        } else  {
                            Toast.makeText(ServiceEdit.this, "Xóa thành công!", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(ServiceEdit.this, Home.class);
                            intent.putExtra("service", "service");
                            startActivity(intent);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(ServiceEdit.this, error.toString(), Toast.LENGTH_SHORT).show();
                    }
                }
        ){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("token",tokenAdmin);
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
    private static void SetWindowFlag(ServiceEdit serviceEdit, final int Bits, Boolean on) {
        Window win =  serviceEdit.getWindow();
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