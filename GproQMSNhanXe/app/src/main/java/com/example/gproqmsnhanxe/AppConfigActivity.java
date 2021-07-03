package com.example.gproqmsnhanxe;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AppConfigActivity extends AppCompatActivity {
    EditText txtIp,txtqmsIP,txtheadname,txtusercode,txtcustCode, txtUsername, txtPassword, txtButHeight, txtButWidth, txtButFontSize, txtLogoHeight, txtLogoWidth, txtTitle, txtTitleFontSize;
    Button btsave, btexit;
    SharedPreferences sharedPreferences;
    Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_config);

        setTitle("Cấu hình");
        getSupportActionBar().setTitle("CẤU HÌNH HỆ THỐNG");
        txtIp = (EditText) findViewById(R.id.txtIp);
        txtqmsIP = (EditText) findViewById(R.id.txtQMSIp);
        txtheadname = (EditText) findViewById(R.id.txtheadname);
        txtusercode = (EditText) findViewById(R.id.txtusercode);
        txtcustCode = (EditText) findViewById(R.id.txtcustCode);
        txtUsername = (EditText) findViewById(R.id.txtUserName);
        txtPassword = (EditText) findViewById(R.id.txtPassword);
        txtButHeight = (EditText) findViewById(R.id.txtButHeight);
        txtButWidth = (EditText) findViewById(R.id.txtButWidth);
        txtButFontSize = (EditText) findViewById(R.id.txtButFontSize);

        txtLogoHeight = (EditText) findViewById(R.id.txtLogoHeight);
        txtLogoWidth = (EditText) findViewById(R.id.txtLogoWidth);
        txtTitleFontSize = (EditText) findViewById(R.id.txtTitleFontSize);
        txtTitle = (EditText) findViewById(R.id.txtTitle);

        sharedPreferences = getSharedPreferences("HMS_SHARED_PREFERENCES", Context.MODE_PRIVATE);
        txtIp.setText(sharedPreferences.getString("IP", "api.ototienthu.com.vn"));
        txtqmsIP.setText(sharedPreferences.getString("QMSIP", "113.160.232.47:89"));
        txtheadname.setText(sharedPreferences.getString("HeadCode", "CH-TP01"));
        txtusercode.setText(sharedPreferences.getString("UserCode", "TT_0825_18112016"));
        txtcustCode.setText(sharedPreferences.getString("CustCode", "Customer Default"));
        txtUsername.setText(sharedPreferences.getString("UserName", "apitest@tienthu.vn"));
        txtPassword.setText(sharedPreferences.getString("Password", "62&z!]r*RV"));
        txtButWidth.setText(sharedPreferences.getString("ButWidth", "200"));
        txtButHeight.setText(sharedPreferences.getString("ButHeight", "200"));
        txtButFontSize.setText(sharedPreferences.getString("ButFontSize", "54"));

        txtLogoWidth.setText(sharedPreferences.getString("LogoWidth", "200"));
        txtLogoHeight.setText(sharedPreferences.getString("LogoHeight", "200"));
        txtTitleFontSize.setText(sharedPreferences.getString("TitleFontSize", "54"));
        txtTitle.setText(sharedPreferences.getString("Title", "ĐIỂM CẤP PHIẾU"));

        btexit= (Button) findViewById(R.id.btexit);
        btexit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exitApp();
            }
        });

        btsave = (Button) findViewById(R.id.btsave);
        //region event
        btsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (txtIp.getText().toString() == "")
                    Toast.makeText(AppConfigActivity.this, "Vui lòng nhập địa chỉ máy chủ.", Toast.LENGTH_LONG).show();
                else {
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean("IS_FIRTS_LAUNCHER", false);
                    editor.putString("IP", txtIp.getText().toString());
                    editor.putString("QMSIP", txtqmsIP.getText().toString());
                    editor.putString("HeadCode", txtheadname.getText().toString());
                    editor.putString("UserCode", txtusercode.getText().toString());
                    editor.putString("CustCode", txtcustCode.getText().toString());
                    editor.putString("UserName", txtUsername.getText().toString());
                    editor.putString("Password", txtPassword.getText().toString());
                    editor.putString("ButHeight", txtButHeight.getText().toString());
                    editor.putString("ButWidth", txtButWidth.getText().toString());
                    editor.putString("ButFontSize", txtButFontSize.getText().toString());
                    editor.putString("LogoHeight", txtLogoHeight.getText().toString());
                    editor.putString("LogoWidth", txtLogoWidth.getText().toString());
                    editor.putString("TitleFontSize", txtTitleFontSize.getText().toString());
                    editor.putString("Title", txtTitle.getText().toString());
                    editor.apply();
                    intent = new Intent(AppConfigActivity.this,HomeActivity.class);
                    startActivity(intent);
                }
            }
        });
        //endregion
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
        new AlertDialog.Builder(AppConfigActivity.this)
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
}