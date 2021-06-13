package com.example.hotelapp.Fragment.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.hotelapp.Fragment.invoice.InvoiceFragment;
import com.example.hotelapp.Fragment.listRoom.ListRoomFragment;
import com.example.hotelapp.Fragment.revenue.RevenueFragment;
import com.example.hotelapp.Fragment.service.ServiceFragment;
import com.example.hotelapp.R;


public class HomeFragment extends Fragment {

    CardView cardViewListRoom, cardViewService, cardViewInvoice, cardViewRevenue;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        setHasOptionsMenu(true);
//        getActivity().getActionBar().hide();
    }
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_home, container, false);

        cardViewListRoom = (CardView) root.findViewById(R.id.cardViewListRoom);
        cardViewService = (CardView) root.findViewById(R.id.cardViewService);
        cardViewInvoice = (CardView) root.findViewById(R.id.cardViewInvoice);
        cardViewRevenue = (CardView) root.findViewById(R.id.cardViewRevenue);
        cardViewInvoice = (CardView) root.findViewById(R.id.cardViewInvoice);


        cardViewListRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment,
                        new ListRoomFragment()).commit();
            }
        });
        cardViewService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment,
                        new ServiceFragment()).addToBackStack(null).commit();
            }
        });
        cardViewInvoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment,
                        new InvoiceFragment()).addToBackStack(null).commit();
            }
        });
        cardViewRevenue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment,
                        new RevenueFragment()).addToBackStack(null).commit();
            }
        });

        return root;
    }
}