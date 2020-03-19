package com.voice.calia;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;

import com.bumptech.glide.Glide;

import uk.co.senab.photoview.PhotoViewAttacher;


public class ImageLocation extends AppCompatActivity {
    ImageView iv_llocation;
    Intent intent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.maplocation);
        iv_llocation = findViewById(R.id.iv_llocation);
        Intent intent = getIntent();
        Bundle b = intent.getExtras();
        String imageURL = (String) b.get("images");
        Glide.with(ImageLocation.this).load(imageURL).into(iv_llocation);
        PhotoViewAttacher pAttacher;
        pAttacher = new PhotoViewAttacher(iv_llocation);
        pAttacher.update();
        // get back
       // ActionBar actionBar = getActionBar();
      //  actionBar.setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


}
