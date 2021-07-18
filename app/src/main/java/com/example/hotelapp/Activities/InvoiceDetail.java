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
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.hotelapp.R;
import com.google.android.material.appbar.AppBarLayout;
import com.muddzdev.styleabletoast.StyleableToast;

import org.json.JSONObject;

import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class InvoiceDetail extends AppCompatActivity {
    EditText edtMaHD, edtSoPhong, edtTenKhachHang, edtCMND, edtSDT, edtDiaChi, edtDichVu, edtPhiDichVu, edtThanhToan, edtNgayTao;
    Button btnDeleteInvoice;
    AppBarLayout appBarLayout;
    public static Toolbar toolbar;
    int ID = 0;
    int permission;
    String urlDeleteInvoice =  "http://192.168.60.1/severApp/deletePay";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        Window window = getWindow();
        setContentView(R.layout.activity_invoice_detail);

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

        appBarLayout = (AppBarLayout) findViewById(R.id.appBarUpdateInvoice);
        toolbar = findViewById(R.id.toolbar_ER);
        toolbar.setTitle("Chi tiết hóa đơn");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        SharedPreferences preferences = this.getApplicationContext().getSharedPreferences("tokenLogin", Context.MODE_PRIVATE);
        permission = preferences.getInt("permission", 0);

        Intent intent = getIntent();

        //        Invoice invoice = (Invoice) intent.getSerializableExtra("dataInvoice");
        int id = (int) intent.getIntExtra("invoiceID", 0);
        int idPhong = (int) intent.getIntExtra("invoiceIDPhong", 0);
        int thanhtoan = (int) intent.getIntExtra("invoiceThanhToan", 0);
        String createDate = (String) intent.getStringExtra("invoiceCreateDate");
        String ten = (String) intent.getStringExtra("invoiceTenKhach");
        String cmnd = (String) intent.getStringExtra("invoiceCMNDKhach");
        String sdt = (String) intent.getStringExtra("invoiceSDTKhach");
        String diachi = (String) intent.getStringExtra("invoiceDiaChiKhach");
        String dichvu = (String) intent.getStringExtra("invoiceDichVu");
        int phidichvu = (int) intent.getIntExtra("invoicePhiDichVu", 0);
//        Toast.makeText(InvoiceDetail.this, String.valueOf(id), Toast.LENGTH_SHORT).show();

        getDataInvoice();
        ID = id;
        edtMaHD.setText("" + id);
        edtSoPhong.setText("Phòng "+idPhong);
        edtTenKhachHang.setText(""+ ten);
        edtCMND.setText(""+cmnd);
        edtSDT.setText(""+sdt);
        edtDiaChi.setText(""+diachi);
        edtDichVu.setText(""+dichvu);
        edtPhiDichVu.setText(""+ NumberFormat.getNumberInstance(Locale.US).format(phidichvu));
        edtThanhToan.setText(""+ NumberFormat.getNumberInstance(Locale.US).format(thanhtoan));
        edtNgayTao.setText(""+ createDate);

        btnDeleteInvoice.setOnClickListener(v -> {
            confirmDeleteInvoice(String.valueOf(id));
        });
    }

    private void getDataInvoice() {
        btnDeleteInvoice = findViewById(R.id.btn_delete_invoice);
        edtMaHD = findViewById(R.id.edtMaHD);
        edtSoPhong = findViewById(R.id.edtSoPhong);
        edtTenKhachHang = findViewById(R.id.edtTenKhachHang);
        edtCMND = findViewById(R.id.edtCMND);
        edtSDT = findViewById(R.id.edtSoDienThoai);
        edtDiaChi = findViewById(R.id.edtDiaChi);
        edtDichVu = findViewById(R.id.edtDichVuSuDung);
        edtPhiDichVu = findViewById(R.id.edtPhiDichVu);
        edtThanhToan = findViewById(R.id.edtThanhToan);
        edtNgayTao = findViewById(R.id.edtNgayTao);
    }

    public void confirmDeleteInvoice(String maHoaDon){
        AlertDialog.Builder dialogDelInvoice = new AlertDialog.Builder(InvoiceDetail.this);
        dialogDelInvoice.setMessage("Xác nhận xóa hóa đơn "+ maHoaDon + " ?");
        dialogDelInvoice.setNegativeButton("Có", (dialog, which) -> {
            SharedPreferences preferences = InvoiceDetail.this.getApplicationContext().getSharedPreferences("tokenLogin", Context.MODE_PRIVATE);
            String token = preferences.getString("token", "");
            deleteInvoice(urlDeleteInvoice, token);
        });
        dialogDelInvoice.setPositiveButton("Không", (dialog, which) -> {

        });
        dialogDelInvoice.show();
    }
    public void deleteInvoice(String url, String token){
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest= new StringRequest(Request.Method.POST, url,
                response -> {
                    try {
                        JSONObject obj = new JSONObject(response);
                        String status = obj.getString("status");
                        String msg = obj.getString("msg");
                        if(status.equals("success")){
                            StyleableToast.makeText(InvoiceDetail.this, "Xóa hóa đơn thành công!", Toast.LENGTH_SHORT, R.style.toastSuccess2).show();
                            Intent intent = new Intent(InvoiceDetail.this, Home.class);
                            intent.putExtra("invoice", "invoice");
                            startActivity(intent);
                        } else {
                            StyleableToast.makeText(InvoiceDetail.this, msg, Toast.LENGTH_SHORT, R.style.toastStyle).show();
                        }
                    } catch (Throwable t) {
                        StyleableToast.makeText(InvoiceDetail.this, "Kiểm tra lại quyền chỉnh sửa!", Toast.LENGTH_SHORT, R.style.toastStyle).show();
                    }
                },
                error -> Toast.makeText(InvoiceDetail.this, error.toString(), Toast.LENGTH_SHORT).show()
        ){
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("token",token);
                params.put("ID",String.valueOf(ID));
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }
    private static void SetWindowFlag(InvoiceDetail invoiceDetail, final int Bits, Boolean on) {
        Window win =  invoiceDetail.getWindow();
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