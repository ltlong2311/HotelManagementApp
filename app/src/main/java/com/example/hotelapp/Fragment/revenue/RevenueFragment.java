package com.example.hotelapp.Fragment.revenue;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.hotelapp.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RevenueFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RevenueFragment extends Fragment {



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_revenue, container, false);
    }
}