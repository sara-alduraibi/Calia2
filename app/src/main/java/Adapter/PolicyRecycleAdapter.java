package Adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import Extra.PrefManager;
import ModelClass.BookingModelClass;
import fragment.HomeFragment;

import com.voice.calia.HomePageActivity;
import com.voice.calia.MainActivity;
import com.voice.calia.R;


public class PolicyRecycleAdapter extends RecyclerView.Adapter<PolicyRecycleAdapter.MyViewHolder> {
    private Activity activity;

    Context context;

    boolean showingfirst = true;
    int myPos = 0;

    private List<BookingModelClass> OfferList;
    PrefManager prefManager;




    public class MyViewHolder extends RecyclerView.ViewHolder {



        TextView title,agree,disagree;




        public MyViewHolder(View view) {
            super(view);


            title = (TextView) view.findViewById(R.id.title);
            agree = (TextView) view.findViewById(R.id.tv_agree);
            disagree = (TextView) view.findViewById(R.id.tv_disagree);



        }



    }


    public PolicyRecycleAdapter(Activity activity) {
        this.activity = activity;

        prefManager = new PrefManager(activity);

        if (!prefManager.isFirstTimeLaunch()) {
            Intent intent = new Intent(context, HomePageActivity.class);
            context.startActivity(intent);

        }
    }

    public PolicyRecycleAdapter(Context context, List<BookingModelClass> offerList) {
        this.OfferList = offerList;
        this.context = context;
    }

    @Override
    public PolicyRecycleAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.policy_item, parent, false);


        return new PolicyRecycleAdapter.MyViewHolder(itemView);


    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        final BookingModelClass lists = OfferList.get(position);

        holder.title.setText(lists.getTitle());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myPos = position;
                notifyDataSetChanged();

            }
        });

        holder.agree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, MainActivity.class);
                context.startActivity(intent);
            }
        });
        holder.disagree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(context)
                        .setTitle("Delete entry")
                        .setMessage("Are you sure you want to delete this entry?")

                        // Specifying a listener allows you to take an action before dismissing the dialog.
                        // The dialog is automatically dismissed when a dialog button is clicked.
                              .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // Continue with delete operation
                                ((AppCompatActivity)context).finishAndRemoveTask();
                            }
                        })
                        // A null listener allows the button to dismiss the dialog and take no further action.
                        .setNegativeButton(android.R.string.no, null)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }
        });


    }


    @Override
    public int getItemCount() {
        return OfferList.size();

    }

}


