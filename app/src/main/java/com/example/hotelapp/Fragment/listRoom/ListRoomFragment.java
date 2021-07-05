package com.example.hotelapp.Fragment.listRoom;

import android.content.Intent;
import android.os.Bundle;
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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.ui.AppBarConfiguration;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.hotelapp.Activities.Home;
import com.example.hotelapp.R;
import com.example.hotelapp.Model.Room;
import com.example.hotelapp.Adapters.RoomAdapter;
import com.example.hotelapp.Activities.RoomEdit;
import com.google.android.material.floatingactionbutton.FloatingActionButton;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ListRoomFragment extends Fragment {
    GridView gvRoom;
    ArrayList<Room> arrayRoom;
    RoomAdapter adapter;

   ActionBar actionBar;

    private ListRoomViewModel galleryViewModel;
    ImageView image;
    private AppBarConfiguration mAppBarConfiguration;
    String urlGetData = "http://192.168.60.1/severApp/rooms";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        setRetainInstance(true);

        Bundle bundle= getArguments();
        if(bundle!=null){
            String value= bundle.getString("key");
        }
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        galleryViewModel =
                new ViewModelProvider(this).get(ListRoomViewModel.class);
        View root = inflater.inflate(R.layout.fragment_room_list, container, false);
        final TextView textView = root.findViewById(R.id.text_gallery);
        ((Home) getActivity())
                .setActionBarTitle("Sơ đồ phòng");
//        image = root.findViewById(R.id.imageLR);
//        image.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment,
//                        new ListRoomFragment()).commit();
//            }
//        });
        galleryViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });

        FloatingActionButton fab = root.findViewById(R.id.add_room);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddRoomFragment addRoom= new AddRoomFragment();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.drawer_layout, addRoom, "Thêm Phòng")
                        .addToBackStack(null)
                        .commit();
            }
        });


        gvRoom = root.findViewById(R.id.gridViewRoom);
        arrayRoom = new ArrayList<>();
        adapter = new RoomAdapter(getActivity(), R.layout.room_item, arrayRoom);
        gvRoom.setAdapter(adapter);
        getData(urlGetData);
        gvRoom.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), RoomEdit.class);
                intent.putExtra("dataRoom", arrayRoom.get(position));
                startActivity(intent);
            }
        });

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
//    private void getData(String url){
//        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
//        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
//                new Response.Listener<JSONArray>() {
//                    @Override
//                    public void onResponse(JSONArray response) {
////                        Toast.makeText(getActivity(), response.toString(), Toast.LENGTH_SHORT).show();
//                        for (int i=0; i < response.length(); i++){
//                            try {
//                                JSONObject object = response.getJSONObject(i);
//                                arrayRoom.add(new Room(
//                                        object.getInt("id"),
//                                        object.getInt("tang"),
//                                        object.getString("tenPhong"),
//                                        object.getInt("gia"),
//                                        object.getInt("trangThai"),
//                                        object.getInt("tuSua")
//                                ));
//                            } catch (JSONException e) {
//                                e.printStackTrace();
//                            }
//                        }
////                        adapter.getData().clear();
////                        adapter.getData().addAll(arrayRoom);
//                        adapter.notifyDataSetChanged();
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
////                        Toast.makeText(getActivity(), error.toString(), Toast.LENGTH_SHORT).show();
//                    }
//                }
//
//
//        );
//        requestQueue.add(jsonArrayRequest);
//        refresh(1000);
//    }

    private void refresh(int ms) {
        final Handler handler = new Handler();

        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                getData(urlGetData);
            }
        };
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_add_room, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.menu_add_room) {
            AddRoomFragment addRoom= new AddRoomFragment();
            getActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.drawer_layout, addRoom, "Add")
                    .addToBackStack(null)
                    .commit();
            }
        return super.onOptionsItemSelected(item);
    }

}