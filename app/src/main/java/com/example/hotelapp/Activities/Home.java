package com.example.hotelapp.Activities;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.hotelapp.Fragment.home.HomeFragment;
import com.example.hotelapp.Fragment.listRoom.ListRoomFragment;
import com.example.hotelapp.Fragment.invoice.InvoiceFragment;
import com.example.hotelapp.Fragment.manage.ManageFragment;
import com.example.hotelapp.Fragment.revenue.RevenueFragment;
import com.example.hotelapp.Fragment.service.ServiceFragment;
import com.example.hotelapp.LoginActivity;
import com.example.hotelapp.R;
import com.google.android.material.navigation.NavigationView;
import com.muddzdev.styleabletoast.StyleableToast;

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

public class Home extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;
    private AppBarConfiguration mAppBarConfiguration;
//    String urlGetData = "http://192.168.60.1/severApp/services";
    public static Toolbar mToolbar;
//    GridView gridViewRoom;
//    ListView lvRoom;
//    ArrayList<Room> arrayRoom;
//    RoomAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Window window = getWindow();


        setContentView(R.layout.activity_home);

        if(Build.VERSION.SDK_INT>=19 && Build.VERSION.SDK_INT<21 )
        {
            SetWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, true);
        }
        if(Build.VERSION.SDK_INT>=19)
        {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE |
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }
        if(Build.VERSION.SDK_INT>=21)
        {
            SetWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }


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
       mToolbar = findViewById(R.id.toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();


        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.

        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_room_list, R.id.nav_invoice, R.id.nav_service, R.id.nav_revenue)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);

//        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
//        NavigationUI.setupWithNavController(navigationView, navController);

        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(drawerToggle);

//        getData(urlGetData);
        //        String value=getIntent().getStringExtra("key");
//        Bundle bundle = new Bundle();
//        bundle.putString("key",value);
//        ListRoomFragment lr= new ListRoomFragment();
//        lr.setArguments(bundle);
        setActionBarTitle("Home");

        Intent intent = getIntent();
        String toListRoom = intent.getStringExtra("url");
        if (toListRoom != null){
            mToolbar.setTitle("Sơ đồ phòng");
            getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment,
                    new ListRoomFragment()).commit();
        }


        String toService = intent.getStringExtra("service");
        if (toService != null){
//            Toast.makeText(Home.this, toService, Toast.LENGTH_SHORT).show();
            getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment,
                    new ServiceFragment()).commit();
        }

        String toInvoice = intent.getStringExtra("invoice");
        if (toInvoice != null){
            getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment,
                    new InvoiceFragment()).commit();
        }

    }
    private static void SetWindowFlag(Activity activity, final int Bits, Boolean on) {
        Window win =  activity.getWindow();
        WindowManager.LayoutParams Winparams = win.getAttributes();
        if (on) {
            Winparams.flags  |=Bits;
        } else {
            Winparams.flags &= ~Bits;
        }
        win.setAttributes(Winparams);

    }
    private void getData(String url){
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
//                        private String response = response;
                        Toast.makeText(Home.this, response.toString(), Toast.LENGTH_SHORT).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
//                        Toast.makeText(Home.this, error.toString(), Toast.LENGTH_SHORT).show();
                          Toast.makeText(Home.this, "", Toast.LENGTH_SHORT).show();
                    }
                }
        );
        requestQueue.add(jsonArrayRequest);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//        if(drawerToggle.onOptionsItemSelected(item)) {
//            return true;
//        }
        int id = item.getItemId();

        if (id == R.id.nav_log_out){

            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
        switch (item.getItemId()) {
            case R.id.nav_log_out:
                Toast.makeText(this, "Logout", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.info_app:
                StyleableToast.makeText(this, "Made by: Nhóm 4- CT2", Toast.LENGTH_SHORT, R.style.toastInfo).show();
                return true;
            case R.id.assist:
                StyleableToast.makeText(this, "Thông tin hỗ trợ ứng dụng", Toast.LENGTH_SHORT, R.style.toastBlueLight).show();
                return true;
        }
        switch (item.getItemId()){
            case R.id.nav_home:
//                mToolbar.setTitle(getString(R.string.dashboard_new_sale));

                mToolbar.setTitle("Home");
                getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment,
                        new HomeFragment()).commit();

                break;
            case R.id.nav_room_list:
                mToolbar.setTitle("Sơ đồ phòng");
                getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment,
                        new ListRoomFragment()).commit();
                break;
            case R.id.nav_service:
                mToolbar.setTitle("Dịch vụ");
                getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment,
                        new ServiceFragment()).commit();
                break;
            case R.id.nav_invoice:
                mToolbar.setTitle("Hóa đơn");
                getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment,
                        new InvoiceFragment()).commit();
                break;
            case R.id.nav_revenue:
                mToolbar.setTitle("Doanh thu");
                getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment,
                        new RevenueFragment()).commit();
                break;
            case R.id.nav_manage:
                mToolbar.setTitle("Quản lý");
                getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment,
                        new ManageFragment()).commit();
                break;
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

        return true;
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
//        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }


    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    public void setActionBarTitle(String title) {
        getSupportActionBar().setTitle(title);
    }
}