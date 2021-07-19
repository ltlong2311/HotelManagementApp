package com.example.hotelapp.Fragment.manage;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.example.hotelapp.R;

import java.util.Calendar;


public class AddUserFragment extends Fragment {
    TextView textNgaySinh;
    EditText edtUsername, edtPassword, edtTen, edtCMT, edtNgaySinh, edtDiaChi;
    DatePickerDialog.OnDateSetListener setListener;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_add_user, container, false);
        textNgaySinh = root.findViewById(R.id.textNS);
        edtNgaySinh = root.findViewById(R.id.editTextNgaySinh);

        Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);

        edtNgaySinh.setOnClickListener((View v) ->{
            DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), android.R.style.Theme_Holo_Dialog_MinWidth
                    ,setListener,year,month,day);
            datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            datePickerDialog.show();
        });

        setListener = (view, year1, month1, dayOfMonth) -> {
            month1 = month1 + 1;
            String date = day+"/"+ month1 +"/"+"year";
            edtNgaySinh.setText(date);
        };

        return root;
    }
}