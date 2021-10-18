package com.example.hotelapp.Adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;

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
import com.example.hotelapp.Fragment.listRoom.ListRoomFragment;
import com.example.hotelapp.Fragment.listRoom.ListRoomStaffFragment;
import com.example.hotelapp.R;
import com.example.hotelapp.Model.Room;
import com.google.android.material.textfield.TextInputLayout;
import com.muddzdev.styleabletoast.StyleableToast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RoomAdapter extends BaseAdapter {

    private final Context context;
    private final int layout;
    private List<Room> RoomList;
    BaseUrl baseUrl = new BaseUrl();
    String urlCheckIn = baseUrl.getBaseURL() + "/createPay";
    String urlCheckOut = baseUrl.getBaseURL() + "/checkoutPay";
    String urlGetInvoiceInfo = baseUrl.getBaseURL() + "/pays/";
    String urlGetServiceInfo = baseUrl.getBaseURL() + "/services";
    String urlAddService = baseUrl.getBaseURL() + "/updatePay";

    public static final String SHARED_ID_INVOICE = "idInvoice";
    int vt = -1;
    int optionServiceID, permission;
    EditText maPhongEdt, tenEdt, cmtEdt, sdtEdt, diachiEdt, maPhongEdtCO, tenEdtCO, cmtEdtCO, sdtEdtCO, diachiEdtCO, thanhtoanEdt, maPhongAddService;
    String token, ten, cmt, sdt, diachi;
    TextView multiSelectService;
    boolean[] selectedService;
    String option = "TheoPhong";
    TextInputLayout layoutOption;
    AutoCompleteTextView optionCheckIn, optionAddService;
    ArrayList<String> arrayList_option, arrayListService;
    ArrayList<Integer> arrayListIdService;
    ArrayAdapter<String> arrayAdapter_option, arrayAdapterService;

    ArrayList<Integer> serviceList = new ArrayList<>();
    String[] serviceArr;
    String serviceIdArr;

    public RoomAdapter(Context context, int layout, List<Room> roomList) {
        this.context = context;
        this.layout = layout;
        RoomList = roomList;
    }

    @Override
    public int getCount() {
        return RoomList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    private static class ViewHolder {
        TextView txtTenPhong, txtTang, txtTrangThai, txtGiaPhong;
        ImageView imageEdit;
        RelativeLayout layoutRoom;

    }

    public List<Room> getData() {
        return RoomList;
    }

    @SuppressLint({"SetTextI18n", "RtlHardcoded", "NonConstantResourceId"})
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(layout, null);
            holder.txtTenPhong = convertView.findViewById(R.id.textViewTenPhongCustom);
            holder.txtTang = convertView.findViewById(R.id.textViewTangCustom);
            holder.txtTrangThai = convertView.findViewById(R.id.textViewTrangThaiCustom);
            holder.txtGiaPhong = convertView.findViewById(R.id.textViewGiaPhongCustom);
            holder.imageEdit = convertView.findViewById(R.id.imageViewEditRoom);
//            holder.imageDelete = (ImageView) convertView.findViewById(R.id.imageViewDeleteRoom);
            holder.layoutRoom = convertView.findViewById(R.id.layoutRoom);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        Room room = RoomList.get(position);
        holder.txtTenPhong.setText(room.getTenPhong());
        holder.txtTang.setText("" + room.getTang());
        holder.txtGiaPhong.setText(room.getGia() + "");
        if (room.getTrangThai() == 0) {
            holder.txtTrangThai.setText("Trống");
//            holder.layoutRoom.setBackgroundColor(Color.parseColor("#DBF1FB"));
            GradientDrawable gradientDrawable = (GradientDrawable) holder.layoutRoom.getBackground().mutate();
            gradientDrawable.setColor(Color.parseColor("#F9FEFF"));
        } else {
            holder.txtTrangThai.setText("Đã dùng");
//            holder.layoutRoom.setBackgroundColor(Color.parseColor("#a9ffeb"));
            GradientDrawable gradientDrawable = (GradientDrawable) holder.layoutRoom.getBackground().mutate();
            gradientDrawable.setColor(Color.parseColor("#F4DFDF"));
        }
        if (room.getTuSua() == 1) {
            holder.txtTrangThai.setText("Đang sửa!");
//            holder.layoutRoom.setBackgroundColor(Color.parseColor("#65c6bb#8ce3da#AEEAE4"));
            GradientDrawable gradientDrawable = (GradientDrawable) holder.layoutRoom.getBackground().mutate();
            gradientDrawable.setColor(Color.parseColor("#C3EFEB"));
        }

        SharedPreferences preferences = context.getApplicationContext().getSharedPreferences("tokenLogin", Context.MODE_PRIVATE);
        permission = preferences.getInt("permission", 0);
        token = preferences.getString("token", "");

        holder.imageEdit.setOnClickListener(v -> {
            PopupMenu popup = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
                popup = new PopupMenu(context, v, Gravity.RIGHT);
            }
            popup.inflate(R.menu.menu_manage_room);
            popup.show();
            popup.setOnMenuItemClickListener(item -> {
                int id = item.getItemId();
                switch (id) {
                    case R.id.checkin:
                        if (room.getTuSua() == 1) {
                            AlertRoomIsFixed();
                        } else if (room.getTrangThai() == 1) {
                            AlertRoomIsUsed();
                        } else {
                            DialogCheckIn(room);
                        }
                        return true;
                    case R.id.checkout:
                        if (room.getTuSua() == 1) {
                            AlertRoomIsFixed();
                        } else if (room.getTrangThai() == 0) {
                            AlertRoomIsEmpty();
                        } else {
                            DialogCheckOut(room);
                        }
                        return true;
                    case R.id.addService:
                        if (room.getTuSua() == 1) {
                            AlertRoomIsFixed();
                        } else if (room.getTrangThai() == 0) {
                            AlertRoomIsEmpty();
                        } else {
                            DialogAddService(room);
                        }
                        return true;
                }
                return true;
            });
        });


        return convertView;
    }

    private void DialogCheckIn(Room room) {
        Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.check_in_room);

        Window window = dialog.getWindow();
        if (window == null) {
            return;
        }

        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        WindowManager.LayoutParams windowAttributes = window.getAttributes();
        windowAttributes.gravity = Gravity.CENTER;
        window.setAttributes(windowAttributes);
        dialog.show();


        Button btnAdd = dialog.findViewById(R.id.btn_submit_checkIn);
        Button btnCannel = dialog.findViewById(R.id.btn_cancel_checkIn);

        maPhongEdt = dialog.findViewById(R.id.editTextSoPhongCheckIn);
        tenEdt = dialog.findViewById(R.id.editTextTenKhachCheckIn);
        cmtEdt = dialog.findViewById(R.id.editTextCMTCheckIn);
        sdtEdt = dialog.findViewById(R.id.editTextSDTCheckIn);
        diachiEdt = dialog.findViewById(R.id.editTextDiaChiCheckIn);
        optionCheckIn = dialog.findViewById(R.id.optionsCheckInMenu);

        arrayList_option = new ArrayList<>();
        arrayList_option.add("Theo ngày");
        arrayList_option.add("Theo giờ");
        arrayAdapter_option = new ArrayAdapter<>(context.getApplicationContext(), R.layout.dropdown_item_ci, arrayList_option);
        optionCheckIn.setAdapter(arrayAdapter_option);
        optionCheckIn.setThreshold(1);

        optionCheckIn.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String check = parent.getItemAtPosition(position).toString();
                if (check.matches("Theo giờ")) {
                    option = "TheoGio";
                } else {
                    option = "TheoPhong";
                }
