package com.example.hotelapp.Fragment.listRoom;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
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
import com.example.hotelapp.R;

import java.util.HashMap;
import java.util.Map;


public class AddRoomFragment extends Fragment{
    LinearLayout layoutHideKeyboard;
    EditText edtTenPhong, edtTang, edtGiaPhong;
    Button btnThemPhong, btnHuy;

    String urlAddRoom = "http://192.168.60.1/serverApp/createRoom.php";

    @Override
    public void onCreate(Bundle savedInstanceState) {

        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_add_room, container, false);
        btnHuy = root.findViewById(R.id.btn_back);
        btnThemPhong = root.findViewById(R.id.btn_add_room);
        edtTenPhong= (EditText) root.findViewById(R.id.editTextTenPhong);
        edtTang= (EditText) root.findViewById(R.id.editTextTang);
        edtGiaPhong= (EditText) root.findViewById(R.id.editTextGiaPhong);
        btnHuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                getActivity().onBackPressed();
//                getFragmentManager().popBackStack();     //back to home, sau do mới thoát
                getFragmentManager().beginTransaction().remove(AddRoomFragment.this).commit();
            }
        });

        btnThemPhong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tenPhong = edtTenPhong.getText().toString().trim();
                String tang = edtTang.getText().toString().trim();
                String giaPhong = edtGiaPhong.getText().toString().trim();
                  if (tenPhong.isEmpty() || tang.isEmpty() || giaPhong.isEmpty()){
                      Toast.makeText(getActivity(), "Vui lòng nhập đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
                      hideKeyBoard(v);
                  } else if (Integer.parseInt(tang) > 10){
                    Toast.makeText(getActivity(), "Hãy nhập đúng số tầng!", Toast.LENGTH_SHORT).show();
                    hideKeyBoard(v);
                  } else {
                      ThemPhong(urlAddRoom);
                      hideKeyBoard(v);
                  }
            }
        });

        layoutHideKeyboard = (LinearLayout) root.findViewById(R.id.layoutHideKeyboard);
        layoutHideKeyboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(v.getWindowToken(), 0);
            }
        });

        return root;
    }

    private void ThemPhong(String url){
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        StringRequest stringRequest= new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.trim().equals("success")){
                            Toast.makeText(getActivity(), "Thêm phòng thành công!", Toast.LENGTH_SHORT).show();
                            getFragmentManager().beginTransaction().remove(AddRoomFragment.this).commit();
                            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment,
                                    new ListRoomFragment()).commit();
                        } else  {

                            Toast.makeText(getActivity(), "Error", Toast.LENGTH_SHORT).show();
                            getFragmentManager().beginTransaction().remove(AddRoomFragment.this).commit();
                        }
//                        adapter.notifyDataSetChanged(); // reload lại danh sách phòng
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("AAA", "Lỗi:\n" + error.toString());
                        Toast.makeText(getActivity(), error.toString(), Toast.LENGTH_SHORT).show();
                    }
                }
        ){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("IDTang",edtTang.getText().toString().trim());
                params.put("TenPhong",edtTenPhong.getText().toString().trim());
                params.put("Gia",edtGiaPhong.getText().toString().trim());
                params.put("TrangThai","0");
                params.put("TuSua","0");
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }
    private void hideKeyBoard(View v) {
        InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }

//    private void Map() {
//        btnThemPhong = (Button) findViewById(R.id.btn_add_room);
//    }

}