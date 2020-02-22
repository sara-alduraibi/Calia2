package fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import Adapter.CategoryPagerAdapterPolicy;

import com.voice.calia.R;

public class PolicyFragment extends Fragment {

    private View view;



    private int tab_value;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view =  inflater.inflate(R.layout.fragment_policy, container, false);

         /*on tab viewpager change code is here*/

        final ViewPager viewPager1 = (ViewPager) view.findViewById(R.id.pager);
        CategoryPagerAdapterPolicy adapter = new CategoryPagerAdapterPolicy(getChildFragmentManager(), 2);
        viewPager1.setAdapter(adapter);
        viewPager1.setOffscreenPageLimit(1);
        return  view;
    }





}
