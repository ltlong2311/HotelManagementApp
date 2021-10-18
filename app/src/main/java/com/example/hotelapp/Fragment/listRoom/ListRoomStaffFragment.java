package com.example.hotelapp.Fragment.listRoom;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.fragment.app.Fragment;
import androidx.navigation.ui.AppBarConfiguration;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.hotelapp.API.BaseUrl;
import com.example.hotelapp.Activities.Home;
import com.example.hotelapp.Activities.RoomEdit;
import com.example.hotelapp.Adapters.RoomAdapter;
import com.example.hotelapp.Model.Room;
import com.example.hotelapp.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class ListRoomStaffFragment extends Fragment {

    GridView gvRoom;
    ArrayList<Room> arrayRoom;
    RoomAdapter adapter;

    ActionBar actionBar;

    ImageView image;
    private AppBarConfiguration mAppBarConfiguration;
    BaseUrl baseUrl = new BaseUrl();
    String urlGetData = baseUrl.getBaseURL()+ "/rooms";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        setRetainInstance(true);

        Bundle bundle= getArguments();
        if(bundle!=null){
            String value= bundle.getString("key");
        }
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_list_room_staff, container, false);

        final TextView textView = root.findViewById(R.id.text_gallery);
        ((Home) getActivity())
                .setActionBarTitle("Sơ đồ phòng");

        gvRoom = root.findViewById(R.id.gridViewRoom);
        arrayRoom = new ArrayList<>();
        adapter = new RoomAdapter(getActivity(), R.layout.room_item, arrayRoom);
        gvRoom.setAdapter(adapter);
        getData(urlGetData);
        return root;
    }
    private void getData(String url){
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("data");
//                            Toast.makeText(getActivity(), response.toString(), Toast.LENGTH_SHORT).show();
                            for (int i = 0; i < jsonArray.length(); i++){
                                JSONObject data = jsonArray.getJSONObject(i);
                                arrayRoom.add(new Room(
                                        data.getInt("ID"),
                                        data.getInt("IDPhong"),
                                        data.getString("TenPhong"),
                                        data.getInt("Gia"),
                                        data.getInt("TrangThai"),
                                        data.getInt("TuSua")
                                ));
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