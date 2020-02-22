package com.voice.calia;

import android.content.Intent;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import Adapter.WalkThroughPagerAdapter;
import me.relex.circleindicator.CircleIndicator;

public class WalkThroughActivity2 extends AppCompatActivity {

    private ViewPager viewPager;

    private WalkThroughPagerAdapter walkThroughPagerAdapter;

    TextView txtskip,txtnext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_walk_through2);

        txtskip = findViewById(R.id.txtskip);
        txtnext = findViewById(R.id.txtnext);


        viewPager = (ViewPager) findViewById(R.id.viewpager);
        CircleIndicator indicator = (CircleIndicator) findViewById(R.id.indicator);



        /*view pager and indicator code is here*/


        walkThroughPagerAdapter = new WalkThroughPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(walkThroughPagerAdapter);
        indicator.setViewPager(viewPager);
        walkThroughPagerAdapter.registerDataSetObserver(indicator.getDataSetObserver());


        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (viewPager.getCurrentItem()==2){
                    txtskip.setVisibility(View.GONE);
                    txtnext.setText("يلا مشينا !");
                }
                else {
                    txtskip.setVisibility(View.VISIBLE);
                    txtnext.setText("التالي");
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        txtskip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(WalkThroughActivity2.this,HomePageActivity.class);
                startActivity(intent);
            }
        });

        txtnext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (viewPager.getCurrentItem()!=2){
                    viewPager.setCurrentItem(viewPager.getCurrentItem()+1);

                }
                else {
                    Intent intent = new Intent(WalkThroughActivity2.this,HomePageActivity.class);
                    startActivity(intent);
                }
            }
        });



    }
}
