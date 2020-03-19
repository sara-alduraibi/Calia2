package com.voice.calia;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import Extra.PrefManager;


public class ContactUsActivity extends AppCompatActivity {

    Intent intent;
    /**
     * @param menu
     * @return menu for user
     */

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.navigation, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.home:
                intent = new Intent(ContactUsActivity.this, MainActivity.class);
                startActivity(intent);
                return true;
            case R.id.aboutus:
                intent = new Intent(ContactUsActivity.this, PolicyActivity.class);
                startActivity(intent);
                return true;
            case R.id.contactus:
                intent = new Intent(ContactUsActivity.this, ContactUsActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contactus);

    }
}
