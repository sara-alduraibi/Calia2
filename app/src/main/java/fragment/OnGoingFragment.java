package fragment;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.DefaultItemAnimator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import Adapter.PolicyRecycleAdapter;
import ModelClass.BookingModelClass;
import com.voice.calia.R;


public class OnGoingFragment extends Fragment {


    private View view;


    private ArrayList<BookingModelClass> bookingModelClasses;
    private RecyclerView recyclerView;
    private PolicyRecycleAdapter bAdapter;

    private String title[] = {"السياسات و الخصوصية"};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_on_going, container, false);

        /*category recyclerview code is here*/

        recyclerView = view.findViewById(R.id.recyclerview);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        bookingModelClasses = new ArrayList<>();

        for (int i = 0; i < title.length; i++) {
            BookingModelClass mycreditList = new BookingModelClass(title[i]);
            bookingModelClasses.add(mycreditList);
        }
        bAdapter = new PolicyRecycleAdapter(getActivity(),bookingModelClasses);
        recyclerView.setAdapter(bAdapter);



        return  view;
    }


}
