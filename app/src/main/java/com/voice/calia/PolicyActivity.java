package com.voice.calia;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
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

                AlertDialog alertDialog = new AlertDialog.Builder(PolicyActivity.this).setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("هل أنت متأكد؟")

                        .setMessage("سيتم إخراجك من البرنامج في حال ضغطك على نعم")

                        .setPositiveButton("نعم", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                finish();
                                moveTaskToBack(true);
                            }
                        })

                        .setNegativeButton("لا", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                            }
                        })
                        .show();
            }
        });

    }
}
