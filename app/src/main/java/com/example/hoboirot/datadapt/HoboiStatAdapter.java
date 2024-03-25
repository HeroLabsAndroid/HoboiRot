package com.example.hoboirot.datadapt;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hoboirot.HoboiLog;
import com.example.hoboirot.R;
import com.example.hoboirot.Util;
import com.example.hoboirot.dialog.HoboiHistDialog;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class HoboiStatAdapter extends RecyclerView.Adapter<HoboiStatAdapter.ViewHolder> {

    private ArrayList<ArrayList<HoboiLog>> localDataSet;
    Context ctx;


    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder)
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final RecyclerView rcvRecCat;

        private final TextView tvCatName;


        public RecyclerView getRcvRecCat() {
            return rcvRecCat;
        }

        public TextView getTvCatName() {
            return tvCatName;
        }

        public ViewHolder(View view) {
            super(view);
            // Define click listener for the ViewHolder's View

            rcvRecCat = (RecyclerView) view.findViewById(R.id.RCLVW_hobs_reccat);
            tvCatName = (TextView) view.findViewById(R.id.TV_reccatname);
        }

    }

    /**
     * Initialize the dataset of the Adapter
     *
     * @param dataSet ArrayList of Hobois
     */
    public HoboiStatAdapter(ArrayList<ArrayList<HoboiLog>> dataSet, Context c) {
        localDataSet = dataSet;
        ctx = c;
    }


    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.lstitm_hobsinreccat, viewGroup, false);

        return new ViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element

        String tit = Util.CatFromOrdinal(position).toString();
        viewHolder.getTvCatName().setText(tit);

        if(!localDataSet.get(position).isEmpty()) {
            viewHolder.getRcvRecCat().setLayoutManager(new LinearLayoutManager(ctx));
            HoboiRecCatAdapter hobRCAdapt = new HoboiRecCatAdapter(localDataSet.get(position), ctx);
            viewHolder.getRcvRecCat().setAdapter(hobRCAdapt);
        }



    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return localDataSet.size();
    }
}
