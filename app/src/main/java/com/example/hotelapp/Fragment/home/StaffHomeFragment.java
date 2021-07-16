package com.example.hotelapp.Fragment.home;

import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.hotelapp.Fragment.invoice.InvoiceFragment;
import com.example.hotelapp.Fragment.listRoom.ListRoomFragment;
import com.example.hotelapp.Fragment.listRoom.ListRoomStaffFragment;
import com.example.hotelapp.Fragment.manage.ManageFragment;
import com.example.hotelapp.Fragment.revenue.RevenueFragment;
import com.example.hotelapp.Fragment.service.ServiceFragment;
import com.example.hotelapp.Fragment.support.SupportFragment;
import com.example.hotelapp.R;


public class StaffHomeFragment extends Fragment {

    CardView cardViewListRoom, cardViewService, cardViewInvoice, cardViewInfo, cardViewApp, cardViewSupport;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_staff_home, container, false);
        cardViewListRoom = (CardView) root.findViewById(R.id.cardViewStaffListRoom);
        cardViewService = (CardView) root.findViewById(R.id.cardViewStaffService);
        cardViewInvoice = (CardView) root.findViewById(R.id.cardViewStaffInvoice);
        cardViewInfo = (CardView) root.findViewById(R.id.cardViewStaffInfo);
        cardViewApp = (CardView) root.findViewById(R.id.cardViewStaffApp);
        cardViewSupport = (CardView) root.findViewById(R.id.cardViewStaffSupport);

        cardViewListRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment,
                        new ListRoomStaffFragment()).commit();
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
        cardViewInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment,
                        new RevenueFragment()).addToBackStack(null).commit();
            }
        });
        cardViewApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(),"App quản lý khách sạn",Toast.LENGTH_SHORT);
            }
        });
        cardViewSupport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment,
                        new SupportFragment()).addToBackStack(null).commit();
            }
        });
        return root;
    }
}