package com.example.hoboirot.datadapt;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.hoboirot.R;
import com.example.hoboirot.Util;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;

public class HoboiHistAdapter extends RecyclerView.Adapter<HoboiHistAdapter.ViewHolder> {

    private ArrayList<LocalDateTime> localDataSet;
    private Context ctx;

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder)
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView getTvDatetime() {
            return tvDatetime;
        }

        public TextView getTvRecentCat() {
            return tvRecentCat;
        }

        private final TextView tvDatetime, tvRecentCat;


        public ViewHolder(View view) {
            super(view);
            // Define click listener for the ViewHolder's View

            tvDatetime = (TextView) view.findViewById(R.id.TV_hob_datetime);
            tvRecentCat = (TextView) view.findViewById(R.id.TV_hob_reccat);
        }

    }

    /**
     * Initialize the dataset of the Adapter
     *
     * @param dataSet ArrayList of Hobois
     */
    public HoboiHistAdapter(ArrayList<LocalDateTime> dataSet, Context c) {
        localDataSet = dataSet;
        Collections.sort(localDataSet);
        ctx = c;
    }


    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.lstitm_hoboilog, viewGroup, false);

        return new ViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        viewHolder.getTvDatetime().setText(Util.DateTimeToString(localDataSet.get(localDataSet.size()-1-position)));
        viewHolder.getTvRecentCat().setText(Util.getRecency(localDataSet.get(localDataSet.size()-1-position)).toString());

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return localDataSet.size();
    }
}
