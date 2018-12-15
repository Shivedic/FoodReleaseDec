package com.shivedic.foodveda.PaymentIntegrationMethods;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Telephony;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


import com.shivedic.foodveda.Activities.MainActivity;
import com.shivedic.foodveda.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.Manifest.permission.SEND_SMS;

public class OrderConfirmed extends AppCompatActivity {

    @BindView(R.id.continueShopping)
    Button continueShopping;
    static int MY_PERMISSIONS_REQUEST_SEND_SMS = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_confirmed);
        ButterKnife.bind(this);
    //    checkAndroidVersion("8958043695");
    }

    @OnClick(R.id.continueShopping)
    public void onClick(View view) {
        Intent intent = new Intent(OrderConfirmed.this, MainActivity.class);
        startActivity(intent);
        finishAffinity();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(OrderConfirmed.this, MainActivity.class);
        startActivity(intent);
        finishAffinity();
    }

}
