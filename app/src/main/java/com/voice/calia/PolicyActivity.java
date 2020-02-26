package com.voice.calia;

import android.content.Intent;
import android.os.Bundle;

import android.view.View;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import Extra.PrefManager;


public class PolicyActivity extends AppCompatActivity {

    TextView agree,disagree;
    PrefManager prefManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.policy_item);

        prefManager = new PrefManager(this);

        if (!prefManager.isFirstTimeLaunch()){

            Intent intent = new Intent(PolicyActivity.this, MainActivity.class);
            startActivity(intent);

        }

        setContentView(R.layout.policy_item);
        agree = findViewById(R.id.tv_agree);
        disagree = findViewById(R.id.tv_disagree);

        agree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PolicyActivity.this, MainActivity.class);
                startActivity(intent);

            }
        });

        disagree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                moveTaskToBack(true);
            }
        });

    }
}
