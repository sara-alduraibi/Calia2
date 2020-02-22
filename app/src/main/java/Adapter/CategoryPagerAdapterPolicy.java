package Adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentManager;

import fragment.OnGoingFragment;


/**
 * Created by wolfsoft5 on 7/4/18.
 */

public class CategoryPagerAdapterPolicy extends FragmentPagerAdapter {

        int mNoOfTabs;

        public CategoryPagerAdapterPolicy(FragmentManager fm, int NumberOfTabs)

        {
                super(fm);
                this.mNoOfTabs = NumberOfTabs;
        }

        @Override
        public Fragment getItem(int position) {
                switch (position) {

                        case 0:
                                OnGoingFragment tab1 = new OnGoingFragment();
                                return tab1;
                        case 1:
                                OnGoingFragment tab2 = new OnGoingFragment();
                                return tab2;



                        default:
                                return null;

                }
        }

        @Override
        public int getCount() {
                return mNoOfTabs;

        }
}

