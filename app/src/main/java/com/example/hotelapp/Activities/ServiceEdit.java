package com.example.hotelapp.Activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
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
import com.example.hotelapp.Adapters.ServiceAdapter;
import com.example.hotelapp.Fragment.service.AddServiceFragment;
import com.example.hotelapp.Fragment.service.ServiceFragment;
import com.example.hotelapp.R;
import com.example.hotelapp.Model.Service;
import com.google.android.material.appbar.AppBarLayout;
import com.muddzdev.styleabletoast.StyleableToast;

import org.json.JSONObject;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class ServiceEdit extends AppCompatActivity {
    EditText edtTenDV, edtGiaDV;
    RadioButton rbnPhucVu, rbnNgungPhucVu;
    Button btnUpdateService, btnDeleteService;
    AppBarLayout appBarLayout;
    Toolbar toolbar;
    int ID = 0;
    int isActive;


    String urlUpdateService =  "http://192.168.60.1/severApp/updateServices";
    String urlDeleteService =  "http://192.168.60.1/severApp/deleteServices";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

        appBarLayout = findViewById(R.id.appBarUpdateService);
        toolbar = findViewById(R.id.toolbar_ER);
        toolbar.setTitle("Thông tin dịch vụ");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        Intent intent = getIntent();
        Service service = (Service) intent.getSerializableExtra("dataService");

        getDataService();
        ID = service.getID();
        isActive = service.getTrangThai();
        edtTenDV.setText(service.getTenDV());
        edtGiaDV.setText(String.valueOf(service.getGia()));
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
                    StyleableToast.makeText(ServiceEdit.this, "Vui lòng điền đẩy đủ thông tin!", Toast.LENGTH_SHORT, R.style.toastStyle).show();
                } else  {
                    SharedPreferences preferences = ServiceEdit.this.getApplicationContext().getSharedPreferences("tokenLogin", Context.MODE_PRIVATE);
                    String token = preferences.getString("token", "");
                    UpdateService(urlUpdateService, token);
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

    public void UpdateService(String url, String token){
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest= new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            String status = obj.getString("status");
                            String msg = obj.getString("msg");
                            if(status.equals("success")){
                                StyleableToast.makeText(ServiceEdit.this, "Cập nhật dịch vụ thành công!", Toast.LENGTH_SHORT, R.style.toastBlueLight).show();
                                Intent intent = new Intent(ServiceEdit.this, Home.class);
                                intent.putExtra("service", "service");
                                startActivity(intent);
                            } else {
                                StyleableToast.makeText(ServiceEdit.this, msg, Toast.LENGTH_SHORT, R.style.toastStyle).show();
                            }
                        } catch (Throwable t) {
                            StyleableToast.makeText(ServiceEdit.this, "Kiểm tra lại quyền chỉnh sửa!", Toast.LENGTH_SHORT, R.style.toastStyle).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        StyleableToast.makeText(ServiceEdit.this, error.toString(), Toast.LENGTH_SHORT, R.style.toastError).show();
                    }
                }
        ){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<>();
                params.put("token",token);
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
                SharedPreferences preferences = ServiceEdit.this.getApplicationContext().getSharedPreferences("tokenLogin", Context.MODE_PRIVATE);
                String token = preferences.getString("token", "");
                deleteService(urlDeleteService, token);
            }
        });
        dialogDelService.setPositiveButton("Không", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        dialogDelService.show();
    }
    public void deleteService(String url, String token){
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest= new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            String status = obj.getString("status");
                            String msg = obj.getString("msg");
                            if(status.equals("success")){
                                StyleableToast.makeText(ServiceEdit.this, "Xóa dịch vụ thành công!", Toast.LENGTH_SHORT, R.style.toastSuccess2).show();
                                Intent intent = new Intent(ServiceEdit.this, Home.class);
                                intent.putExtra("service", "service");
                                startActivity(intent);
                            } else {
                                StyleableToast.makeText(ServiceEdit.this, msg, Toast.LENGTH_SHORT, R.style.toastStyle).show();
                            }
                        } catch (Throwable t) {
                            StyleableToast.makeText(ServiceEdit.this, "Kiểm tra lại quyền chỉnh sửa!", Toast.LENGTH_SHORT, R.style.toastStyle).show();
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
                params.put("token",token);
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