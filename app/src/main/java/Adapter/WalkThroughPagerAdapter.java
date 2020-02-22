package Adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import fragment.SlideOneFragment;
import fragment.SlideThreeFragment;
import fragment.SlideTwoFragment;


/**
 * Created by wolfsoft on 10/11/2015.
 */
public class WalkThroughPagerAdapter extends FragmentStatePagerAdapter {




    public WalkThroughPagerAdapter(FragmentManager fm) {
        super(fm);




    }


    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                SlideOneFragment tab0 = new SlideOneFragment();
                return tab0;

            case 1:
                SlideTwoFragment tab1 = new SlideTwoFragment();
                return tab1;


            case 2:
                SlideThreeFragment tab2 = new SlideThreeFragment();
                return tab2;



            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 3;
    }
}