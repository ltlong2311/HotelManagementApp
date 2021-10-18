package com.example.hotelapp.Fragment.invoice;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.hotelapp.API.BaseUrl;
import com.example.hotelapp.Activities.Home;
import com.example.hotelapp.Model.Invoice;
import com.example.hotelapp.Adapters.InvoiceAdapter;
import com.example.hotelapp.Activities.InvoiceDetail;
import com.example.hotelapp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class InvoiceFragment extends Fragment {
    BaseUrl baseUrl = new BaseUrl();
    String urlGetData = baseUrl.getBaseURL()+ "/pays";

    ListView lvInvoice;
    ArrayList<Invoice> invoiceList;
    InvoiceAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_invoice, container, false);
        ((Home) getActivity())
                .setActionBarTitle("Hóa đơn");
        lvInvoice = root.findViewById(R.id.listViewInvoice);
        invoiceList = new ArrayList<>();
        adapter = new InvoiceAdapter(getActivity(), R.layout.invoice_item, invoiceList);
        lvInvoice.setAdapter(adapter);
        lvInvoice.setOnItemClickListener((parent, view, position, id) -> {
            Intent intent = new Intent(getActivity(), InvoiceDetail.class);
            Invoice invoice = invoiceList.get(position);
//                Toast.makeText(getActivity(), invoiceList.get(position).getCreateDate(), Toast.LENGTH_SHORT).show();
            intent.putExtra("invoiceID", invoiceList.get(position).getID());
            intent.putExtra("invoiceIDPhong", invoiceList.get(position).getIDPhong());
            intent.putExtra("invoiceThanhToan", invoiceList.get(position).getThanhToan());
            intent.putExtra("invoiceCreateDate", invoiceList.get(position).getCreateDate());
            JSONObject json = invoice.getInfoKhach();
            JSONObject jsonAddress = null;
            try {
                String ten = json.getString("Ten");
                String cmt = json.getString("CMT");
                String sdt = json.getString("SDT");
                String diachi = json.getString("DiaChi");
                intent.putExtra("invoiceTenKhach", ten);
                intent.putExtra("invoiceCMNDKhach", cmt);
                intent.putExtra("invoiceSDTKhach", sdt);
                intent.putExtra("invoiceDiaChiKhach", diachi);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            JSONArray arr = invoice.getDichVu();
            String tenDV = "";
            int giaDV = 0;
            ArrayList<String> listTenDV
                    = new ArrayList<String>();
            for (int i = 0; i < arr.length(); i++){
                try {
                    JSONObject dichvu = arr.getJSONObject(i);
                    //dv += dichvu.getString("TenDV");
                    listTenDV.add(dichvu.getString("TenDV"));
                    tenDV = listTenDV.toString();
                    tenDV = tenDV.replace("[", "")
                            .replace("]", "");
                    giaDV += dichvu.getInt("Gia");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            intent.putExtra("invoiceDichVu", tenDV);
            intent.putExtra("invoicePhiDichVu", giaDV);

            startActivity(intent);
        });

        getData(urlGetData);
        return root;
    }



    private void getData(String urlGetData) {
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, urlGetData, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("data");
//                            Toast.makeText(getActivity(), jsonArray.toString(), Toast.LENGTH_SHORT).show();
                            for (int i = 0; i < jsonArray.length(); i++){
                                JSONObject data = jsonArray.getJSONObject(i);
//                                Toast.makeText(getActivity(), data.getString("createDate"), Toast.LENGTH_SHORT).show();
//                                Toast.makeText(getActivity(), data.getJSONObject("InfoKhach").toString(), Toast.LENGTH_SHORT).show();
                                invoiceList.add(new Invoice(
                                        data.getInt("ID"),
                                        data.getInt("IDPhong"),
                                        data.getInt("IDUser"),
                                        data.getJSONObject("InfoKhach"),
                                        data.getJSONArray("DichVu"),
                                        data.getInt("ThanhToan"),
                                        data.getString("createDate")
                                ));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        adapter.notifyDataSetChanged();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        Toast.makeText(getActivity(), error.toString(), Toast.LENGTH_SHORT).show();
                    }
                }
        );
        requestQueue.add(request);
    }
}