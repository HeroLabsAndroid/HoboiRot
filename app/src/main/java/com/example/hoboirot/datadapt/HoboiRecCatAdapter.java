package com.example.hoboirot.datadapt;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.hoboirot.HoboiLog;
import com.example.hoboirot.R;
import com.example.hoboirot.Util;

import java.util.ArrayList;

public class HoboiRecCatAdapter extends RecyclerView.Adapter<HoboiRecCatAdapter.ViewHolder> {

    private ArrayList<HoboiLog> localDataSet;

    private Context ctx;

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder)
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView getTvDatetime() {
            return tvDatetime;
        }

        public TextView getTvName() {
            return tvName;
        }

        private final TextView tvDatetime, tvName;


        public ViewHolder(View view) {
            super(view);
            // Define click listener for the ViewHolder's View

            tvName = (TextView) view.findViewById(R.id.TV_hob_datetime);
            tvDatetime= (TextView) view.findViewById(R.id.TV_hob_reccat);
        }

    }

    /**
     * Initialize the dataset of the Adapter
     *
     * @param dataSet ArrayList of HoboiLogs
     */
    public HoboiRecCatAdapter(ArrayList<HoboiLog> dataSet, Context c) {
        localDataSet = dataSet;

        ctx = c;
    }


    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.lstitm_hob_tit_and_last, viewGroup, false);

        return new ViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        viewHolder.getTvDatetime().setText(localDataSet.get(position).get_last() == null ? "NEVER" : Util.DateToString(localDataSet.get(position).get_last()));
        viewHolder.getTvName().setText(localDataSet.get(position).getHob().getName());

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return localDataSet.size();
    }
}
