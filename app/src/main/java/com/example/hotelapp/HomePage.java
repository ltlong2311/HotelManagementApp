package com.example.hotelapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ListView;

import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;

public class HomePage extends AppCompatActivity {
    Toolbar toolbar;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ListView listView;
    ArrayList<ItemMenu> arrayList;
    MenuAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        Map();
        ActionRouter();
        ActionMenu();
    }

    private void ActionMenu() {
        arrayList = new ArrayList<>();
        arrayList.add(new ItemMenu( "Trang chu",R.drawable.hotel));
        arrayList.add(new ItemMenu( "1",R.drawable.hotel));
        arrayList.add(new ItemMenu( "1",R.drawable.hotel));
        arrayList.add(new ItemMenu( "2",R.drawable.hotel));
        arrayList.add(new ItemMenu( "Home",R.drawable.hotel));
        arrayList.add(new ItemMenu( "1",R.drawable.hotel));
        arrayList.add(new ItemMenu( "2",R.drawable.hotel));
        adapter = new MenuAdapter(this, R.layout.item_row, arrayList);
        listView.setAdapter(adapter);
    }

    private void ActionRouter() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationIcon(R.drawable.ic_action_menu);
        toolbar.setNavigationOnClickListener((v) ->{
            drawerLayout.openDrawer(GravityCompat.START);
        });

    }

    private void Map(){
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        navigationView = (NavigationView) findViewById(R.id.navigation);
        listView = (ListView) findViewById(R.id.listView);
    }


}