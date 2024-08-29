package com.example.hoboirot.datadapt;


import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hoboirot.Hoboi;
import com.example.hoboirot.HoboiLog;
import com.example.hoboirot.R;
import com.example.hoboirot.Util;
import com.example.hoboirot.dialog.AddHoboiDialog;
import com.example.hoboirot.dialog.DatePickerFragment;
import com.example.hoboirot.dialog.HoboiHistDialog;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Locale;

public class HoboiAdapter extends RecyclerView.Adapter<HoboiAdapter.ViewHolder> implements DatePickerDialog.OnDateSetListener {



    public interface HoboiRemoveListener {
        public void onHoboiRemove(int idx, String catID);
    }

    public interface SnackbarListener {
        public void sendMsg(String s);
    }

    HoboiRemoveListener hobremlisten;
    SnackbarListener snacclisten;
    FragmentManager fragMan;
    Context ctx;

    String catID;

    int longclickidx = -1;

    private ArrayList<HoboiLog> localDataSet;

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder)
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvName, tvTimestamp;
        private final MaterialButton btnDone, btnRemove, btnShowlog;

        private final FrameLayout frlytReccatbar;




        public ViewHolder(View view) {
            super(view);
            // Define click listener for the ViewHolder's View

            tvTimestamp = (TextView) view.findViewById(R.id.TXTVIEW_lastperf);
            tvName = (TextView) view.findViewById(R.id.TXTVIEW_hoboiname);
            btnDone = (MaterialButton) view.findViewById(R.id.BTN_hoboidone);
            btnRemove = (MaterialButton) view.findViewById(R.id.BTN_removehoboi);
            btnShowlog = (MaterialButton) view.findViewById(R.id.BTN_showlog);
            frlytReccatbar = (FrameLayout) view.findViewById(R.id.FRMLYT_recindic);
        }

        public FrameLayout getFrlytReccatbar() { return frlytReccatbar; }
        public MaterialButton getBtnShowlog() {
            return btnShowlog;
        }

        public TextView getTvName() {
            return tvName;
        }

        public MaterialButton getBtnDone() {
            return btnDone;
        }

        public MaterialButton getBtnRemove() {
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
    public HoboiAdapter(ArrayList<HoboiLog> dataSet, String catID, Context c, FragmentManager fm) {
        localDataSet = dataSet;
        hobremlisten = (HoboiRemoveListener) c;
        snacclisten = (SnackbarListener) c;
        this.catID = catID;

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
        viewHolder.getFrlytReccatbar().setBackgroundColor(localDataSet.get(position).getReccat().toColor(ctx));

        if(localDataSet.get(position).never_performed()) {
            viewHolder.getTvTimestamp().setText("NEVER");
        } else if(localDataSet.get(position).done_today()) {
            viewHolder.getTvTimestamp().setText("TODAY");
        } else viewHolder.getTvTimestamp().setText(Util.DateToString(localDataSet.get(position).get_last()));

        if(!localDataSet.get(position).done_today()) {
            Drawable dr = ContextCompat.getDrawable(ctx, R.drawable.btn_uncheck);
            viewHolder.getBtnDone().setBackgroundColor(ContextCompat.getColor(ctx, R.color.ACC_5));
            viewHolder.getBtnDone().setIcon(dr);
        } else {
            Drawable dr = ContextCompat.getDrawable(ctx, R.drawable.btn_check);
            viewHolder.getBtnDone().setBackgroundColor(ContextCompat.getColor(ctx, R.color.ACC_4));
            viewHolder.getBtnDone().setIcon(dr);
        }
        viewHolder.getBtnDone().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(localDataSet.get(viewHolder.getAdapterPosition()).done_today()) {
                    Drawable dr = ContextCompat.getDrawable(ctx, R.drawable.btn_uncheck);
                    viewHolder.getBtnDone().setBackgroundColor(ContextCompat.getColor(ctx, R.color.ACC_5));
                    viewHolder.getBtnDone().setIcon(dr);
                    localDataSet.get(viewHolder.getAdapterPosition()).undo();

                    if(localDataSet.get(viewHolder.getAdapterPosition()).never_performed()) {
                        viewHolder.getTvTimestamp().setText("NEVER");
                    } else if(localDataSet.get(viewHolder.getAdapterPosition()).done_today()) {
                        viewHolder.getTvTimestamp().setText("TODAY");
                    } else viewHolder.getTvTimestamp().setText(Util.DateToString(localDataSet.get(viewHolder.getAdapterPosition()).get_last()));
                } else {
                    Drawable dr = ContextCompat.getDrawable(ctx, R.drawable.btn_check);
                    viewHolder.getBtnDone().setBackgroundColor(ContextCompat.getColor(ctx, R.color.ACC_4));
                    viewHolder.getBtnDone().setIcon(dr);
                    localDataSet.get(viewHolder.getAdapterPosition()).perform(LocalDateTime.now());

                    if(localDataSet.get(viewHolder.getAdapterPosition()).never_performed()) {
                        viewHolder.getTvTimestamp().setText("NEVER");
                    } else if(localDataSet.get(viewHolder.getAdapterPosition()).done_today()) {
                        viewHolder.getTvTimestamp().setText("TODAY");
                    } else viewHolder.getTvTimestamp().setText(Util.DateToString(localDataSet.get(viewHolder.getAdapterPosition()).get_last()));
                }

                viewHolder.getFrlytReccatbar().setBackgroundColor(localDataSet.get(viewHolder.getAdapterPosition()).getReccat().toColor(ctx));

            }
        });

        viewHolder.getBtnDone().setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                longclickidx = viewHolder.getAdapterPosition();
                DatePickerFragment dpFrag = new DatePickerFragment(HoboiAdapter.this);
                dpFrag.show(fragMan, "datepicc");
                return true;
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
                HoboiHistDialog hobDial = new HoboiHistDialog(localDataSet.get(viewHolder.getAdapterPosition()));

                hobDial.show(fragMan, "showhobhist");
            }
        });
    }

    public void removeItem(ViewHolder viewHolder) {
        localDataSet.remove(viewHolder.getAdapterPosition());
        notifyItemRemoved(viewHolder.getAdapterPosition());
        hobremlisten.onHoboiRemove(viewHolder.getAdapterPosition(), catID);
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return localDataSet.size();
    }

    //Called when date selected in date picker (upon long-click on doneBtn)
    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        int idx = longclickidx;
        longclickidx = -1;

        if(idx >= 0) {
            if(!localDataSet.get(idx).performed_on(LocalDate.of(year, month+1, dayOfMonth))) {
                localDataSet.get(idx).perform(LocalDate.of(year, month+1, dayOfMonth).atStartOfDay());
                snacclisten.sendMsg("Nachtrag: "+localDataSet.get(idx).getHob().getName()+" am "+dayOfMonth+"."+(month+1)+"."+year);
            } else {
                localDataSet.get(idx).unperform(LocalDate.of(year, month+1, dayOfMonth));
                snacclisten.sendMsg("Rückgängig: "+localDataSet.get(idx).getHob().getName()+" am "+dayOfMonth+"."+(month+1)+"."+year);
            }
        }
    }
}
