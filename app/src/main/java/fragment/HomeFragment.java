package fragment;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import Adapter.HomeCategoryRecycleAdapter;
import ModelClass.HomeCategoryModelClass;
import com.voice.calia.R;


public class HomeFragment extends Fragment {

    private View view;

    private ArrayList<HomeCategoryModelClass> homeCategoryModelClasses;
    private RecyclerView recyclerView;
    private HomeCategoryRecycleAdapter bAdapter;


    private Integer image[] = {R.drawable.logo};
    private Integer imagebtn [] = {R.drawable.sadrobot};


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view =  inflater.inflate(R.layout.fragment_home, container, false);



        /*category recyclerview code is here*/

        recyclerView = view.findViewById(R.id.HomeRecyclerview);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getActivity(),1);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        homeCategoryModelClasses = new ArrayList<>();


            HomeCategoryModelClass mycreditList = new HomeCategoryModelClass(image[0],imagebtn[0]);
            homeCategoryModelClasses.add(mycreditList);
        bAdapter = new HomeCategoryRecycleAdapter(getActivity(),homeCategoryModelClasses);
        recyclerView.setAdapter(bAdapter);


        return  view;
    }


    }

