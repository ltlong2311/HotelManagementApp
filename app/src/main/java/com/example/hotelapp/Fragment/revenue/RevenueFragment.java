package com.example.hotelapp.Fragment.revenue;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.hotelapp.API.BaseUrl;
import com.example.hotelapp.Activities.Home;
import com.example.hotelapp.Activities.InvoiceDetail;
import com.example.hotelapp.Activities.RoomEdit;
import com.example.hotelapp.Model.Invoice;
import com.example.hotelapp.R;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class RevenueFragment extends Fragment {

    private LineChart lineChart;
    TextView lastMonthRevenue, thisMonthRevenue, thisWeekRevenue;
    BaseUrl baseUrl = new BaseUrl();
    String urlGetRevenue = baseUrl.getBaseURL()+ "/statisticPay";

    ArrayList<String> timeLinesList = new ArrayList<>();
    ArrayList<String> timeLinesListThisWeek = new ArrayList<>();
    ArrayList<String> timeLinesListLastMonth = new ArrayList<>();
    String[] timeLines = new String[7];
    String[] timeLinesThisWeek = new String[7];
    String[] timeLinesLastMonth = new String[]{"Tuần 1", "Tuần 2", "Tuần 3", "Tuần 4", "Tuần 5"};
    ArrayList<Integer> revenueMList = new ArrayList<>();
    ArrayList<Integer> revenueMListThisWeek = new ArrayList<>();
    ArrayList<Integer> revenueMListLastMonth = new ArrayList<>();

    ArrayList<Entry> yValues = new ArrayList<>();

    ArrayList<String> arrayList_option;
    ArrayAdapter<String> arrayAdapterOption;
    AutoCompleteTextView optionSelect;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_revenue, container, false);
        ((Home) getActivity())
                .setActionBarTitle("Doanh thu");
        optionSelect = root.findViewById(R.id.optionRevenueSelection);
        lastMonthRevenue = root.findViewById(R.id.lastMonthRevenue);
        thisMonthRevenue = root.findViewById(R.id.thisMonthRevenue);
        thisWeekRevenue = root.findViewById(R.id.thisWeekRevenue);
        lineChart = root.findViewById(R.id.lineChart);
        lineChart.setBackgroundColor(Color.WHITE);
        lineChart.setDrawGridBackground(true);
        lineChart.animateXY(1500, 0);
        lineChart.setDrawBorders(false);
        lineChart.getDescription().setEnabled(false);
        lineChart.setNoDataTextColor(Color.RED);
        lineChart.setGridBackgroundColor(128);
        lineChart.setBorderColor(0);
