package Adapter;


import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;


import fragment.BeautyFragment;



/**
 * Created by wolfsoft5 on 7/4/18.
 */

public class CategoryPagerAdapterAllServices extends FragmentPagerAdapter {

        int mNoOfTabs;

        public CategoryPagerAdapterAllServices(FragmentManager fm, int NumberOfTabs)

        {
                super(fm);
                this.mNoOfTabs = NumberOfTabs;
        }

        @Override
        public Fragment getItem(int position) {
                switch (position) {

                        case 0:
                                BeautyFragment tab1 = new BeautyFragment();
                                return tab1;
                        default:
                                return null;

                }
        }

        @Override
        public int getCount() {
                return mNoOfTabs;

        }
}

