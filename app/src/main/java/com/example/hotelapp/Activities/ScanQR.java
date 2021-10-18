package com.example.hotelapp.Activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.hotelapp.API.BaseUrl;
import com.example.hotelapp.Fragment.invoice.InvoiceFragment;
import com.example.hotelapp.Fragment.listRoom.Capture;
import com.example.hotelapp.Fragment.listRoom.ListRoomFragment;
import com.example.hotelapp.LoginActivity;
import com.example.hotelapp.R;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.example.hotelapp.Adapters.RoomAdapter.SHARED_ID_INVOICE;

public class ScanQR extends AppCompatActivity {
    BaseUrl baseUrl = new BaseUrl();
    String urlCheckIn = baseUrl.getBaseURL() + "/createPay";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_q_r);
        Button btnScan = findViewById(R.id.btnScan);
        btnScan.setOnClickListener(v -> {
            IntentIntegrator intentIntegrator = new IntentIntegrator(
                    ScanQR.this
            );
            intentIntegrator.setPrompt("flash use volume up key");
            intentIntegrator.setBeepEnabled(true);
            intentIntegrator.setOrientationLocked(true);
            intentIntegrator.setCaptureActivity(Capture.class);
            intentIntegrator.initiateScan();
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        IntentResult intentResult = IntentIntegrator.parseActivityResult(
                requestCode,resultCode,data
        );

        if (intentResult.getContents() != null) {
            SharedPreferences preferences = ScanQR.this.getApplicationContext().getSharedPreferences("tokenLogin", Context.MODE_PRIVATE);
            String token = preferences.getString("token", "");
            checkIn(urlCheckIn, intentResult.getContents(), token);
            AlertDialog.Builder builder = new AlertDialog.Builder(ScanQR.this);
            builder.setTitle("Result");
            builder.setMessage("Check-in Success!");
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent(ScanQR.this, Home.class);
                    intent.putExtra("url", "url");
                    startActivity(intent);
                    dialog.dismiss();
                }
            });
            builder.show();
            Toast.makeText(ScanQR.this, intentResult.getContents(), Toast.LENGTH_SHORT).show();
        } else  {
            //when result content is  null
            Toast.makeText(ScanQR.this.getApplicationContext(), "none", Toast.LENGTH_SHORT).show();
        }
    }

    private void checkIn(String url, String qrcode, String token) {
        RequestQueue requestQueue = Volley.newRequestQueue(ScanQR.this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                response -> {
                    try {
                        JSONObject obj = new JSONObject(response);
                        String msg = obj.getString("msg");
                        String status = obj.getString("status");
                        JSONObject dataID = obj.getJSONObject("data");
                        int idInvoice = dataID.getInt("ID");
                        int idPhong = dataID.getInt("IDPhong");
                        SharedPreferences preferences = this.getApplicationContext().getSharedPreferences(SHARED_ID_INVOICE, Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putInt(String.valueOf(idPhong), idInvoice);
                        editor.apply();
                        if (status.equals("success")) {
                            Toast.makeText(ScanQR.this, msg, Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(ScanQR.this, msg, Toast.LENGTH_SHORT).show();
                        }

                    } catch (Throwable t) {
                        Toast.makeText(ScanQR.this, "Could not parse malformed JSON: \"" + response + "\"", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> {
                    Log.d("AAA", "Lỗi:\n" + error.toString());
                    Toast.makeText(ScanQR.this, error.toString(), Toast.LENGTH_SHORT).show();
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("token", token);
                params.put("qrcode", qrcode);
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }
}