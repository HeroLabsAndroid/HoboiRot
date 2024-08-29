package com.example.hoboirot.datadapt;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hoboirot.HobCat;
import com.example.hoboirot.R;
import com.example.hoboirot.dialog.AddHoboiDialog;

import java.util.ArrayList;
import java.util.Locale;

public class HobCatAdapter extends RecyclerView.Adapter<HobCatAdapter.ViewHolder> implements HoboiAdapter.HoboiRemoveListener, AddHoboiDialog.AddHoboiDialogListener {

    ViewHolder hold;
    ArrayList<HobCat> data;
    Context c;

    FragmentManager fragMan;

    public interface CatRemoveListener {
        void onCatRemoved(String catID);
    }

    HoboiAdapter.HoboiRemoveListener hrListen;
    CatRemoveListener crListen;

    public HobCatAdapter(Context context, ArrayList<HobCat> data, FragmentManager fragMan) {
        c = context;
        hrListen = (HoboiAdapter.HoboiRemoveListener) c;
        crListen = (CatRemoveListener) c;
        this.data = data;
        this.fragMan = fragMan;
    }

    @NonNull
    @Override
    public HobCatAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.lstitm_hobcat, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HobCatAdapter.ViewHolder holder, int position) {

        holder.catTitTV.setText(data.get(position).name);

        holder.debugTV.setText(String.format(Locale.getDefault(), "%d hobbs", data.get(position).hob.size()));

        holder.yeetCatBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                crListen.onCatRemoved(data.get(holder.getAdapterPosition()).name);
            }
        });

        holder.addHobBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddHoboiDialog hobDial = new AddHoboiDialog(data.get(holder.getAdapterPosition()).name);

                hobDial.show(fragMan, "addhob");
            }
        });

        holder.hobRecView.setLayoutManager(new LinearLayoutManager(c));
        HoboiAdapter hobAdapt = new HoboiAdapter(data.get(position).hob, data.get(position).name, c, fragMan);
        holder.hobRecView.setAdapter(hobAdapt);

        hold = holder;
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    @Override
    public void onHoboiRemove(int idx, String catID) {
        hrListen.onHoboiRemove(idx, catID);
    }

    @Override
    public void onAddHoboiBtnClick(DialogFragment dialog, String name, String catID) {
        for(int i=0; i<data.size(); i++) {
            if(catID.contentEquals(data.get(i).name)) {
                notifyItemChanged(i);
            }
        }
    }

    @Override
    public void onAddCatBtnClick(DialogFragment dialog, String name) {

    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        RecyclerView hobRecView;
        TextView catTitTV, debugTV;
        Button addHobBtn, yeetCatBtn;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            hobRecView = itemView.findViewById(R.id.RCLV_hobcat_hobs);
            catTitTV = itemView.findViewById(R.id.TV_hobcat_cattit);
            debugTV = itemView.findViewById(R.id.TV_hobcat_debug);
            addHobBtn = itemView.findViewById(R.id.BTN_hobcats_addhob);
            yeetCatBtn = itemView.findViewById(R.id.BTN_hobcats_yeetcat);
        }
    }
}