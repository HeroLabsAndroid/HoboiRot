package com.example.hoboirot.datadapt;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.hoboirot.Hoboi;
import com.example.hoboirot.R;

import java.util.ArrayList;

public class HoboiAdapter extends RecyclerView.Adapter<HoboiAdapter.ViewHolder> {

    private ArrayList<Hoboi> localDataSet;

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder)
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView textView;
        private final Button btnDone, btnRemove;


        public ViewHolder(View view) {
            super(view);
            // Define click listener for the ViewHolder's View

            textView = (TextView) view.findViewById(R.id.TXTVIEW_hoboiname);
            btnDone = (Button) view.findViewById(R.id.BTN_hoboidone);
            btnRemove = (Button) view.findViewById(R.id.BTN_removehoboi);
        }

        public TextView getTextView() {
            return textView;
        }

        public Button getBtnDone() {
            return btnDone;
        }

        public Button getBtnRemove() {
            return btnRemove;
        }
    }

    /**
     * Initialize the dataset of the Adapter
     *
     * @param dataSet String[] containing the data to populate views to be used
     * by RecyclerView
     */
    public HoboiAdapter(ArrayList<Hoboi> dataSet) {
        localDataSet = dataSet;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.lstitm_hoboi, viewGroup, false);

        return new ViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        viewHolder.getTextView().setText(localDataSet.get(position).getName());

        viewHolder.getBtnDone().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(localDataSet.get(viewHolder.getAdapterPosition()).isDoneToday()) {
                    viewHolder.getBtnDone().setBackgroundColor(Color.argb(255, 116, 67, 204));
                    localDataSet.get(viewHolder.getAdapterPosition()).setDoneToday(false);
                } else {
                    viewHolder.getBtnDone().setBackgroundColor(Color.argb(255, 128, 128, 128));
                    localDataSet.get(viewHolder.getAdapterPosition()).setDoneToday(true);
                }

            }
        });
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return localDataSet.size();
    }
}
