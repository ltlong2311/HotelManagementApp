package com.example.hotelapp.Fragment.service;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.hotelapp.API.BaseUrl;
import com.example.hotelapp.Activities.Home;
import com.example.hotelapp.LoginActivity;
import com.example.hotelapp.R;
import com.muddzdev.styleabletoast.StyleableToast;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class AddServiceFragment extends Fragment {
    LinearLayout layoutHideKeyboard;
    EditText edtTenDV, edtGiaDV;
    Button btnThemDV, btnHuy;
    BaseUrl baseUrl = new BaseUrl();
    String urlAddService = baseUrl.getBaseURL()+ "/createServices";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_add_service, container, false);
        btnHuy = root.findViewById(R.id.btn_back);
        btnThemDV = root.findViewById(R.id.btn_add_room);
        edtTenDV= root.findViewById(R.id.editTextTenDichVu);
        edtGiaDV= root.findViewById(R.id.editTextGiaDichVu);
        btnHuy.setOnClickListener(view -> {
            getFragmentManager().beginTransaction().remove(AddServiceFragment.this).commit();
            hideKeyBoard(view);
        });

        btnThemDV.setOnClickListener(v -> {
            String tendv = edtTenDV.getText().toString().trim();
            String giadv = edtGiaDV.getText().toString().trim();
            if (tendv.isEmpty() || giadv.isEmpty()){
                StyleableToast.makeText(getActivity(), "Vui lòng nhập đầy đủ thông tin!", Toast.LENGTH_SHORT, R.style.toastStyle).show();
            } else {
                SharedPreferences preferences = getActivity().getApplicationContext().getSharedPreferences("tokenLogin", Context.MODE_PRIVATE);
                String token = preferences.getString("token", "");
                ThemDV(urlAddService, token);
                hideKeyBoard(v);
            }
        });

        layoutHideKeyboard = (LinearLayout) root.findViewById(R.id.layoutHideKeyboard);
        layoutHideKeyboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyBoard(v);
            }
        });

        return root;
    }

    private void hideKeyBoard(View v) {
        InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }

    private void ThemDV(String url, String token) {
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        StringRequest stringRequest= new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            String status = obj.getString("status");
                            String msg = obj.getString("msg");
                            if(status.equals("success")){
                                StyleableToast.makeText(getActivity(), "Thêm dịch vụ thành công!", Toast.LENGTH_SHORT, R.style.toastSuccess2).show();
                                getFragmentManager().beginTransaction().remove(AddServiceFragment.this).commit();
                                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment,
                                        new ServiceFragment()).commit();
                            } else {
                                StyleableToast.makeText(getActivity(), msg, Toast.LENGTH_SHORT, R.style.toastStyle).show();
                            }
                        } catch (Throwable t) {
                            StyleableToast.makeText(getActivity(), "Kiểm tra lại quyền chỉnh sửa!", Toast.LENGTH_SHORT, R.style.toastStyle).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("AAA", "Lỗi:\n" + error.toString());
                        Toast.makeText(getActivity(), error.toString(), Toast.LENGTH_SHORT).show();
                        StyleableToast.makeText(getActivity(), "Bạn không có quyền chỉnh sửa", Toast.LENGTH_SHORT, R.style.toastStyle).show();
                    }
                }
        ){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("token",token);
                params.put("TenDV",edtTenDV.getText().toString().trim());
                params.put("Gia",edtGiaDV.getText().toString().trim());
                params.put("TrangThai","1");
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }
}