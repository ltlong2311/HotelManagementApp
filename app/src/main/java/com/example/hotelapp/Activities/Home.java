package com.example.hotelapp.Activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
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
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.hotelapp.API.BaseUrl;
import com.example.hotelapp.Fragment.home.HomeFragment;
import com.example.hotelapp.Fragment.home.StaffHomeFragment;
import com.example.hotelapp.Fragment.listBooking.ListBookingFragment;
import com.example.hotelapp.Fragment.listRoom.ListRoomFragment;
import com.example.hotelapp.Fragment.invoice.InvoiceFragment;
import com.example.hotelapp.Fragment.listRoom.ListRoomStaffFragment;
import com.example.hotelapp.Fragment.manage.ChangePasswordFragment;
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
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Home extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    BaseUrl baseUrl = new BaseUrl();
    DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;
    private AppBarConfiguration mAppBarConfiguration;
    String checkPermission = baseUrl.getBaseURL() + "/permissions";
    int permission;
    public static Toolbar mToolbar;

    @SuppressLint("CutPasteId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Window window = getWindow();

        setContentView(R.layout.activity_home);

        if (Build.VERSION.SDK_INT >= 19 && Build.VERSION.SDK_INT < 21) {
            SetWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, true);
        }
        if (Build.VERSION.SDK_INT >= 19) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE |
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }
        if (Build.VERSION.SDK_INT >= 21) {
            SetWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.drawer_layout);
        mToolbar = findViewById(R.id.toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_room_list, R.id.nav_invoice, R.id.nav_service, R.id.nav_booking_list, R.id.nav_change_password,
                R.id.nav_revenue, R.id.nav_log_out, R.id.nav_manage, R.id.info_app)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);

        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(drawerToggle);

        setActionBarTitle("Home");

        SharedPreferences preferences = Home.this.getApplicationContext().getSharedPreferences("tokenLogin", Context.MODE_PRIVATE);
        String token = preferences.getString("token", "");
        checkPermission(checkPermission, token);

        Intent intent = getIntent();
        String toListRoom = intent.getStringExtra("url");
        if (toListRoom != null) {
            mToolbar.setTitle("Sơ đồ phòng");
            getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment,
                    new ListRoomFragment()).commit();
        }

        String toService = intent.getStringExtra("service");
        if (toService != null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment,
                    new ServiceFragment()).commit();
        }

        String toInvoice = intent.getStringExtra("invoice");
        if (toInvoice != null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment,
                    new InvoiceFragment()).commit();
        }

        String toManage = intent.getStringExtra("manage");
        if (toManage != null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment,
                    new ManageFragment()).commit();
        }

    }

    private static void SetWindowFlag(Activity activity, final int Bits, Boolean on) {
        Window win = activity.getWindow();
        WindowManager.LayoutParams Winparams = win.getAttributes();
        if (on) {
            Winparams.flags |= Bits;
        } else {
            Winparams.flags &= ~Bits;
        }
        win.setAttributes(Winparams);
    }

    private void getData(String url) {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                response -> Toast.makeText(Home.this, response.toString(), Toast.LENGTH_SHORT).show(),
                error -> Toast.makeText(Home.this, "", Toast.LENGTH_SHORT).show()
        );
        requestQueue.add(jsonArrayRequest);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//        if(drawerToggle.onOptionsItemSelected(item)) {
