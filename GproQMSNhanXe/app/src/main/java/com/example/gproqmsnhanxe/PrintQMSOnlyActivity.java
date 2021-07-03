package com.example.gproqmsnhanxe;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

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
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

public class PrintQMSOnlyActivity extends AppCompatActivity {
    //region variable declare
    String IPAddress;
    ConfigModel configModel ;
    static ImageButton[] buttonArr;
    JSONArray services = null;
    ProgressDialog progressDialog;
    ImageView imgLogo ;
    TextView lbTitle;
    //endregion
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_print_q_m_s_only);

        progressDialog = new ProgressDialog(PrintQMSOnlyActivity.this);
        progressDialog.setTitle("Loading...");
        progressDialog.setMessage("Đang tải dữ liệu...");
        progressDialog.show();

        Intent intent = getIntent();
        configModel = (ConfigModel) intent.getSerializableExtra("ConfigModel");
        if(configModel == null){
            GetAppConfig();
        }
        // Instantiate the cache
        Cache cache = new DiskBasedCache(getCacheDir(), 1024 * 1024); // 1MB cap
        // Set up the network to use HttpURLConnection as the HTTP client.
        Network network = new BasicNetwork(new HurlStack());
        // Instantiate the RequestQueue with the cache and network.
        final RequestQueue mRequestQueue = new RequestQueue(cache, network);
        mRequestQueue.start();

        imgLogo = (ImageView)findViewById(R.id.imgLogo);
         LinearLayout.LayoutParams parms = new LinearLayout.LayoutParams(Integer.parseInt(configModel.getLogoWidth()) ,Integer.parseInt(configModel.getLogoHeight()) );
        parms.setMargins(30,0,30,0);
        imgLogo.setLayoutParams(parms);
        imgLogo.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                goBack();
                return false;
            }
        });
        new LoadImageInternet().execute(configModel.getQMSIP()+"/Content/logo.png");

        lbTitle = (TextView)findViewById(R.id.lbChaoDG);
        lbTitle.setText(configModel.getTitle());
        lbTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, Float.parseFloat(configModel.getTitleFonSize()) );

          GetServices(mRequestQueue);
    }

    //region method
    private void PrintTicket(int dichvuId, String thoigian ) {
        progressDialog.show();
        //region
        String str = (configModel.getQMSIP() + "/api/serviceapi/PrintTicket?soxe=&thoigian="+thoigian+"&dichvuId=" + dichvuId  );
        RequestQueue rqQue = Volley.newRequestQueue(PrintQMSOnlyActivity.this);
        JsonObjectRequest jRequest = new JsonObjectRequest(
                Request.Method.GET, str, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        progressDialog.hide();
                        Boolean rs = response.optBoolean("IsSuccess");
                        if (rs){
                            Toast.makeText(PrintQMSOnlyActivity.this, "Gửi yêu cầu cấp phiếu thành công. STT: "+response.optInt("Data"), Toast.LENGTH_SHORT).show();
                            //goBack( );
                        }
                        else
                            Toast.makeText(PrintQMSOnlyActivity.this, "Gửi yêu cầu cấp phiếu thất bại.", Toast.LENGTH_SHORT).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.hide();
                        Toast.makeText(PrintQMSOnlyActivity.this, "Gửi YC cấp phiếu : Không kết nối được với máy chủ." , Toast.LENGTH_SHORT).show();
                    }
                }
        );
        rqQue.add(jRequest);
        //endregion
    }

    private void GetServices(RequestQueue mRequestQueue) {
        progressDialog.show();
        String str = (configModel.getQMSIP() + "/api/serviceapi/getservices");
        RequestQueue rqQue = Volley.newRequestQueue(PrintQMSOnlyActivity.this);
        JsonArrayRequest jRequest = new JsonArrayRequest(
                Request.Method.GET, str, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        if (response != null) {
                            progressDialog.hide();
                            services = response;
                            InitServiceButtons();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.hide();
                        Toast.makeText(PrintQMSOnlyActivity.this, "Lấy Dịch vụ : Không kết nối được với máy chủ.", Toast.LENGTH_SHORT).show();
                    }
                }
        );
        jRequest.setShouldCache(false);
        jRequest.setRetryPolicy(new DefaultRetryPolicy(20000, 20, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        // rqQue.add(jRequest);
        mRequestQueue.add(jRequest);
    }

    private void InitServiceButtons() {
        buttonArr = new ImageButton[services.length()];
        LinearLayout root = (LinearLayout)findViewById(R.id.layout_list_dichvu);
        JSONObject object = null;
        int count = 0;
        if(services != null && services.length() > 0){
            for (int i=0;i< services.length();i++) {
                LinearLayout row = new LinearLayout(PrintQMSOnlyActivity.this);
                row.setLayoutParams(new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        1.0f
                ));
                row.setOrientation(LinearLayout.HORIZONTAL);
                row.setGravity(Gravity.CENTER);
                row.setPadding(0,0,0,30);

                // for (int ii=0;ii< NUM_COL;ii++) {
                object = null;
                try {
                    object = services.getJSONObject(count);
                } catch (JSONException e) {
                    // e.printStackTrace();
                }

                if (object != null) {
                    buttonArr[count] = new ImageButton(PrintQMSOnlyActivity.this);
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                            Integer.parseInt(configModel.getButWidth())   ,
                            Integer.parseInt(configModel.getButHeight())   ,
                            1.0f
                    );
                    params.setMargins(30, 0, 0, 0);
                    buttonArr[count].setLayoutParams(params);
                    buttonArr[count].setBackgroundColor(Color.parseColor("#00ffffff"));
                    buttonArr[count].setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                    buttonArr[count].setBackgroundResource(R.drawable.printer_ticket);
                    buttonArr[count].setPadding(30,5,5,5);

                    final JSONObject finalObject = object;
                    buttonArr[count].setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            PrintTicket(finalObject.optInt("Id",0), finalObject.optString("Code"));
                        }
                    });
                    row.addView(buttonArr[count]);

                    TextView textView = new TextView(PrintQMSOnlyActivity.this);
                    textView.setLayoutParams(new LinearLayout.LayoutParams(
                            TableLayout.LayoutParams.MATCH_PARENT,
                            TableLayout.LayoutParams.WRAP_CONTENT,
                            100
                    ));
                    textView.setText(object.optString("Name").toString() );
                    textView.setTextColor(Color.RED);
                    textView.setTextSize(Integer.parseInt(configModel.getButFonSize()) );
                    textView.setGravity(Gravity.CENTER_VERTICAL);
                    textView.setSingleLine(false);
                    textView.setPadding(30,0,0,0);
                    row.addView(textView);
                }
                count++;
                //}
                root.addView(row);
            }
        }
    }

    private void GetAppConfig() {
        SharedPreferences sharedPreferences = getSharedPreferences("HMS_SHARED_PREFERENCES", Context.MODE_PRIVATE);
        Boolean isFirst = sharedPreferences.getBoolean("IS_FIRTS_LAUNCHER", true);
        if (isFirst) {
            Intent intent = new Intent(PrintQMSOnlyActivity.this, AppConfigActivity.class);
            startActivity(intent);
        } else {
            configModel = new ConfigModel();
            configModel.setQMSIP("http://" + sharedPreferences.getString("QMSIP", "0.0.0.0"));
            configModel.setButFonSize(sharedPreferences.getString("TitleFonSize", "50"));
            configModel.setButHeight(sharedPreferences.getString("LogoHeight", "150"));
            configModel.setButWidth(sharedPreferences.getString("LogoWidth", "150"));
            configModel.setButWidth(sharedPreferences.getString("Title", "Title"));
        }
    }

    //endregion

    private void goBack( ) {
        Intent intent = new Intent(PrintQMSOnlyActivity.this, HomeActivity.class);
        intent.putExtra("ConfigModel", configModel);
        startActivity(intent);
    }

    private class LoadImageInternet extends AsyncTask<String,Void, Bitmap> {
        Bitmap bitmap = null;
        @Override
        protected Bitmap doInBackground(String... strings) {
            try {
                URL url = new URL(strings[0]);
                InputStream is = url.openConnection().getInputStream();
                bitmap= BitmapFactory.decodeStream(is);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return bitmap;
        }
        @Override
        protected void onPostExecute(Bitmap bitmap){
            super.onPostExecute(bitmap);
            try {
                //  imAvatar  = findViewById(R.id.imAvatar);
                imgLogo.setImageBitmap(bitmap);
                //  Drawable top = Drawable.createFromStream(bitmap , "src");
            }
            catch (Exception e){}
        }
    }

}