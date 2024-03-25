package com.example.hoboirot.datadapt;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hoboirot.Hoboi;
import com.example.hoboirot.HoboiLog;
import com.example.hoboirot.R;
import com.example.hoboirot.Util;
import com.example.hoboirot.dialog.AddHoboiDialog;
import com.example.hoboirot.dialog.HoboiHistDialog;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class HoboiAdapter extends RecyclerView.Adapter<HoboiAdapter.ViewHolder> {

    public interface HoboiRemoveListener {
        public void onHoboiRemove();
    }

    HoboiRemoveListener hobremlisten;
    FragmentManager fragMan;
    Context ctx;

    private ArrayList<HoboiLog> localDataSet;

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder)
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvName, tvTimestamp;
        private final Button btnDone, btnRemove, btnShowlog;




        public ViewHolder(View view) {
            super(view);
            // Define click listener for the ViewHolder's View

            tvTimestamp = (TextView) view.findViewById(R.id.TXTVIEW_lastperf);
            tvName = (TextView) view.findViewById(R.id.TXTVIEW_hoboiname);
            btnDone = (Button) view.findViewById(R.id.BTN_hoboidone);
            btnRemove = (Button) view.findViewById(R.id.BTN_removehoboi);
            btnShowlog = (Button) view.findViewById(R.id.BTN_showlog);
        }

        public Button getBtnShowlog() {
            return btnShowlog;
        }

        public TextView getTvName() {
            return tvName;
        }

        public Button getBtnDone() {
            return btnDone;
        }

        public Button getBtnRemove() {
            return btnRemove;
        }

        public TextView getTvTimestamp() {
            return tvTimestamp;
        }
    }

    /**
     * Initialize the dataset of the Adapter
     *
     * @param dataSet ArrayList of Hobois
     */
    public HoboiAdapter(ArrayList<HoboiLog> dataSet, Context c, FragmentManager fm) {
        localDataSet = dataSet;
        hobremlisten = (HoboiRemoveListener) c;
        fragMan = fm;
        ctx = c;
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
        viewHolder.getTvName().setText(localDataSet.get(position).getHob().getName());

        if(localDataSet.get(position).never_performed()) {
            viewHolder.getTvTimestamp().setText("NEVER");
        } else if(localDataSet.get(position).done_today()) {
            viewHolder.getTvTimestamp().setText("TODAY");
        } else viewHolder.getTvTimestamp().setText(Util.DateTimeToString(localDataSet.get(position).get_last()));

        if(localDataSet.get(position).done_today()) viewHolder.getBtnDone().setBackgroundColor(Color.argb(255, 128, 128, 128));
        else viewHolder.getBtnDone().setBackgroundColor(Color.argb(255, 116, 67, 204));
        viewHolder.getBtnDone().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(localDataSet.get(viewHolder.getAdapterPosition()).done_today()) {
                    viewHolder.getBtnDone().setBackgroundColor(Color.argb(255, 116, 67, 204));
                    localDataSet.get(viewHolder.getAdapterPosition()).undo();

                    if(localDataSet.get(position).never_performed()) {
                        viewHolder.getTvTimestamp().setText("NEVER");
                    } else if(localDataSet.get(position).done_today()) {
                        viewHolder.getTvTimestamp().setText("TODAY");
                    } else viewHolder.getTvTimestamp().setText(Util.DateTimeToString(localDataSet.get(position).get_last()));
                } else {
                    viewHolder.getBtnDone().setBackgroundColor(Color.argb(255, 128, 128, 128));
                    localDataSet.get(viewHolder.getAdapterPosition()).perform(LocalDateTime.now());

                    if(localDataSet.get(position).never_performed()) {
                        viewHolder.getTvTimestamp().setText("NEVER");
                    } else if(localDataSet.get(position).done_today()) {
                        viewHolder.getTvTimestamp().setText("TODAY");
                    } else viewHolder.getTvTimestamp().setText(Util.DateTimeToString(localDataSet.get(position).get_last()));
                }

            }
        });

        viewHolder.getBtnRemove().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
                boolean rpl = false;
                builder.setPositiveButton("I do!", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        removeItem(viewHolder);
                    }
                });
                builder.setNegativeButton("Nah fuck that", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancels the dialog.
                    }
                });

                builder.setTitle("Delete that shit?");

                AlertDialog dialog = builder.create();
                dialog.show();


            }
        });

        viewHolder.getBtnShowlog().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {;
                HoboiHistDialog hobDial = new HoboiHistDialog(localDataSet.get(viewHolder.getAdapterPosition()).getTS(), localDataSet.get(viewHolder.getAdapterPosition()).getHob().getName());

                hobDial.show(fragMan, "showhobhist");
            }
        });
    }

    public void removeItem(ViewHolder viewHolder) {
        localDataSet.remove(viewHolder.getAdapterPosition());
        notifyItemRemoved(viewHolder.getAdapterPosition());
        hobremlisten.onHoboiRemove();
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return localDataSet.size();
    }
}
