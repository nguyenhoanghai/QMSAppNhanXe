package com.example.gproqmsnhanxe;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Cache;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Network;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PrintActivity extends AppCompatActivity {
    Integer id = 0, count = 0, sexIndex = 0;
    String   headName = "Cửa hàng", data = "", searchText, searchType ;
    ConfigModel configModel ;

    Cache cache = null;
    Network network = null;
    public RequestQueue mRequestQueue = null;
    Intent intent;
    ProgressDialog progressDialog;
    EditText txtbso, txtkm, txtsomay, txtsokhung, txtnote;
    Spinner cbdichvu;
    Button btsave, btback;
    List<DichVu> Dichvus;
    DichVu dvuSelected = null;
    ArrayAdapter adapter;
    KhachHang khachHang;
    JSONArray jsonServices;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_print);
        configModel = new ConfigModel();
        intent = getIntent();
        configModel = (ConfigModel) intent.getSerializableExtra("ConfigModel");
        khachHang = (KhachHang) intent.getSerializableExtra("KHang");
        data = intent.getStringExtra("data");
        searchText = intent.getStringExtra("sText");
        searchType = intent.getStringExtra("sType");

        getSupportActionBar().setTitle("TẠO PHIẾU DỊCH VỤ");

        progressDialog = new ProgressDialog(PrintActivity.this);
        progressDialog.setTitle("Loading...");
        progressDialog.setMessage("Đang tải dữ liệu...");

        cache = new DiskBasedCache(getCacheDir(), 1024 * 1024);
        network = new BasicNetwork(new HurlStack());
        mRequestQueue = new RequestQueue(cache, network);
        mRequestQueue.start();

        initView();
        GetServices();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            goBack(true);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void goBack(boolean useOldData) {
        intent = new Intent(PrintActivity.this, HomeActivity.class);
        intent.putExtra("data", data);
        intent.putExtra("ConfigModel", configModel);
        intent.putExtra("sText", (useOldData? searchText:""));
        intent.putExtra("sType", searchType);
        startActivity(intent);
    }

    private void initView() {
        txtbso = (EditText) findViewById(R.id.txtbso);
        txtbso.setText(khachHang.getLicensePlate());
        txtsokhung = (EditText) findViewById(R.id.txtsokhung);
        txtsomay = (EditText) findViewById(R.id.txtsomay);
        txtnote = (EditText) findViewById(R.id.txtnote);
        txtkm = (EditText) findViewById(R.id.txtkm);
/*
        String jsonTinh = "[" +
                "{\"Id\":\"1\",\"Code\":\"KTDK\",\"Name\":\"Kiểm tra định kỳ\"}," +
                "{\"Id\":\"2\",\"Code\":\"KTDKTC\",\"Name\":\"Kiểm tra định kỳ thiện chí\"}," +
                "{\"Id\":\"3\",\"Code\":\"SCLD\",\"Name\":\"Sửa chữa lưu động\"}," +
                "{\"Id\":\"4\",\"Code\":\"SCPT\",\"Name\":\"Sửa chữa - thay thế phụ tùng\"}," +
                "{\"Id\":\"5\",\"Code\":\"SCTC\",\"Name\":\"Sửa chữa thiện chí\"}," +
                "{\"Id\":\"6\",\"Code\":\"XEBH\",\"Name\":\"Sửa chữa xe bảo hiểm\"}," +
                "{\"Id\":\"7\",\"Code\":\"XEBHANH\",\"Name\":\"Sửa chữa xe bảo hành\"}," +
                "{\"Id\":\"8\",\"Code\":\"XETN\",\"Name\":\"Sửa chữa xe tai nạn\"}," +
                "]";
        Dichvus = new ArrayList<>();
        try {
            JSONArray jsonArray = new JSONArray(jsonTinh);
            JSONObject object;
            if (jsonArray != null && jsonArray.length() > 0) {
                for (int i = 0; i < jsonArray.length(); i++) {
                    object = null;
                    try {
                        object = jsonArray.getJSONObject(i);
                        if (object != null) {
                            Dichvus.add(new DichVu(
                                    object.optInt("Id"),
                                    object.optString("Name"),
                                    object.optString("Code")
                            ));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

       cbdichvu = (Spinner) findViewById(R.id.cbdichvu);
        adapter = new ArrayAdapter(PrintActivity.this, android.R.layout.simple_list_item_1, Dichvus);
        cbdichvu.setAdapter(adapter);
        cbdichvu.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                dvuSelected = Dichvus.get(position);
                int i = dvuSelected.getId();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        */

        btback = (Button) findViewById(R.id.btback);
        btback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goBack(true);
            }
        });

        btsave = (Button) findViewById(R.id.btsave);
        //region event
        btsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(configModel.getCustCode()   ==  khachHang.getCode() && txtbso.getText().toString() == ""){
                    Toast.makeText(PrintActivity.this, "Lỗi nhập liệu: Vui lòng nhập biển số xe."  , Toast.LENGTH_LONG).show();
                }else
                {
                    progressDialog.show();
                    try {
                        String url = (configModel.getAPIIP()   + "/api/v1/customers/createserviceorder");
                        StringRequest jRequest = new StringRequest(
                                Request.Method.POST, url,
                                new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        progressDialog.hide();
                                        mRequestQueue.stop();
                                        mRequestQueue = null;
                                        try {
                                            JSONObject _obj = new JSONObject(response);
                                            switch (_obj.getInt("statusCode")) {
                                                case 200:
                                                    String rs = _obj.getString("data");
                                                    TaoPhieuQMS(rs);
                                                    Toast.makeText(PrintActivity.this, "Tạo dịch vụ thành công. mã : " + rs, Toast.LENGTH_LONG).show();
                                                    break;
                                                case 404:
                                                case 500:
                                                    Toast.makeText(PrintActivity.this, "Lỗi " + _obj.getInt("statusCode") + ": " + _obj.getString("statusText"), Toast.LENGTH_LONG).show();
                                                    break;
                                            }
                                        } catch (JSONException e) {
                                            progressDialog.hide();
                                            e.printStackTrace();
                                        }
                                    }
                                },
                                new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        progressDialog.hide();
                                        Toast.makeText(PrintActivity.this, "Method: createserviceorder /n StatusCode :" + error.networkResponse.statusCode, Toast.LENGTH_LONG).show();
                                        // Login_TienThu(true);
                                    }
                                }
                        ) {
                            @Override
                            protected Map<String, String> getParams() {
                                Map<String, String> params = new HashMap<>();
                                params.put("CustAccount", khachHang.getCode());
                                params.put("PlateId", txtbso.getText().toString());
                                params.put("CurrentKM", txtkm.getText().toString());
                                params.put("ServicePool", dvuSelected.getCode());
                                params.put("DimensionStore",configModel.getHeadCode()  );
                                params.put("CustRef", txtnote.getText().toString());
                                params.put("EngineID", txtsomay.getText().toString());
                                params.put("PersonnelNumberId", configModel.getUserCode()  );
                                params.put("InventSerialId", txtsokhung.getText().toString());
                                return params;
                            }

                            @Override
                            public Map<String, String> getHeaders() throws AuthFailureError {
                                Map<String, String> headers = new HashMap<String, String>();
                                headers.put("Authorization", "Bearer " + configModel.getTOKEN()  );
                                return headers;
                            }

                            @Override
                            public String getBodyContentType() {
                                return "application/x-www-form-urlencoded; charset=UTF-8";
                            }
                        };
                        jRequest.setShouldCache(false);
                        jRequest.setRetryPolicy(new DefaultRetryPolicy(86399, 20, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                        if (mRequestQueue == null) {
                            mRequestQueue = new RequestQueue(cache, network);
                            mRequestQueue.start();
                        }
                        mRequestQueue.add(jRequest);
                    }
                    catch (Exception e) {
                        progressDialog.hide();
                    }
                }
            }
        });
        //endregion
    }

    private void TaoPhieuQMS(String maphieuDV) {
        progressDialog.show();
        //region
        String str =configModel.getQMSIP()   + "/api/tienthu/PrintTicket?maphieudichvu=" + maphieuDV
                + "&madichvu=" + dvuSelected.getId()
                + "&diachi=" + khachHang.getAddress()
                + "&ten=" + khachHang.getName()
                + "&maKH=" + khachHang.getCode()
                + "&soxe=" + khachHang.getLicensePlate()
                + "&phone=" + khachHang.getPhone();

        RequestQueue rqQue = Volley.newRequestQueue(PrintActivity.this);
        JsonObjectRequest jRequest = new JsonObjectRequest(
                Request.Method.GET, str, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        progressDialog.hide();
                        Boolean rs = response.optBoolean("IsSuccess");
                        if (rs){
                            Toast.makeText(PrintActivity.this, "Gửi yêu cầu cấp phiếu thành công. STT: "+response.optInt("Data"), Toast.LENGTH_SHORT).show();
                            goBack(false);
                        }
                        else
                            Toast.makeText(PrintActivity.this, "Gửi yêu cầu cấp phiếu thất bại.", Toast.LENGTH_SHORT).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.hide();
                        Toast.makeText(PrintActivity.this, "Gửi YC cấp phiếu : Không kết nối được với máy chủ QMS.", Toast.LENGTH_SHORT).show();
                    }
                }
        );
        jRequest.setShouldCache(false);
        jRequest.setRetryPolicy(new DefaultRetryPolicy(86399, 20, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        if (mRequestQueue == null) {
            mRequestQueue = new RequestQueue(cache, network);
            mRequestQueue.start();
        }
        mRequestQueue.add(jRequest);
    }

    private void GetServices( ) {
        Dichvus = new ArrayList<>();
        String str = (configModel.getQMSIP()   + "/api/serviceapi/getservices");
        RequestQueue rqQue = Volley.newRequestQueue(PrintActivity.this);
        JsonArrayRequest jRequest = new JsonArrayRequest(
                Request.Method.GET, str, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        if (response != null) {
                            jsonServices = response;
                            //  arrServices = new String[response.length()];
                            for (int ii = 0; ii < response.length(); ii++) {
                                try {
                                    JSONObject jsonObject = response.getJSONObject(ii);
                                    //  arrServices[ii] = jsonObject.getString("Name");

                                    if (jsonObject != null) {
                                        Dichvus.add(new DichVu(
                                                jsonObject.optInt("Id"),
                                                jsonObject.optString("Name"),
                                                jsonObject.optString("Data1")
                                        ));
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                            InitSpinerService();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(PrintActivity.this, "Lấy Dịch vụ : Không kết nối được với máy chủ.", Toast.LENGTH_SHORT).show();
                    }
                }
        );
        jRequest.setShouldCache(false);
        jRequest.setRetryPolicy(new DefaultRetryPolicy(20000, 20, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        if (mRequestQueue == null) {
            mRequestQueue = new RequestQueue(cache, network);
            mRequestQueue.start();
        }
        mRequestQueue.add(jRequest);
    }

    private void InitSpinerService() {
        cbdichvu = (Spinner) findViewById(R.id.cbdichvu);
        try {
              adapter = new ArrayAdapter(PrintActivity.this, android.R.layout.simple_list_item_2, Dichvus);
             cbdichvu.setAdapter(adapter);
        }
       catch (Exception ex){}

        cbdichvu.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                dvuSelected = Dichvus.get(position);
                int i = dvuSelected.getId();
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
    }
}