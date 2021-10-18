package com.example.hotelapp.Fragment.listRoom;

import android.content.DialogInterface;
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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
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
import com.example.hotelapp.API.BaseUrl;
import com.example.hotelapp.Activities.CreateStaffAccount;
import com.example.hotelapp.Activities.Home;
import com.example.hotelapp.Activities.ScanQR;
import com.example.hotelapp.R;
import com.example.hotelapp.Model.Room;
import com.example.hotelapp.Adapters.RoomAdapter;
import com.example.hotelapp.Activities.RoomEdit;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
//import com.google.zxing.integration.android.IntentIntegrator;
//import com.google.zxing.integration.android.IntentResult;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ListRoomFragment extends Fragment {
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
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        setRetainInstance(true);
        Bundle bundle = getArguments();
        if (bundle != null) {
            String value = bundle.getString("key");
        }
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_room_list, container, false);
        final TextView textView = root.findViewById(R.id.text_gallery);
        ((Home) getActivity())
                .setActionBarTitle("Sơ đồ phòng");

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
        ImageView checkInQR = root.findViewById(R.id.checkInQR);
        checkInQR.setOnClickListener(view -> {
            Intent intent = new Intent(getActivity(), ScanQR.class);
            startActivity(intent);
        });
        return root;
    }

    private void getData(String url) {
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("data");
//                            Toast.makeText(getActivity(), response.toString(), Toast.LENGTH_SHORT).show();
                            for (int i = 0; i < jsonArray.length(); i++) {
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
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        IntentResult intentResult = IntentIntegrator.parseActivityResult(
                requestCode,resultCode,data
        );

        if (intentResult.getContents() != null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle("Result");
            builder.setMessage(intentResult.getContents());
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            builder.show();
            Toast.makeText(getActivity(), intentResult.getContents(), Toast.LENGTH_SHORT).show();
        } else  {
            //when result content is  null
            Toast.makeText(getActivity().getApplicationContext(), "none", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_add_room, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.menu_add_room) {
            AddRoomFragment addRoom = new AddRoomFragment();
            getActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.drawer_layout, addRoom, "Add")
                    .addToBackStack(null)
                    .commit();
        }
        return super.onOptionsItemSelected(item);
    }

}