//                Toast.makeText(parent.getContext(), option, Toast.LENGTH_SHORT).show();
            }
        });

        maPhongEdt.setText(room.getTenPhong());

        btnCannel.setOnClickListener(v -> dialog.dismiss());

        btnAdd.setOnClickListener(v -> {
            ten = tenEdt.getText().toString().trim();
            cmt = cmtEdt.getText().toString().trim();
            sdt = sdtEdt.getText().toString().trim();
            diachi = diachiEdt.getText().toString().trim();
            if (ten.matches("") || cmt.length() == 0 || sdt.length() == 0 || diachi.matches("")) {
                StyleableToast.makeText(context, "Vui lòng nhập đầy đủ thông tin!", Toast.LENGTH_SHORT, R.style.toastStyle).show();
            } else {
                checkIn(urlCheckIn, room.getId(), dialog);
            }
        });

    }

    private void DialogCheckOut(Room room) {
        Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.check_out_room);

        Window window = dialog.getWindow();
        if (window == null) {
            return;
        }
        int idPhong = room.getId();
        SharedPreferences preferences = context.getApplicationContext().getSharedPreferences(SHARED_ID_INVOICE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.apply();
        int idInvoice = preferences.getInt(String.valueOf(idPhong), 0);
//        StyleableToast.makeText(context, String.valueOf(idInvoice), Toast.LENGTH_SHORT, R.style.toastSuccess2).show();

        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        WindowManager.LayoutParams windowAttributes = window.getAttributes();
        windowAttributes.gravity = Gravity.CENTER;
        window.setAttributes(windowAttributes);
        dialog.show();

        maPhongEdtCO = dialog.findViewById(R.id.editTextSoPhongCheckOut);
        tenEdtCO = dialog.findViewById(R.id.editTextTenKhachCheckOut);
        cmtEdtCO = dialog.findViewById(R.id.editTextCMTCheckOut);
        diachiEdtCO = dialog.findViewById(R.id.editTextDiaChiCheckOut);
        sdtEdtCO = dialog.findViewById(R.id.editTextSDTCheckOut);
        thanhtoanEdt = dialog.findViewById(R.id.editTextThanhToanCheckOut);

//        String payments = NumberFormat.getNumberInstance(Locale.US).format(room.getGia());

        maPhongEdtCO.setText(room.getTenPhong());
//        thanhtoanEdt.setText(payments+ " VND");
        getInvoiceInfo(urlGetInvoiceInfo + idInvoice);

        Button btnSubmitCheckOut = dialog.findViewById(R.id.btn_submit_checkout);
        Button btnCancel = dialog.findViewById(R.id.btn_cancel_checkout);
        btnCancel.setOnClickListener(v -> dialog.dismiss());

        btnSubmitCheckOut.setOnClickListener(v -> {
            ten = tenEdtCO.getText().toString().trim();
            cmt = cmtEdtCO.getText().toString().trim();
            sdt = sdtEdtCO.getText().toString().trim();
            diachi = diachiEdtCO.getText().toString().trim();
//            if (ten.matches("") || cmt.length() == 0 || sdt.length() == 0 || diachi.matches("")) {
            if (ten.matches("") || sdt.length() == 0 ) {
                StyleableToast.makeText(context, "Không thể để trống thông tin checkout!", Toast.LENGTH_SHORT, R.style.toastStyle).show();
            } else {
                confirmCheckOut(dialog, idInvoice, idPhong);
            }
        });

    }

    public void confirmCheckOut(Dialog dialogCheckOut, int idInvoice, int idPhong) {
        AlertDialog.Builder dialogDelInvoice = new AlertDialog.Builder(context);
        dialogDelInvoice.setMessage("Xác nhận đã thanh toán và thực hiện lưu hóa đơn?");
        dialogDelInvoice.setNegativeButton("Xác nhận", (dialog, which) -> checkOut(urlCheckOut, idInvoice, idPhong, dialogCheckOut));
        dialogDelInvoice.setPositiveButton("Không", (dialog, which) -> {

        });
        dialogDelInvoice.show();
    }

    private void DialogAddService(Room room) {
        Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.add_service_to_room);

        Window window = dialog.getWindow();
        if (window == null) {
            return;
        }

        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        WindowManager.LayoutParams windowAttributes = window.getAttributes();
        windowAttributes.gravity = Gravity.CENTER;
        window.setAttributes(windowAttributes);
        dialog.show();


        int idPhong = room.getId();
        SharedPreferences preferences = context.getApplicationContext().getSharedPreferences(SHARED_ID_INVOICE, Context.MODE_PRIVATE);
        int idInvoice = preferences.getInt(String.valueOf(idPhong), 0);