//        lineChart.setViewPortOffsets(0, 0, 0,0);
//        lineChart.getXAxis().setAxisMinValue(0f);
        YAxis yAxis = lineChart.getAxisLeft();
        yAxis.setAxisMinimum(0);
        yAxis.setDrawAxisLine(false);
        yAxis.setDrawZeroLine(false);
        yAxis.setTextSize(9f);
        YAxis rightYAxis = lineChart.getAxisRight();
        rightYAxis.setEnabled(false);
        SharedPreferences preferences = getActivity().getApplicationContext().getSharedPreferences("tokenLogin", Context.MODE_PRIVATE);
        String token = preferences.getString("token", "");
        getLastMonthRevenue(urlGetRevenue, token);
        getThisMonthRevenue(urlGetRevenue, token);
        getThisWeekRevenue(urlGetRevenue, token);
        getLastWeekRevenue(urlGetRevenue, token);

        optionSelect.setAdapter(arrayAdapterOption);
        optionSelect.setThreshold(1);

        arrayList_option = new ArrayList<>();
        arrayList_option.add("Tuần trước");
        arrayList_option.add("Tháng trước");
        arrayList_option.add("Tuần này");
        arrayAdapterOption = new ArrayAdapter<>(getActivity().getApplicationContext(), R.layout.dropdown_item_re, arrayList_option);
        optionSelect.setAdapter(arrayAdapterOption);
        optionSelect.setThreshold(1);



        optionSelect.setOnItemClickListener((parent, view, position, id) -> {
            String check = parent.getItemAtPosition(position).toString();
            if (check.matches("Tuần trước")){
//                lineChart.clear();
                Toast.makeText(getActivity(), Arrays.toString(timeLines), Toast.LENGTH_SHORT).show();
                showChart("Doanh thu tuần trước", revenueMList, timeLines);
            } else if(check.matches("Tháng trước")){
                Toast.makeText(getActivity(), timeLinesListLastMonth.toString(), Toast.LENGTH_SHORT).show();
                showChart("Doanh thu tháng trước", revenueMListLastMonth, timeLinesLastMonth);
            }else if(check.matches("Tuần này")){
//                lineChart.clear();
//                System.arraycopy(timeLines, 0, timeLinesListThisWeek.toArray(timeLines), 0, timeLines.length);
                Toast.makeText(getActivity(), timeLinesListThisWeek.toString(), Toast.LENGTH_SHORT).show();
                showChart("Doanh thu tuần này", revenueMListThisWeek, timeLinesThisWeek);
            }
        });

        return root;
    }
    public void showChart(String titleChart, ArrayList<Integer> revenueList, String[] timeLine){
        lineChart.clear();
        yValues.clear();
        for (int i = 0; i < revenueList.size(); i++){
            yValues.add(new Entry(i, revenueList.get(i)));
        }
//        yValues.add(new Entry(0, 1320000));
//        yValues.add(new Entry(1, 1999000));
//        yValues.add(new Entry(2, 1702000));

        LineDataSet set1 = new LineDataSet(yValues, titleChart);
        set1.setFillAlpha(120);
        set1.setColor(Color.BLUE);
        set1.setValueTextSize(10f);
        set1.setValueTextColor(Color.parseColor("#67809f"));
        set1.setDrawFilled(true);
        set1.setLineWidth(1.5f);
        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(set1);

        LineData data = new LineData(dataSets);
        lineChart.setData(data);

        XAxis xAxis = lineChart.getXAxis();
        xAxis.setDrawAxisLine(false);
        xAxis.setDrawGridLines(false);

        xAxis.setValueFormatter(new IndexAxisValueFormatter(timeLine));
        xAxis.setGranularity(1);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
    }

    public void getLastMonthRevenue(String url, String token){
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        StringRequest stringRequest= new StringRequest(Request.Method.POST, url,
                response -> {
                    try {
                        JSONObject obj = new JSONObject(response);
                        JSONArray revenueData = obj.getJSONArray("data");
//                            Toast.makeText(getActivity(), revenueData.toString(), Toast.LENGTH_SHORT).show();
                        int revenue = 0;
                        for (int i = 0; i < revenueData.length(); i++){
                            try {
                                JSONObject revenuePoint = revenueData.getJSONObject(i);
                                revenue += revenuePoint.getInt("summary");
                                revenueMListLastMonth.add(revenuePoint.getInt("summary"));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        String lastMonthR = NumberFormat.getNumberInstance(Locale.US).format(revenue);
                        lastMonthRevenue.setText(lastMonthR);
                    } catch (Throwable t) {
                        Toast.makeText(getActivity(), "Could not parse malformed JSON: \"" + response + "\"", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> Toast.makeText(getActivity(), error.toString(), Toast.LENGTH_SHORT).show()
        ){
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("token", token);
                params.put("filter","lastMonth");
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }
    public void getThisMonthRevenue(String url, String token){
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        StringRequest stringRequest= new StringRequest(Request.Method.POST, url,
                response -> {
                    try {
                        JSONObject obj = new JSONObject(response);
                        JSONArray revenueData = obj.getJSONArray("data");
                        int revenue = 0;
                        for (int i = 0; i < revenueData.length(); i++){
                            try {
                                JSONObject revenuePoint = revenueData.getJSONObject(i);
                                revenue += revenuePoint.getInt("summary");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        String thisMonthR = NumberFormat.getNumberInstance(Locale.US).format(revenue);
                        thisMonthRevenue.setText(thisMonthR);
                    } catch (Throwable t) {
                        Toast.makeText(getActivity(), "Could not parse malformed JSON: \"" + response + "\"", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> Toast.makeText(getActivity(), error.toString(), Toast.LENGTH_SHORT).show()
        ){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("token", token);
                params.put("filter","thisMonth");
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }
    public void getThisWeekRevenue(String url, String token){
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        StringRequest stringRequest= new StringRequest(Request.Method.POST, url,
                response -> {
                    try {
                        JSONObject obj = new JSONObject(response);
                        JSONArray revenueData = obj.getJSONArray("data");
                        int revenue = 0;
                        for (int i = 0; i < revenueData.length(); i++){
                            try {
                                JSONObject revenuePoint = revenueData.getJSONObject(i);
                                revenue += revenuePoint.getInt("summary");
                                timeLinesListThisWeek.add(revenuePoint.getString("from").substring(5)); //rm year
                                timeLinesListThisWeek.toArray(timeLinesThisWeek);
                                revenueMListThisWeek.add(revenuePoint.getInt("summary"));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        String thisWeekR = NumberFormat.getNumberInstance(Locale.US).format(revenue);
                        thisWeekRevenue.setText(thisWeekR);
                    } catch (Throwable t) {
                        Toast.makeText(getActivity(), "Could not parse malformed JSON: \"" + response + "\"", Toast.LENGTH_SHORT).show();
                    }
//                    showChart("Doanh thu tuần này", revenueMListThisWeek);
                },
                error -> Toast.makeText(getActivity(), error.toString(), Toast.LENGTH_SHORT).show()
        ){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("token", token);
                params.put("filter","thisWeek");
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }
    public void getLastWeekRevenue(String url, String token){
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        StringRequest stringRequest= new StringRequest(Request.Method.POST, url,
                response -> {
                    try {
                        JSONObject obj = new JSONObject(response);
                        JSONArray revenueData = obj.getJSONArray("data");
                        for (int i = 0; i < revenueData.length(); i++){
                            try {
                                JSONObject revenuePoint = revenueData.getJSONObject(i);
                                timeLinesList.add(revenuePoint.getString("from").substring(5)); //rm year
                                timeLinesList.toArray(timeLines);
                                revenueMList.add(revenuePoint.getInt("summary"));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
//                        Toast.makeText(getActivity(), timeLinesList.toString(), Toast.LENGTH_SHORT).show();
//                        Toast.makeText(getActivity(), revenueMList.toString(), Toast.LENGTH_SHORT).show();
                    } catch (Throwable t) {
                        Toast.makeText(getActivity(), "Could not parse malformed JSON: \"" + response + "\"", Toast.LENGTH_SHORT).show();
                    }
                    showChart("Doanh thu tuần trước", revenueMList, timeLines);
                },

                error -> Toast.makeText(getActivity(), error.toString(), Toast.LENGTH_SHORT).show()
        ){
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("token", token);
                params.put("filter","lastWeek");
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }
}