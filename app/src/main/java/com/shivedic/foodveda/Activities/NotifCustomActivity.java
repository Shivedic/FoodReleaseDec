package com.shivedic.foodveda.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.shivedic.foodveda.R;

public class NotifCustomActivity extends AppCompatActivity {

    public static TextView titleView, messageView;
public static Button contiBtn, skipBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notif_custom);
        Intent intent = getIntent();
        String id = intent.getStringExtra("id");
        String title = intent.getStringExtra("title");
        String message = intent.getStringExtra("message");
        titleView = (TextView) findViewById(R.id.notifTitle);
        messageView = (TextView) findViewById(R.id.notifDesc);
        titleView.setText(title);
        messageView.setText(message);
        contiBtn = (Button) findViewById(R.id.continueBtn);
        skipBtn = (Button) findViewById(R.id.skipBtn);
        contiBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(NotifCustomActivity.this, MainActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |Intent.FLAG_ACTIVITY_CLEAR_TASK);
                getApplicationContext().startActivity(i);
            }
        });

        skipBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent();
                i.setAction(Intent.ACTION_MAIN);
                i.addCategory(Intent.CATEGORY_HOME);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |Intent.FLAG_ACTIVITY_CLEAR_TASK);
                getApplicationContext().startActivity(i);
            }
        });

    }
}
