package com.example.gproqmsnhanxe;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class HomeActivity extends AppCompatActivity {

    EditText txtcode;
    private Button btScan,  btfind, btexit, btconfig, btprintQMS;
    private EditText txt;
    boolean isStop = false;

    Integer id = 0,count =0;
    String code = "", searchText = "", searchType = "",   searchData ="" ;

    ConfigModel configModel;

    Cache cache = null;
    Network network = null;
    public RequestQueue mRequestQueue = null;
    Intent intent;

    RadioGroup radioGroup;
    RadioButton raBSXe,raSDThoai;

    ProgressDialog progressDialog;

    ArrayList<KhachHang> khachHangs  = new ArrayList<>();;
    ListView lvKH;
    KHListViewAdapter adapter;
    LinearLayout resultbox;
    KhachHang selectecKH;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        setTitle("CẤP SỐ TỰ ĐỘNG GPRO-QMS");
        configModel = new ConfigModel();
        getAppConfig();

        progressDialog = new ProgressDialog(HomeActivity.this);
        progressDialog.setTitle("Loading...");
        progressDialog.setMessage("Đang tải dữ liệu...");

        // Instantiate the cache
        cache = new DiskBasedCache(getCacheDir(), 1024 * 1024); // 1MB cap
        // Set up the network to use HttpURLConnection as the HTTP client.
        network = new BasicNetwork(new HurlStack());
        // Instantiate the RequestQueue with the cache and network.
        mRequestQueue = new RequestQueue(cache, network);
        mRequestQueue.start();

        initView();

        intent = getIntent();
        String response = intent.getStringExtra("data");
        if(response != null && response !=""){
            try {
                JSONObject _obj = new JSONObject(response);
                JSONArray jsonArray = _obj.getJSONArray("data");
                if (jsonArray.length() > 0) {
                    for (int i = 0; i < jsonArray.length(); i++) {
                        String[] str = jsonArray.getString(i).split(";");
                        if (str.length == 6) {
                            khachHangs.add(new KhachHang(
                                    str[1],
                                    str[0],
                                    str[3],
                                    str[2],
                                    str[4],
                                    Integer.parseInt(str[5])
                            ));
                        }
                    }
                    adapter = new KHListViewAdapter(HomeActivity.this, khachHangs);
                    lvKH.setAdapter(adapter);
                    resultbox.setVisibility(View.VISIBLE);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            searchText = intent.getStringExtra("sText");
            txtcode.setText(searchText);
            searchType = intent.getStringExtra("sType");
            configModel = (ConfigModel) intent.getSerializableExtra("ConfigModel");

            switch (searchType){
                case "LicensePlate": raBSXe.setChecked(true); break;
                case "CustomerPhone": raSDThoai.setChecked(true); break;
            }
            if(searchText.compareTo("") == 0)
                resultbox.setVisibility(View.INVISIBLE);
        }

        if(configModel== null || configModel.getTOKEN() == null || configModel.getTOKEN() == "")
            Login_TienThu(false);
    }

    private void initView() {
        resultbox = (LinearLayout)findViewById(R.id.resultbox);
        resultbox.setVisibility(View.GONE);
        radioGroup = (RadioGroup)findViewById(R.id.radioGroup);
        raBSXe = (RadioButton) findViewById(R.id.raBienSo);
        raSDThoai = (RadioButton)findViewById(R.id.raDienThoai);
        txtcode = (EditText) findViewById(R.id.txtcode);

        btScan = (Button) findViewById(R.id.btscan);
        btScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentIntegrator intentIntegrator = new IntentIntegrator(HomeActivity.this);
                intentIntegrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
                intentIntegrator.setCameraId(0);
                intentIntegrator.setOrientationLocked(false);
                intentIntegrator.setPrompt("Scanning");
                intentIntegrator.setBeepEnabled(true);
                intentIntegrator.setBarcodeImageEnabled(true);
                intentIntegrator.initiateScan();
            }
        });

        btfind = (Button) findViewById(R.id.btfind);
        btfind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                code = txtcode.getText().toString();
                FindCustomer_TienThu();
            }
        });

        btexit = (Button) findViewById(R.id.btexit);
        btexit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exitApp();
            }
        });

        btconfig = (Button) findViewById(R.id.btconfig);
        btconfig.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(HomeActivity.this,AppConfigActivity.class);
                startActivity(intent);
            }
        });

        btprintQMS = (Button) findViewById(R.id.btprintQMS);
        btprintQMS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(HomeActivity.this,PrintQMSOnlyActivity.class);
                intent.putExtra("ConfigModel",  configModel);
                startActivity(intent);
            }
        });

        lvKH = (ListView) findViewById(R.id.lvKH);
        lvKH.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                KhachHang _kh = khachHangs.get(position);
                if(raSDThoai.isChecked()){
                    FindCustHistories(_kh.getCode(),_kh.getName(),_kh.getAddress(),_kh.getLicensePlate(), _kh.getTimes());
                }

                intent = new Intent(HomeActivity.this, PrintActivity.class);
                intent.putExtra("data", searchData);
                intent.putExtra("maKH", khachHangs.get(position).getCode());
                intent.putExtra("sText", searchText);
                intent.putExtra("sType", searchType);
                intent.putExtra("KHang", khachHangs.get(position));
                intent.putExtra("ConfigModel",  configModel);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
       /* if(requestCode == ScanningActivity.SCANNING_FOR_PRINTER &&
                resultCode == Activity.RESULT_OK){
            initPrinter();
        }
        else {*/
        final IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null && result.getContents() != null) {
            // Toast.makeText(HomeActivity.this, result.getContents(), Toast.LENGTH_LONG).show();
            progressDialog.show();
            code = result.getContents();
            txtcode.setText( result.getContents());
            FindCustomer_TienThu( );
        }
        //}
    }

    private void Login_TienThu(final boolean isFindCustomer ) {
        try {
            String url =  (configModel.getAPIIP()  +"/api/v1/oauth/token");
            StringRequest jRequest = new StringRequest(
                    Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            progressDialog.hide();
                            mRequestQueue.stop();
                            mRequestQueue = null;
                            isStop = true;
                            try {
                                JSONObject _obj = new JSONObject(response);
                                configModel.setTOKEN(_obj.getString("access_token"));
                                //Toast.makeText(HomeActivity.this, "Đăng nhập vào server TienThu thành công", Toast.LENGTH_LONG).show();
                                if(isFindCustomer)
                                    FindCustomer_TienThu();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            progressDialog.hide();
                            Toast.makeText(HomeActivity.this, "Đăng nhập vào server TienThu thất bại", Toast.LENGTH_LONG).show();
                        }
                    }
            ){
                @Override
                protected Map<String, String> getParams()
                {
                    Map<String,String> params = new HashMap<>();
                    params.put("username",configModel.getUserName()  );
                    params.put("password", configModel.getPassword()  );
                    params.put("grant_type","password");
                    return params;
                }
            };
            jRequest.setShouldCache(false);
            jRequest.setRetryPolicy(new DefaultRetryPolicy(86399 , 20, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            if (mRequestQueue == null) {
                mRequestQueue = new RequestQueue(cache, network);
                mRequestQueue.start();
            }
            mRequestQueue.add(jRequest);
        } catch (Exception e) {
            progressDialog.hide();
        }
    }

    private void FindCustomer_TienThu( ) {
        if(configModel.getTOKEN() == "")
            Login_TienThu(true);
        else {
            try {
                khachHangs.clear();
                searchText = txtcode.getText().toString();
                if(raBSXe.isChecked()){
                    searchType  = "LicensePlate";
                }
                else if(raSDThoai.isChecked()){
                    searchType  ="CustomerPhone";
                }
                String url =configModel.getAPIIP()  + ("/api/v1/customers/searchcustomers?searchtext="+searchText+"&searchtype="+searchType);
                RequestQueue rqQue = Volley.newRequestQueue(HomeActivity.this);
                StringRequest jRequest = new StringRequest(
                        Request.Method.GET, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                progressDialog.hide();
                                mRequestQueue.stop();
                                mRequestQueue = null;
                                isStop = true;
                                try {
                                    JSONObject _obj = new JSONObject(response);
                                    if(_obj.getInt("statusCode") == 200){
                                        searchData = response;
                                        JSONArray jsonArray = _obj.getJSONArray("data");
                                        if(jsonArray.length()>0){
                                            for (int i = 0; i < jsonArray.length(); i++) {
                                                String[] str = jsonArray.getString(i).split(";");
                                                if (str.length == 6) {
                                                    khachHangs.add(new KhachHang(
                                                            str[1],
                                                            str[0],
                                                            str[3],
                                                            str[2],
                                                            str[4],
                                                            Integer.parseInt(str[5])
                                                    ));
                                                }
                                            }
                                            adapter = new KHListViewAdapter(HomeActivity.this, khachHangs);
                                            lvKH.setAdapter(adapter);
                                            resultbox.setVisibility(View.VISIBLE);
                                           // Toast.makeText(HomeActivity.this, khachHangs.size()+"-"+searchType, Toast.LENGTH_LONG).show();

                                            try{
                                             //   if( khachHangs.size() > 0 && searchType  == "LicensePlate"){
                                               //     KhachHang _kh = khachHangs.get(0);
                                              //      FindCustHistories(_kh.getCode(),_kh.getName(),_kh.getAddress(),_kh.getLicensePlate(), _kh.getTimes());
                                             //   }
                                            }
                                            catch (Exception e){
                                                Toast.makeText(HomeActivity.this, e.getMessage().toString(), Toast.LENGTH_LONG).show();

                                            }
                                        }
                                        else
                                        {
                                            AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);
                                            builder.setCancelable(true);
                                            builder.setTitle("Thông báo");
                                            builder.setMessage("Không tìm thấy thông tin khách hàng trong hệ thống. Bạn có muốn tạo phiếu dịch vụ mới không ?");
                                            builder.setPositiveButton("Có",
                                                    new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            KhachHang _kh =  new KhachHang(configModel.getCustCode()  ,configModel.getCustCode(),"","","",0);
                                                            intent = new Intent(HomeActivity.this, PrintActivity.class);
                                                            intent.putExtra("data", searchData);
                                                            intent.putExtra("ConfigModel", configModel);
                                                            intent.putExtra("sText", searchText);
                                                            intent.putExtra("sType", searchType);
                                                            intent.putExtra("KHang", _kh);
                                                            startActivity(intent);
                                                        }
                                                    });
                                            builder.setNegativeButton("Không", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                }
                                            });

                                            AlertDialog dialog = builder.create();
                                            dialog.show();
                                        }
                                    }
                                } catch (JSONException e) {
                                   // e.printStackTrace();
                                } catch (Exception e){
                                    Toast.makeText(HomeActivity.this, e.getMessage().toString(), Toast.LENGTH_LONG).show();

                                   // e.printStackTrace();
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                progressDialog.hide();
                                Toast.makeText(HomeActivity.this, "Method: searchorderheader /n StatusCode :"+error.networkResponse.statusCode, Toast.LENGTH_LONG).show();
                                Login_TienThu(true);
                            }
                        }
                ) {
                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError
                    {
                        Map<String,String> headers = new HashMap<>();
                        headers.put("content-type", "JSON");
                        headers.put("Authorization", "Bearer "+configModel.getTOKEN());
                        return headers;
                    }
                } ;
                jRequest.setShouldCache(false);
                jRequest.setRetryPolicy(new DefaultRetryPolicy(86399 , 20, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                if (mRequestQueue == null) {
                    mRequestQueue = new RequestQueue(cache, network);
                    mRequestQueue.start();
                }
                mRequestQueue.add(jRequest);
            } catch (Exception e) {
                progressDialog.hide();
            }
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exitApp();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void exitApp(){
        new AlertDialog.Builder(HomeActivity.this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Đóng ứng dụng")
                .setMessage("Bạn có muốn đóng ứng dụng không ?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finishAffinity();
                        System.exit(0);
                        // finish();
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }

    private  void getAppConfig(){
        SharedPreferences sharedPreferences = getSharedPreferences("HMS_SHARED_PREFERENCES", Context.MODE_PRIVATE);
        Boolean isFirst = sharedPreferences.getBoolean("IS_FIRTS_LAUNCHER", true);
        if (isFirst) {
            Intent intent = new Intent(HomeActivity.this, AppConfigActivity.class);
            startActivity(intent);
        } else {
            configModel.setAPIIP("http://" + sharedPreferences.getString("IP", "0.0.0.0"));
            configModel.setQMSIP("http://" + sharedPreferences.getString("QMSIP", "0.0.0.0"));
            configModel.setHeadCode(sharedPreferences.getString("HeadCode", "0"));
            configModel.setUserCode(sharedPreferences.getString("UserCode", "0"));
            configModel.setCustCode(sharedPreferences.getString("CustCode", "Customer Code") );
            configModel.setUserName(sharedPreferences.getString("UserName", "apitest@tienthu.vn"));
            configModel.setPassword(sharedPreferences.getString("Password", "62&z!]r*RV"));
            configModel.setButFonSize(sharedPreferences.getString("ButFontSize", "20"));
            configModel.setButHeight(sharedPreferences.getString("ButHeight", "50"));
            configModel.setButWidth(sharedPreferences.getString("ButWidth", "50"));

            configModel.setTitleFonSize(sharedPreferences.getString("TitleFontSize", "50"));
            configModel.setLogoHeight(sharedPreferences.getString("LogoHeight", "150"));
            configModel.setLogoWidth(sharedPreferences.getString("LogoWidth", "150"));
            configModel.setTitle(sharedPreferences.getString("Title", "Title"));
        }
    }

    private void FindCustHistories(final String maKH, final String tenKH, final String dChi, final String bSX, final int soLan) {
        progressDialog.show();
        try {
            String url = configModel.getAPIIP()  + ("/api/v1/report/motohistory?plateid="+bSX );
            // String url = serverAddress+ ("/api/v1/report/motohistory?plateid=81B1-09262"  );
            RequestQueue rqQue = Volley.newRequestQueue(HomeActivity.this);
            StringRequest jRequest = new StringRequest(
                    Request.Method.GET, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            progressDialog.hide();
                            mRequestQueue.stop();
                            mRequestQueue = null;
                            isStop = true;
                            try {
                                String ngaysua = "...";
                                String dsCongViecs = "";
                                String cuahang = "";
                                JSONObject _obj = new JSONObject(response);
                                if(_obj.getInt("statusCode") == 200){
                                    searchData = response;
                                    JSONArray jsonArray = _obj.getJSONArray("data");
                                    if(jsonArray.length()>0){
                                        String[] dataRow = jsonArray.getString(0).split(";");
                                        ngaysua = dataRow[5]  ;
                                        cuahang =  dataRow[12];
                                        String maDV = dataRow[6];
                                        dsCongViecs = dataRow[8];
                                        for (int i = 1; i < jsonArray.length(); i++) {
                                            dataRow = jsonArray.getString(i).split(";");
                                            if ( dataRow[6] == maDV) {
                                                dsCongViecs = ("|"+ dataRow[8]);
                                            }
                                            else
                                                break;
                                        }
                                    }
                                }
                                LuuKhachHangQMS(maKH,tenKH,dChi,bSX, soLan,ngaysua,dsCongViecs,cuahang);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            progressDialog.hide();
                            Toast.makeText(HomeActivity.this, "Method: searchorderheader /n StatusCode :"+error.networkResponse.statusCode, Toast.LENGTH_LONG).show();
                            Login_TienThu(true);
                        }
                    }
            ) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError
                {
                    Map<String,String> headers = new HashMap<>();
                    headers.put("content-type", "JSON");
                    headers.put("Authorization", "Bearer "+configModel.getTOKEN());
                    return headers;
                }
            } ;
            jRequest.setShouldCache(false);
            jRequest.setRetryPolicy(new DefaultRetryPolicy(86399 , 20, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            if (mRequestQueue == null) {
                mRequestQueue = new RequestQueue(cache, network);
                mRequestQueue.start();
            }
            mRequestQueue.add(jRequest);
        } catch (Exception e) {
            progressDialog.hide();
        }
    }

    private void LuuKhachHangQMS(String maKH,String tenKH,String dChi,String bSX,int soLan,String ngaysua,String congviecs,String cuahang) {
        progressDialog.show();
        //region
        String str =configModel.getQMSIP() + "/api/tienthu/LuuThongTinKH?dChi=" + dChi
                + "&tenKH=" + tenKH
                + "&maKH=" + maKH
                + "&soxe=" + bSX
                + "&soLan=" + soLan
                + "&ngaysua=" + ngaysua
                + "&congviecs=" + congviecs
                + "&cuahang=" + cuahang;

        RequestQueue rqQue = Volley.newRequestQueue(HomeActivity.this);
        JsonObjectRequest jRequest = new JsonObjectRequest(
                Request.Method.GET, str, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        progressDialog.hide();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.hide();
                        Toast.makeText(HomeActivity.this, "Gửi thông tin khách hàng : Không kết nối được với máy chủ QMS."+ error.getMessage().toString(), Toast.LENGTH_SHORT).show();
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

}