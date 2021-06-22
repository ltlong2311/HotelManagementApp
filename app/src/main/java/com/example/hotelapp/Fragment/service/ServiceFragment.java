package com.example.hotelapp.Fragment.service;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.hotelapp.Activities.Home;
import com.example.hotelapp.R;
import com.example.hotelapp.Model.Service;
import com.example.hotelapp.Adapters.ServiceAdapter;
import com.example.hotelapp.Activities.ServiceEdit;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

public class ServiceFragment extends Fragment {

    DrawerLayout drawerLayout;
    String urlGetData = "http://192.168.60.1/severApp/services";

    ListView lvService;
    ArrayList<Service> arrayService;
    ServiceAdapter adapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_service, container, false);
        ((Home) getActivity())
                .setActionBarTitle("Dịch vụ");
        lvService = root.findViewById(R.id.listViewService);
        arrayService = new ArrayList<>();
        adapter = new ServiceAdapter(getActivity(), R.layout.service_item, arrayService);
        lvService.setAdapter(adapter);
        getData(urlGetData);

        lvService.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), ServiceEdit.class);
                intent.putExtra("dataService", (Serializable) arrayService.get(position));
                startActivity(intent);
            }
        });

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
//                                Toast.makeText(getActivity(), data.getString("TenDV"), Toast.LENGTH_SHORT).show();
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
    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_add_service, menu);
        super.onCreateOptionsMenu(menu, inflater);


    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.menu_add_service) {
            AddServiceFragment add= new AddServiceFragment();
            getActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.drawer_layout, add)
                    .addToBackStack(null)
                    .commit();
        }
        return super.onOptionsItemSelected(item);
    }
}