package com.example.hotelapp.Fragment.receipt;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.hotelapp.R;
import com.example.hotelapp.Service;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class ReceiptFragment extends Fragment {

    String urlGetData = "http://192.168.1.107/severApp/pays";



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_service, container, false);

        getData(urlGetData);
        return root;
    }



    private void getData(String urlGetData) {
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, urlGetData, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("data");
                            Toast.makeText(getActivity(), response.toString(), Toast.LENGTH_SHORT).show();
                            for (int i = 0; i < jsonArray.length(); i++){
                                JSONObject data = jsonArray.getJSONObject(i);
//                                arrayService.add(new Service(
//                                        data.getInt("ID"),
//                                        data.getString("TenDV"),
//                                        data.getInt("Gia"),
//                                        data.getInt("TrangThai")
//                                ));

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
//                        adapter.notifyDataSetChanged();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        Toast.makeText(getActivity(), error.toString(), Toast.LENGTH_SHORT).show();
                    }
                }
        );
        requestQueue.add(request);
    }
}