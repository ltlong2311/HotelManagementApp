package com.example.hotelapp.Fragment.service;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.ui.AppBarConfiguration;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.hotelapp.R;
import com.example.hotelapp.Room;
import com.example.hotelapp.RoomAdapter;
import com.example.hotelapp.Service;
import com.example.hotelapp.ServiceAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ServiceFragment extends Fragment {

    DrawerLayout drawerLayout;
    String urlGetData = "http://192.168.1.3/severApp/services";

    ListView lvService;
    ArrayList<Service> arrayService;
    ServiceAdapter adapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_service, container, false);


        lvService = root.findViewById(R.id.listViewService);
        arrayService = new ArrayList<>();
        adapter = new ServiceAdapter(getActivity(), R.layout.service_row, arrayService);
        lvService.setAdapter(adapter);
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
//                            Toast.makeText(getActivity(), response.toString(), Toast.LENGTH_SHORT).show();
//                            Toast.makeText(getActivity(), jsonArray.getJSONObject(1).getString("TenDV"), Toast.LENGTH_SHORT).show();
                            for (int i = 0; i < jsonArray.length(); i++){
                                  JSONObject data = jsonArray.getJSONObject(i);
                                  arrayService.add(new Service(
                                          data.getInt("ID"),
                                          data.getString("TenDV"),
                                          data.getInt("Gia"),
                                          data.getInt("TrangThai")
                                  ));
                                Toast.makeText(getActivity(), data.getString("TenDV"), Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        adapter.notifyDataSetChanged();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
//                        Toast.makeText(getActivity(), error.toString(), Toast.LENGTH_SHORT).show();
                    }
                }
        );
        requestQueue.add(request);
    }
}