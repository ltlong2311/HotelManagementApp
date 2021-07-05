package com.example.hotelapp.Fragment.revenue;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class RevenueFragment extends Fragment {

    private LineChart lineChart;
    TextView lastMonthRevenue, thisMonthRevenue, thisWeekRevenue;
    String urlGetRevenue = "http://192.168.60.1/severApp/statisticPay";
    String token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJJRCI6IjEiLCJ1c2VyTmFtZSI6ImFkbWluIiwiSG9UZW4iOiJhZG1pbiJ9.234-aSxbQPO_Ozd4kcffsavH1FRWBgBx61dga5ZrAWE";

    ArrayList<String> timeLinesList = new ArrayList<String>();
    String[] timeLines = new String[]{"d1","d2","d3","d4","d5","d6","d7"};
    ArrayList<Integer> revenueMList = new ArrayList<Integer>();

    ArrayList<Entry> yValues = new ArrayList<>();

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
        lastMonthRevenue = root.findViewById(R.id.lastMonthRevenue);
        thisMonthRevenue = root.findViewById(R.id.thisMonthRevenue);
        thisWeekRevenue = root.findViewById(R.id.thisWeekRevenue);
        lineChart = (LineChart) root.findViewById(R.id.lineChart);
        lineChart.setBackgroundColor(Color.WHITE);
        lineChart.setDrawGridBackground(true);
        lineChart.animateXY(2000, 0);
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

//        ArrayList<Entry> yValues = new ArrayList<>();


        getLastMonthRevenue(urlGetRevenue);
        getThisMonthRevenue(urlGetRevenue);
        getThisWeekRevenue(urlGetRevenue);
        getLastWeekRevenue(urlGetRevenue);
        return root;
    }
    public void showChart(){

        for (int i = 0; i < revenueMList.size(); i++){
            yValues.add(new Entry(i, revenueMList.get(i)));
            Toast.makeText(getActivity(), revenueMList.toString(), Toast.LENGTH_SHORT).show();
        }
//        yValues.add(new Entry(0, 1320000));
//        yValues.add(new Entry(1, 1999000));
//        yValues.add(new Entry(2, 1702000));
//        yValues.add(new Entry(3, 2303000));
//        yValues.add(new Entry(4, 1802000));
//        yValues.add(new Entry(5, 2603000));
//        yValues.add(new Entry(6, 2503000));



        LineDataSet set1 = new LineDataSet(yValues, "Doanh thu");
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

        xAxis.setValueFormatter(new IndexAxisValueFormatter(timeLines));
        xAxis.setGranularity(1);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
    }

    public void getLastMonthRevenue(String url){
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        StringRequest stringRequest= new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            JSONArray revenueData = obj.getJSONArray("data");
                            Toast.makeText(getActivity(), revenueData.toString(), Toast.LENGTH_SHORT).show();
                            String startTime = "";
                            String endTime = "";
                            int revenue = 0;
                            ArrayList<String> listRevenuePoint = new ArrayList<String>();
                            for (int i = 0; i < revenueData.length(); i++){
                                try {
                                    JSONObject revenuePoint = revenueData.getJSONObject(i);
                                    revenue += revenuePoint.getInt("summary");
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                            lastMonthRevenue.setText(String.valueOf(revenue));
                        } catch (Throwable t) {
                            Toast.makeText(getActivity(), "Could not parse malformed JSON: \"" + response + "\"", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getActivity(), error.toString(), Toast.LENGTH_SHORT).show();
                    }
                }
        ){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("token", token);
                params.put("filter","lastMonth");
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }
    public void getThisMonthRevenue(String url){
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        StringRequest stringRequest= new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            JSONArray revenueData = obj.getJSONArray("data");
                            String startTime = "";
                            String endTime = "";
                            int revenue = 0;
                            ArrayList<String> listRevenuePoint = new ArrayList<String>();
                            for (int i = 0; i < revenueData.length(); i++){
                                try {
                                    JSONObject revenuePoint = revenueData.getJSONObject(i);
                                    revenue += revenuePoint.getInt("summary");
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                            thisMonthRevenue.setText(String.valueOf(revenue));
                        } catch (Throwable t) {
                            Toast.makeText(getActivity(), "Could not parse malformed JSON: \"" + response + "\"", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getActivity(), error.toString(), Toast.LENGTH_SHORT).show();
                    }
                }
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
    public void getThisWeekRevenue(String url){
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        StringRequest stringRequest= new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            JSONArray revenueData = obj.getJSONArray("data");
                            String startTime = "";
                            String endTime = "";
                            int revenue = 0;
                            ArrayList<String> listRevenuePoint = new ArrayList<String>();
                            for (int i = 0; i < revenueData.length(); i++){
                                try {
                                    JSONObject revenuePoint = revenueData.getJSONObject(i);
                                    revenue += revenuePoint.getInt("summary");
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                            thisWeekRevenue.setText(String.valueOf(revenue));
                        } catch (Throwable t) {
                            Toast.makeText(getActivity(), "Could not parse malformed JSON: \"" + response + "\"", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getActivity(), error.toString(), Toast.LENGTH_SHORT).show();
                    }
                }
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

    public void getLastWeekRevenue(String url){
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        StringRequest stringRequest= new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            JSONArray revenueData = obj.getJSONArray("data");
                            String startTime = "";
                            String endTime = "";
                            int revenue = 0;
                            for (int i = 0; i < revenueData.length(); i++){
                                try {
                                    JSONObject revenuePoint = revenueData.getJSONObject(i);
                                    timeLinesList.add(revenuePoint.getString("from").substring(5)); //bỏ năm
                                    timeLinesList.toArray(timeLines);
                                    revenueMList.add(revenuePoint.getInt("summary"));
                                    revenue += revenuePoint.getInt("summary");
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                            Toast.makeText(getActivity(), timeLinesList.toString(), Toast.LENGTH_SHORT).show();
                            Toast.makeText(getActivity(), revenueMList.toString(), Toast.LENGTH_SHORT).show();
                            thisWeekRevenue.setText(String.valueOf(revenue));
                        } catch (Throwable t) {
                            Toast.makeText(getActivity(), "Could not parse malformed JSON: \"" + response + "\"", Toast.LENGTH_SHORT).show();
                        }
                        showChart();
                    }
                },

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getActivity(), error.toString(), Toast.LENGTH_SHORT).show();
                    }
                }
        ){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("token", token);
                params.put("filter","lastWeek");
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }
}