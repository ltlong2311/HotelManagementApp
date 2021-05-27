package com.example.hotelapp.Fragment.listRoom;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.ui.AppBarConfiguration;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.hotelapp.R;
import com.example.hotelapp.Room;
import com.example.hotelapp.RoomAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ListRoomFragment extends Fragment {
    GridView gvRoom;
    ArrayList<Room> arrayRoom;
    RoomAdapter adapter;

    private ListRoomViewModel galleryViewModel;
    private AppBarConfiguration mAppBarConfiguration;
    String urlGetData = "http://192.168.1.3/severApp/products";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);

    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        galleryViewModel =
                new ViewModelProvider(this).get(ListRoomViewModel.class);
        View root = inflater.inflate(R.layout.fragment_room_list, container, false);
        final TextView textView = root.findViewById(R.id.text_gallery);

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
                Snackbar.make(view, "Add room", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Thêm phòng", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        gvRoom = root.findViewById(R.id.gridViewRoom);
        arrayRoom = new ArrayList<>();
        adapter = new RoomAdapter(getActivity(), R.layout.room_row, arrayRoom);
        gvRoom.setAdapter(adapter);

        getData(urlGetData);
        return root;
    }

    private void getData(String url){
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
//                        Toast.makeText(getActivity(), response.toString(), Toast.LENGTH_SHORT).show();
                        for (int i=0; i < response.length(); i++){
                            try {
                                JSONObject object = response.getJSONObject(i);
                                arrayRoom.add(new Room(
                                        object.getInt("id"),
                                        object.getInt("tang"),
                                        object.getString("tenPhong"),
                                        object.getInt("gia"),
                                        object.getInt("trangThai"),
                                        object.getInt("tuSua")
                                ));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        adapter.notifyDataSetChanged();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
//                        Toast.makeText(getActivity(), error.toString(), Toast.LENGTH_SHORT).show();
                    }
                }


        );
        requestQueue.add(jsonArrayRequest);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_add, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        return super.onOptionsItemSelected(item);
    }
}