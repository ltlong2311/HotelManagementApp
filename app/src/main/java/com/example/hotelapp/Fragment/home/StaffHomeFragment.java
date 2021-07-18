package com.example.hotelapp.Fragment.home;

import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.hotelapp.Fragment.invoice.InvoiceFragment;
import com.example.hotelapp.Fragment.listRoom.ListRoomStaffFragment;
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
        cardViewListRoom = root.findViewById(R.id.cardViewStaffListRoom);
        cardViewService = root.findViewById(R.id.cardViewStaffService);
        cardViewInvoice = root.findViewById(R.id.cardViewStaffInvoice);
        cardViewInfo = root.findViewById(R.id.cardViewStaffInfo);
        cardViewApp = root.findViewById(R.id.cardViewStaffApp);
        cardViewSupport = root.findViewById(R.id.cardViewStaffSupport);

        cardViewListRoom.setOnClickListener((View v) ->
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment,
                    new ListRoomStaffFragment()).commit()
        );
        cardViewService.setOnClickListener((View v) ->
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment,
                        new ServiceFragment()).addToBackStack(null).commit()
         );
        cardViewInvoice.setOnClickListener((View v) ->
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment,
                        new InvoiceFragment()).addToBackStack(null).commit()
        );
        cardViewInfo.setOnClickListener((View v) ->
                Toast.makeText(getActivity(),"Thông tin",Toast.LENGTH_SHORT)
        );
        cardViewApp.setOnClickListener((View v) ->
                Toast.makeText(getActivity(),"App quản lý khách sạn",Toast.LENGTH_SHORT)
        );
        cardViewSupport.setOnClickListener((View v) ->
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment,
                        new SupportFragment()).addToBackStack(null).commit()
        );
        return root;
    }
}