//            return true;
//        }
        int id = item.getItemId();

        if (id == R.id.nav_log_out) {
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
        switch (item.getItemId()) {
            case R.id.nav_log_out:
                Toast.makeText(this, "Logout", Toast.LENGTH_SHORT).show();
                SharedPreferences preferences = this.getApplicationContext().getSharedPreferences("tokenLogin", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.remove("token");
                editor.remove("permission");
                editor.apply();
                return true;
            case R.id.info_app:
                StyleableToast.makeText(this, "Made by: Nhóm 4- CT2", Toast.LENGTH_SHORT, R.style.toastInfo).show();
                return true;
            case R.id.assist:
                StyleableToast.makeText(this, "Thông tin hỗ trợ ứng dụng", Toast.LENGTH_SHORT, R.style.toastBlueLight).show();
                return true;
        }
        switch (item.getItemId()) {
            case R.id.nav_home:
                if (permission == 2) {
                    mToolbar.setTitle("Home");
                    getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment,
                            new HomeFragment()).commit();
                } else {
                    mToolbar.setTitle("Home");
                    getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment,
                            new StaffHomeFragment()).commit();
                }
                break;
            case R.id.nav_room_list:
                if (permission == 2) {
                    mToolbar.setTitle("Sơ đồ phòng");
                    getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment,
                            new ListRoomFragment()).commit();
                } else if (permission == 1) {
                    mToolbar.setTitle("Sơ đồ phòng");
                    getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment,
                            new ListRoomStaffFragment()).commit();
                }
                break;
            case R.id.nav_service:
                mToolbar.setTitle("Dịch vụ");
                getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment,
                        new ServiceFragment()).commit();
                break;
            case R.id.nav_booking_list:
                mToolbar.setTitle("Danh sách đặt phòng");
                getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment,
                        new ListBookingFragment()).commit();
                break;
            case R.id.nav_invoice:
                if (permission == 2) {
                    mToolbar.setTitle("Hóa đơn");
                    getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment,
                            new InvoiceFragment()).commit();
                } else if (permission == 1) {
                    StyleableToast.makeText(this, "Chức năng này chỉ dành cho quản lý!", Toast.LENGTH_SHORT, R.style.toastStyle).show();

                }
                break;
            case R.id.nav_revenue:
                if (permission == 2) {
                    mToolbar.setTitle("Doanh thu");
                    getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment,
                            new RevenueFragment()).commit();
                } else {
                    StyleableToast.makeText(this, "Chức năng này chỉ dành cho quản lý!", Toast.LENGTH_SHORT, R.style.toastStyle).show();
                }
                break;
            case R.id.nav_manage:
                if (permission == 2) {
                    mToolbar.setTitle("Quản lý");
                    getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment,
                            new ManageFragment()).commit();
                } else {
                    StyleableToast.makeText(this, "Chức năng này chỉ dành cho người quản lý!", Toast.LENGTH_SHORT, R.style.toastStyle).show();
                }
                break;
            case R.id.nav_change_password:
                mToolbar.setTitle("Đổi mật khẩu");
                getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment,
                        new ChangePasswordFragment()).commit();
                break;
        }
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

        return true;
    }


    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
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

    public void checkPermission(String url, String token) {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                response -> {
                    try {
                        JSONObject obj = new JSONObject(response);
//                            StyleableToast.makeText(Home.this, obj.toString(), Toast.LENGTH_SHORT, R.style.toastStyle).show();
                        String status = obj.getString("status");
                        JSONObject data = obj.getJSONObject("data");
                        if (status.equals("success")) {
                            permission = data.getInt("role");
                            SharedPreferences preferences = Home.this.getApplicationContext().getSharedPreferences("tokenLogin", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = preferences.edit();
                            editor.putInt("permission", permission);
                            editor.apply();
                            if (permission == 1) {
                                getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment,
                                        new StaffHomeFragment()).commit();
                            }

//                                StyleableToast.makeText(Home.this, String.valueOf(permission), Toast.LENGTH_SHORT, R.style.toastSuccess2).show();
                        } else {
                            StyleableToast.makeText(Home.this, "Lỗi!", Toast.LENGTH_SHORT, R.style.toastStyle).show();
                        }
                    } catch (Throwable t) {
                        StyleableToast.makeText(Home.this, "Could not parse malformed JSON: \"" + response + "\"", Toast.LENGTH_SHORT, R.style.toastStyle).show();
                    }
                },
                error -> StyleableToast.makeText(Home.this, error.toString(), Toast.LENGTH_SHORT, R.style.toastError).show()
        ) {
            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<>();
                params.put("token", token);
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }

    public void setActionBarTitle(String title) {
        getSupportActionBar().setTitle(title);
    }

    private void hideKeyBoard(View v) {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }
}