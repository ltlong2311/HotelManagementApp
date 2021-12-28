package com.example.hotelapp.Fragment.manage;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;


import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.hotelapp.API.BaseUrl;
import com.example.hotelapp.Activities.Home;
import com.example.hotelapp.Fragment.home.HomeFragment;
import com.example.hotelapp.Fragment.invoice.InvoiceFragment;
import com.example.hotelapp.Fragment.listRoom.AddRoomFragment;
import com.example.hotelapp.LoginActivity;
import com.example.hotelapp.R;
import com.google.android.material.appbar.AppBarLayout;
import com.muddzdev.styleabletoast.StyleableToast;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ChangePasswordFragment extends Fragment {

    AppBarLayout appBarLayout;
    Toolbar toolbar;
    EditText oldPass, newPass, retypePass;
    Button btnChangePassword;
    RelativeLayout relativeLayout;
    ScrollView scrollView;
    BaseUrl baseUrl = new BaseUrl();
    String urlVerifyPass = baseUrl.getBaseURL()+ "/verifyPassword";
    String urlChangePass = baseUrl.getBaseURL()+ "/renewPassword";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_change_password, container, false);

        oldPass = root.findViewById(R.id.editTextOldPassword);
        newPass = root.findViewById(R.id.editTextNewPassword);
        retypePass = root.findViewById(R.id.editTextRetypePassword);
        btnChangePassword = root.findViewById(R.id.btn_change_password);
        scrollView = root.findViewById(R.id.scrollViewCP);
        relativeLayout = root.findViewById(R.id.changePassword);
        relativeLayout.setOnClickListener(this::hideKeyBoard);
        scrollView.setOnClickListener(this::hideKeyBoard);
        SharedPreferences preferences = getActivity().getApplicationContext().getSharedPreferences("tokenLogin", Context.MODE_PRIVATE);
        String token = preferences.getString("token", "");
        btnChangePassword.setOnClickListener((View v) -> {
            verifyPassword(urlVerifyPass, token);
            hideKeyBoard(v);
        });
        return root;
    }

    public void verifyPassword(String url, String token){
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        StringRequest stringRequest= new StringRequest(Request.Method.POST, url,
                response -> {
                    try {
                        JSONObject obj = new JSONObject(response);
                        String status = obj.getString("status");
                        String msg = obj.getString("msg");
                        if(status.equals("success")){
                           changePassword(urlChangePass, token);
                        } else {
                            StyleableToast.makeText(getActivity(), msg, Toast.LENGTH_SHORT, R.style.toastStyle).show();
                        }
                    } catch (Throwable t) {
                        StyleableToast.makeText(getActivity(), "Kiểm tra lại quyền chỉnh sửa!", Toast.LENGTH_SHORT, R.style.toastStyle).show();
                    }
                },
                error -> Toast.makeText(getActivity(), error.toString(), Toast.LENGTH_SHORT).show()
        ){
            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<>();
                params.put("token", String.valueOf(token));
                params.put("password", oldPass.getText().toString().trim());

                return params;
            }
        };
        requestQueue.add(stringRequest);
    }
    public void changePassword(String url, String token){
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        StringRequest stringRequest= new StringRequest(Request.Method.POST, url,
                response -> {
                    try {
                        JSONObject obj = new JSONObject(response);
                        String status = obj.getString("status");
                        String msg = obj.getString("msg");
                        if(status.equals("success")){
                            StyleableToast.makeText(getActivity(), "Đổi mật khẩu thành công, hãy đăng nhập lại!", Toast.LENGTH_SHORT, R.style.toastBlueLight).show();
                            SharedPreferences preferences = getActivity().getApplicationContext().getSharedPreferences("tokenLogin", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = preferences.edit();
                            editor.remove("token");
                            editor.remove("permission");
                            editor.apply();
                            Intent intent = new Intent(getActivity().getApplicationContext(), LoginActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                        } else {
                            StyleableToast.makeText(getActivity(), msg, Toast.LENGTH_SHORT, R.style.toastStyle).show();
                        }
                    } catch (Throwable t) {
                        StyleableToast.makeText(getActivity(), t.toString(), Toast.LENGTH_SHORT, R.style.toastStyle).show();
                    }
                },
                error -> Toast.makeText(getActivity(), error.toString(), Toast.LENGTH_SHORT).show()
        ){
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("token", String.valueOf(token));
                params.put("password", newPass.getText().toString().trim());
                params.put("cfPassword",retypePass.getText().toString().trim());
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }
    private void hideKeyBoard(View v) {
        InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }

}