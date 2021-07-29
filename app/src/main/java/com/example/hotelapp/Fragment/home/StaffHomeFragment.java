package com.example.hotelapp.Fragment.home;

import android.app.ActionBar;
import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.example.hotelapp.Activities.Home;
import com.example.hotelapp.Fragment.invoice.InvoiceFragment;
import com.example.hotelapp.Fragment.listRoom.ListRoomStaffFragment;
import com.example.hotelapp.Fragment.service.ServiceFragment;
import com.example.hotelapp.Fragment.support.SupportFragment;
import com.example.hotelapp.R;

import java.util.ArrayList;
import java.util.List;


public class StaffHomeFragment extends Fragment {

    CardView cardViewListRoom, cardViewService, cardViewInvoice, cardViewInfo, cardViewApp, cardViewSupport;
    ImageSlider imageSlider;

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
        cardViewApp = root.findViewById(R.id.cardViewStaffApp);
        cardViewSupport = root.findViewById(R.id.cardViewStaffSupport);
        imageSlider = root.findViewById(R.id.slider);

        ((Home) getActivity())
                .setActionBarTitle("Home");

        List<SlideModel> slideList = new ArrayList<>();
        slideList.add(new SlideModel(R.drawable.ks,null));
        slideList.add(new SlideModel(R.drawable.hotel6, null));
        slideList.add(new SlideModel(R.drawable.hotel_room13,null));
        slideList.add(new SlideModel(R.drawable.hotel_room4,null));
        slideList.add(new SlideModel(R.drawable.hotel_room3,null));
        imageSlider.setImageList(slideList, ScaleTypes.CENTER_CROP);

        cardViewListRoom.setOnClickListener((View v) ->
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment,
                    new ListRoomStaffFragment()).commit()
        );
        cardViewService.setOnClickListener((View v) ->
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment,
                        new ServiceFragment()).addToBackStack(null).commit()
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