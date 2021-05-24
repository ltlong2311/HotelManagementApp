package com.example.hotelapp;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.hotelapp.ui.ReceiptFragment;
import com.example.hotelapp.ui.home.HomeFragment;
import com.example.hotelapp.ui.listRoom.ListRoomFragment;
import com.example.hotelapp.ui.service.ServiceFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.core.view.GravityCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Home extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    DrawerLayout drawerLayout;
    private AppBarConfiguration mAppBarConfiguration;
    String urlGetData = "http://192.168.1.4/severApp/products";

//    GridView gridViewRoom;
//    ListView lvRoom;
//    ArrayList<Room> arrayRoom;
//    RoomAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//        FloatingActionButton fab = findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });


       drawerLayout = findViewById(R.id.drawer_layout);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();


        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.

        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_room_list, R.id.nav_receipt, R.id.nav_service, R.id.nav_revenue)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

//        if(savedInstanceState == null) {
//            getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment,
//                    new HomeFragment()).commit();
//            navigationView.setCheckedItem(R.id.nav_home);
//        }
////

//        lvRoom = findViewById(R.id.gridViewRoom);

//        lvRoom = (ListView) findViewById(R.id.listViewRoom);
//        arrayRoom = new ArrayList<>();
//        adapter = new RoomAdapter(this, R.layout.room_row, arrayRoom);
//        lvRoom.setAdapter(adapter);
          getData(urlGetData);
    }

    private void getData(String url){
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
//                        private String response = response;
                        Toast.makeText(Home.this, response.toString(), Toast.LENGTH_SHORT).show();
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
//                        adapter.notifyDataSetChanged();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(Home.this, error.toString(), Toast.LENGTH_SHORT).show();
                    }
                }
        );
        requestQueue.add(jsonArrayRequest);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
//            case R.id.nav_home:
//                getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment,
//                        new HomeFragment()).commit();
//
//                break;
//            case R.id.nav_room_list:
//                getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment,
//                        new ListRoomFragment()).commit();
//                break;
//            case R.id.nav_slideshow:
//                getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment,
//                        new ServiceFragment()).commit();
//                break;
//            case R.id.nav_receipt:
//                getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment,
//                        new ReceiptFragment()).commit();
//                break;
//            case R.id.nav_manager_account:
//                Toast.makeText(this, "Quản lý", Toast.LENGTH_SHORT).show();
//                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_log_out:
                Toast.makeText(this, "Test", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.info_app:
                Toast.makeText(this, "Thông tin ứng dụng", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.assist:
                Toast.makeText(this, "Thông tin hỗ trợ", Toast.LENGTH_SHORT).show();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

//    @Override
//    public boolean onOptionsItemSelected(Menu menu) {
//        // The action bar home/up action should open or close the drawer.
//        switch (menu.getItemID()) {
//            case android.R.id.home:
//                mDrawer.openDrawer(GravityCompat.START);
//                return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
}