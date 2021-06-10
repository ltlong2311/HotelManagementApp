package com.example.hotelapp.Fragment.invoice;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.hotelapp.Customer;
import com.example.hotelapp.Home;
import com.example.hotelapp.Invoice;
import com.example.hotelapp.InvoiceAdapter;
import com.example.hotelapp.R;
import com.example.hotelapp.Service;
import com.example.hotelapp.ServiceAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class InvoiceFragment extends Fragment {

    String urlGetData = "http://192.168.60.1/severApp/pays";

    ListView lvInvoice;
    ArrayList<Invoice> arrayInvoice;
    InvoiceAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_invoice, container, false);
        ((Home) getActivity())
                .setActionBarTitle("Hóa đơn");
        lvInvoice = root.findViewById(R.id.listViewInvoice);
        arrayInvoice = new ArrayList<>();
        adapter = new InvoiceAdapter(getActivity(), R.layout.invoice_row, arrayInvoice);
        lvInvoice.setAdapter(adapter);
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
                            Toast.makeText(getActivity(), response.toString(), Toast.LENGTH_SHORT).show();
                            for (int i = 0; i < jsonArray.length(); i++){
                                JSONObject data = jsonArray.getJSONObject(i);
                                Toast.makeText(getActivity(), data.getString("createDate"), Toast.LENGTH_SHORT).show();
                                JSONArray infoCustomer = data.getJSONArray("infoKhach");
                                ArrayList<Customer> customer = new ArrayList<>();
                                for (int j = 0; j < infoCustomer.length(); j++){
                                    JSONObject dataInfo = infoCustomer.getJSONObject(j);
                                    customer.add(new Customer(
                                            dataInfo.getString("Ten"),
                                            dataInfo.getString("CMT"),
                                            dataInfo.getString("SDT"),
                                            dataInfo.getString("Diachi")
                                            ));
                                    Toast.makeText(getActivity(), dataInfo.getString("Ten"), Toast.LENGTH_SHORT).show();
                                }
                                JSONArray serviceUsed = data.getJSONArray("DichVu");
                                ArrayList<Customer> service = new ArrayList<>();
                                for (int k = 0; k < serviceUsed.length(); k++){
                                    JSONObject dataService = serviceUsed.getJSONObject(k);
//                                    service.add(new ServiceUsed(
//                                            dataService.getString("TenDV"),
//                                            dataService.getString("Gia")
//                                    ));
                                    Toast.makeText(getActivity(), dataService.getString("TenDV"), Toast.LENGTH_SHORT).show();
                                }
                                ArrayList<Service> Service = new ArrayList<>();
                                {

                                }
//                                arrayInvoice.add(new Invoice(
//                                        data.getInt("ID"),
//                                        data.getInt("IDPhong"),
//                                        data.getInt("IDUser"),
//                                        data.getJSONArray("InfoKhach"),
//                                        data.getJSONArray("DichVu"),
//                                        data.getInt("ThanhToan"),
//                                        data.getString("createDate")
//                                ));


/*                               private int ID;
                                private int IDPhong;
                                private int IDUser;
                                private Customer InfoKhach;
                                private List<Service> DichVu;
                                private int ThanhToan;
                                private String createDate; */
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
//                        adapter.notifyDataSetChanged();
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