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



import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.android.material.bottomnavigation.BottomNavigationMenuView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.app.AppCompatActivity;

import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.lang.reflect.Field;


import fragment.PolicyFragment;
import fragment.CategoryFragment;
import fragment.HomeFragment;
import fragment.ProfileFragment;
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

    protected BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment fragment;
            switch (item.getItemId()) {
                case R.id.navigation_shop:
                    city_linear.setVisibility(View.VISIBLE);
                    title.setText("");
                    fragment = new HomeFragment();
                    loadFragment(fragment);
                    return true;

                case R.id.navigation_gifts:
                    city_linear.setVisibility(View.GONE);
                    title.setText("من نحن ؟");
                    fragment = new CategoryFragment();
                    loadFragment(fragment);

                    return true;
                case R.id.navigation_cart:

                    city_linear.setVisibility(View.GONE);
                    title.setText("MyBooking");
                    fragment = new PolicyFragment();
                    loadFragment(fragment);


                    return true;
                case R.id.navigation_profile:

                    city_linear.setVisibility(View.GONE);
                    title.setText("Profile");
                    fragment = new ProfileFragment();
                    loadFragment(fragment);
                    return true;
            }

            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        /**
         * This function is not a main function, it is for the design layouts since we have a navigation bar
         *
         */
        linear = findViewById(R.id.linear);
        linear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomePageActivity.this, HomePageActivity.class);
                startActivity(intent);
            }
        });

        city_linear = findViewById(R.id.city_linear);
        title = findViewById(R.id.title);

        prefManager = new PrefManager(this);

        if (!prefManager.isFirstTimeLaunch()){
            loadFragment(new HomeFragment());

        }
        else {
        loadFragment(new PolicyFragment()); }

        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.navigation);
        BottomNavigationViewHelper.disableShiftMode(bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        BottomNavigationMenuView menuView = (BottomNavigationMenuView) bottomNavigationView.getChildAt(0);
        for (int i = 0; i < menuView.getChildCount(); i++) {
            final View iconView = menuView.getChildAt(i).findViewById(com.google.android.material.R.id.icon);
            final ViewGroup.LayoutParams layoutParams = iconView.getLayoutParams();
            final DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
            layoutParams.height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 18, displayMetrics);
            layoutParams.width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 18, displayMetrics);
            iconView.setLayoutParams(layoutParams);
        }

    }
    private void loadFragment(Fragment fragment) {
        // load fragment
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.framelayout, fragment);
        transaction.commit();
    }


    public static class BottomNavigationViewHelper {
        @SuppressLint("RestrictedApi")
        public static void disableShiftMode(BottomNavigationView view) {
            BottomNavigationMenuView menuView = (BottomNavigationMenuView) view.getChildAt(0);
            try {
                Field shiftingMode = menuView.getClass().getDeclaredField("mShiftingMode");
                shiftingMode.setAccessible(true);
                shiftingMode.setBoolean(menuView, false);
                shiftingMode.setAccessible(false);
                for (int i = 0; i < menuView.getChildCount(); i++) {
                    BottomNavigationItemView item = (BottomNavigationItemView) menuView.getChildAt(i);
                    //noinspection RestrictedApi
                    item.setShifting(false);
                    // set once again checked value, so view will be updated
                    //noinspection RestrictedApi
                    item.setChecked(item.getItemData().isChecked());
                }
            } catch (NoSuchFieldException e) {
                Log.e("BNVHelper", "Unable to get shift mode field", e);
            } catch (IllegalAccessException e) {
                Log.e("BNVHelper", "Unable to change value of shift mode", e);
            }
        }


    }




        }


