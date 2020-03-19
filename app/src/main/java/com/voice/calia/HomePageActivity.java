package com.voice.calia;

/**
 *
 *
 *
 * Here is the first main page of Calia project
 *  Where it must includes all the main functions
 *  The main functions are ------> //
 *  1- GreetingsFunction.
 *  2-ConvertSpeechToText.
 *  3-Extract Intent to Reg-exp
 *  4-Calling WebService
 *  5-ConvertTextToSpeech
 * ---------------> ///
 * //
 * ///
 * ////
 * Other functions are for the design---> changing from view to another view
 *
 *
 */



import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import Extra.PrefManager;
public class HomePageActivity extends AppCompatActivity {

    /**
     * Load all the elements of the interface
     */

    FrameLayout frameLayout;
    LinearLayout linear,city_linear;
    TextView title;
    ImageView img_view;
    PrefManager prefManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);


        /**
         * This function is not a main function, it is for the design layouts since we have a navigation bar
         *
         */


    }
}