//        StyleableToast.makeText(context, String.valueOf(idInvoice), Toast.LENGTH_SHORT, R.style.toastSuccess2).show();

        maPhongAddService = dialog.findViewById(R.id.editTextSoPhongAddService);
        optionAddService = dialog.findViewById(R.id.optionAddServiceMenu);
        multiSelectService = dialog.findViewById(R.id.btnAddMultiService);
        arrayListService = new ArrayList<>();
        arrayListIdService = new ArrayList<>();
//        arrayListService.add("Giặt ủi quần áo");
//        arrayListService.add("Spa");

        maPhongAddService.setText(room.getTenPhong());
        getServiceInfo(urlGetServiceInfo);

        arrayAdapterService = new ArrayAdapter<>(context.getApplicationContext(), R.layout.dropdown_item_ci, arrayListService);
        optionAddService.setAdapter(arrayAdapterService);
        optionAddService.setThreshold(1);


        optionAddService.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String check = parent.getItemAtPosition(position).toString();
                for (int i = 0; i < arrayListService.size(); i++) {
                    if (check.matches(arrayListService.get(i))) {
                        optionServiceID = arrayListIdService.get(i);
                        serviceIdArr = String.valueOf(optionServiceID);
                    }
                    ;
                }
//                Toast.makeText(parent.getContext(), String.valueOf(optionServiceID), Toast.LENGTH_SHORT).show();
            }
        });


        multiSelectService.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("Chọn dịch vụ")
                    .setCancelable(false)
                    .setMultiChoiceItems(serviceArr, selectedService, new DialogInterface.OnMultiChoiceClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int i, boolean isChecked) {
                            if (isChecked) {
                                if (!serviceList.contains(i)) {
                                    serviceList.add(i);
                                }
                            } else {
                                for (int j = 0; j < serviceList.size(); j++) {
                                    if (serviceList.get(j) == i) {
                                        serviceList.remove(j);
                                    }
                                    ;
                                }
                            }
                        }
                    });
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    String item = "";
                    serviceIdArr = "";
                    for (int i = 0; i < serviceList.size(); i++) {
                        int position = serviceList.get(i);
                        item = item + serviceArr[position];
                        serviceIdArr = serviceIdArr + arrayListIdService.get(position);
                        if (i != serviceList.size() - 1) {
                            item = item + ", ";
                            serviceIdArr = serviceIdArr + ", ";
                        }
                    }
                    optionAddService.setText(item);
