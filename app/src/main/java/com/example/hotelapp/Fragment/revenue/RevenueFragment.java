package com.example.hotelapp.Fragment.revenue;

import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.hotelapp.R;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.util.ArrayList;
import java.util.Map;

public class RevenueFragment extends Fragment {

    private LineChart lineChart;
    private int fillColor = Color.argb(151, 51, 181, 229);

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_revenue, container, false);
        lineChart = (LineChart) root.findViewById(R.id.lineChart);
        lineChart.setBackgroundColor(Color.WHITE);
        lineChart.setGridBackgroundColor(Color.WHITE);
        lineChart.setDrawGridBackground(true);

        lineChart.setDrawBorders(true);
        lineChart.getDescription().setEnabled(true);

        ArrayList<Entry> yValues = new ArrayList<>();
        yValues.add(new Entry(0, 1660f));
        yValues.add(new Entry(1, 1820f));
        yValues.add(new Entry(2, 1400f));
        yValues.add(new Entry(3, 1999f));

        LineDataSet set1 = new LineDataSet(yValues, "Data Set");
        set1.setFillAlpha(110);
        set1.setColor(Color.RED);
        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(set1);

        LineData data = new LineData(dataSets);
        lineChart.setData(data);

        return root;
    }
}