//                         Toast.makeText(context, serviceIdArr, Toast.LENGTH_SHORT).show();
                    StyleableToast.makeText(context, serviceIdArr, Toast.LENGTH_SHORT, R.style.toastSuccess2).show();

                }
            });
            builder.setNegativeButton("Cannel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });

            builder.setNeutralButton("Clear All", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    for (int j = 0; j < selectedService.length; j++) {
                        selectedService[j] = false;
                        serviceList.clear();
                        serviceIdArr = "";
                        optionAddService.setText("");
                    }
                }
            });
            builder.show();
        });

        Button btnAdd = dialog.findViewById(R.id.btn_add_service_room);
        Button btnCannel = dialog.findViewById(R.id.btn_cancel_add_service);
        btnCannel.setOnClickListener(v -> dialog.dismiss());

        btnAdd.setOnClickListener(v -> addService(urlAddService, idInvoice, dialog));

    }

    public void AlertRoomIsUsed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage("Phòng đã được sử dụng!")
                .setNegativeButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .show();
    }

    public void AlertRoomIsEmpty() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage("Phòng trống!")
                .setNegativeButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        builder.show();
    }

    public void AlertRoomIsFixed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Thông báo")
                .setMessage("Phòng đang tạm dừng hoạt động!")
                .setNegativeButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        builder.show();
    }

    private void checkIn(String url, int idPhong, Dialog dialog) {
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            String msg = obj.getString("msg");
                            String status = obj.getString("status");
                            JSONObject dataID = obj.getJSONObject("data");
                            int idInvoice = dataID.getInt("ID");
                            SharedPreferences preferences = context.getApplicationContext().getSharedPreferences(SHARED_ID_INVOICE, Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = preferences.edit();
                            editor.putInt(String.valueOf(idPhong), idInvoice);
                            editor.apply();
                            if (status.equals("success")) {
                                Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                                if (permission == 2) {
                                    ListRoomFragment optionsFrag = new ListRoomFragment();
                                    ((Home) context).getSupportFragmentManager()
                                            .beginTransaction()
                                            .replace(R.id.nav_host_fragment, optionsFrag, "")
                                            .addToBackStack(null)
                                            .commit();
                                } else {
                                    ListRoomStaffFragment optionsFrag = new ListRoomStaffFragment();
                                    ((Home) context).getSupportFragmentManager()
                                            .beginTransaction()
                                            .replace(R.id.nav_host_fragment, optionsFrag, "")
                                            .addToBackStack(null)
                                            .commit();
                                }

                            } else {
                                Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
                            }

                        } catch (Throwable t) {
                            Toast.makeText(context, "Could not parse malformed JSON: \"" + response + "\"", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("AAA", "Lỗi:\n" + error.toString());
                        Toast.makeText(context, error.toString(), Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("token", token);
                params.put("IDPhong", String.valueOf(idPhong));
                params.put("Ten", tenEdt.getText().toString().trim());
                params.put("CMT", cmtEdt.getText().toString().trim());
                params.put("DiaChi", diachiEdt.getText().toString().trim());
                params.put("SDT", sdtEdt.getText().toString().trim());
                params.put("DichVu", "5");
                params.put("Option", option);
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }

    public void getInvoiceInfo(String url) {
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONObject json = response.getJSONObject("data");
                            String ThanhToan = json.getString("ThanhToan");
                            JSONObject infoKhach = json.getJSONObject("infoKhach");
                            thanhtoanEdt.setText(ThanhToan);
                            tenEdtCO.setText(infoKhach.getString("Ten"));
                            cmtEdtCO.setText(infoKhach.getString("CMT"));
                            sdtEdtCO.setText(infoKhach.getString("SDT"));
                            diachiEdtCO.setText(infoKhach.getString("DiaChi"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        Toast.makeText(context, error.toString(), Toast.LENGTH_SHORT).show();
                    }
                }
        );
        requestQueue.add(request);
    }

    private void checkOut(String url, int idInvoice, int idPhong, Dialog dialog) {
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            String msg = obj.getString("msg");
                            String status = obj.getString("status");
//                            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
                            if (status.equals("success")) {
                                StyleableToast.makeText(context, msg, Toast.LENGTH_SHORT, R.style.toastSuccess2).show();
                                SharedPreferences preferences = context.getApplicationContext().getSharedPreferences(SHARED_ID_INVOICE, Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = preferences.edit();
                                editor.remove(String.valueOf(idPhong));
                                editor.apply();
                                dialog.dismiss();
                                if (permission == 2) {
                                    ListRoomFragment optionsFrag = new ListRoomFragment();
                                    ((Home) context).getSupportFragmentManager()
                                            .beginTransaction()
                                            .replace(R.id.nav_host_fragment, optionsFrag, "")
                                            .addToBackStack(null)
                                            .commit();
                                } else {
                                    ListRoomStaffFragment optionsFrag = new ListRoomStaffFragment();
                                    ((Home) context).getSupportFragmentManager()
                                            .beginTransaction()
                                            .replace(R.id.nav_host_fragment, optionsFrag, "")
                                            .addToBackStack(null)
                                            .commit();
                                }
                            } else {
                                Toast.makeText(context, msg.toString(), Toast.LENGTH_SHORT).show();
                            }

                        } catch (Throwable t) {
                            Toast.makeText(context, "Could not parse malformed JSON: \"" + response + "\"", Toast.LENGTH_SHORT).show();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("AAA", "Lỗi:\n" + error.toString());
                        Toast.makeText(context, error.toString(), Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("token", token);
                params.put("ID", String.valueOf(idInvoice));
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }


    private void getServiceInfo(String url) {
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        JSONArray jsonArray = response.getJSONArray("data");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            try {
                                JSONObject data = jsonArray.getJSONObject(i);
                                int status = data.getInt("TrangThai");
                                if (status == 1) {                    //lay service hien co
                                    arrayListService.add(data.getString("TenDV"));
                                    arrayListIdService.add(data.getInt("ID"));
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
//                            StyleableToast.makeText(context, arrayListService.toString(), Toast.LENGTH_SHORT, R.style.toastSuccess2).show();
                        }
                        serviceArr = arrayListService.toArray(new String[0]);
                        selectedService = new boolean[serviceArr.length];
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> {
                    error.printStackTrace();
                    Toast.makeText(context, error.toString(), Toast.LENGTH_SHORT).show();
                }
        );
        requestQueue.add(request);
    }

    private void addService(String url, int idInvoice, Dialog dialog) {
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            String msg = obj.getString("msg");
                            String status = obj.getString("status");
                            if (status.equals("success")) {
                                StyleableToast.makeText(context, "Thêm dịch vụ vào hóa đơn thành công!", Toast.LENGTH_SHORT, R.style.toastSuccess2).show();
                                dialog.dismiss();
                                if (permission == 2) {
                                    ListRoomFragment optionsFrag = new ListRoomFragment();
                                    ((Home) context).getSupportFragmentManager()
                                            .beginTransaction()
                                            .replace(R.id.nav_host_fragment, optionsFrag, "")
                                            .addToBackStack(null)
                                            .commit();
                                } else {
                                    ListRoomStaffFragment optionsFrag = new ListRoomStaffFragment();
                                    ((Home) context).getSupportFragmentManager()
                                            .beginTransaction()
                                            .replace(R.id.nav_host_fragment, optionsFrag, "")
                                            .addToBackStack(null)
                                            .commit();
                                }

                            } else {
                                Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
                            }

                        } catch (Throwable t) {
                            Toast.makeText(context, "Could not parse malformed JSON: \"" + response + "\"", Toast.LENGTH_SHORT).show();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("AAA", "Lỗi:\n" + error.toString());
                        Toast.makeText(context, error.toString(), Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("token", token);
                params.put("ID", String.valueOf(idInvoice));
                params.put("DichVu", serviceIdArr);
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }

    public void hideKeyBoard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

}
