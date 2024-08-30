package com.example.hoboirot.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hoboirot.HobPerf;
import com.example.hoboirot.HoboiLog;
import com.example.hoboirot.R;
import com.example.hoboirot.datadapt.HoboiHistAdapter;
import com.example.hoboirot.datadapt.HoboiStatAdapter;
import com.example.hoboirot.statview.StatView;
import com.example.hoboirot.statview.StatViewable;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class HoboiStatDialog extends DialogFragment implements StatViewable {


    RecyclerView rcvReccats;
    ArrayList<ArrayList<HoboiLog>> hobois_in_cats;
    ArrayList<HoboiLog> hoboi_list;

    StatView statView;

    public HoboiStatDialog(ArrayList<ArrayList<HoboiLog>> hobs_reccat) {
        hobois_in_cats = hobs_reccat;
        hoboi_list = hobb_list();
    }

    private ArrayList<HoboiLog> hobb_list() {
        ArrayList<HoboiLog> hl = new ArrayList<>();
        for (ArrayList<HoboiLog> catlog: hobois_in_cats) {
            hl.addAll(catlog);
        }

        return hl;
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction.
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());

        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View layout = inflater.inflate(R.layout.dlg_hoboistats, null);


        rcvReccats = (RecyclerView) layout.findViewById(R.id.RCLVW_reccat);
        rcvReccats.setLayoutManager(new LinearLayoutManager(requireActivity()));

        HoboiStatAdapter hobstatAdapt = new HoboiStatAdapter(hobois_in_cats, requireContext());
        rcvReccats.setAdapter(hobstatAdapt);

        statView = (StatView) layout.findViewById(R.id.SV_gridstat);
        statView.attachViewable(this);
        statView.invalidate();

        builder.setView(layout);

        // Create the AlertDialog object and return it.
        return builder.create();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

    }

    @Override
    public int get_nr_dsets() {
        return hoboi_list.size();
    }

    @Override
    public boolean done_on_day(LocalDate date, int dset) {
        ArrayList<HobPerf> log = hoboi_list.get(dset).getLog();
        for(HobPerf hp: log) {
            if(hp.ld.isEqual(date)) return true;
        }
        return false;
    }

    @Override
    public LocalDate get_earliest() {
        LocalDate earliest = LocalDate.MAX;
        for (int i=0; i<hoboi_list.size(); i++) {
            ArrayList<HobPerf> log = hoboi_list.get(i).getLog();
            for(HobPerf hp: log) {
                if(hp.ld.isBefore(earliest)) earliest=hp.ld;
            }
        }

        return earliest;
    }

    @Override
    public String get_dset_name(int dset) {
        return hoboi_list.get(dset).getHob().getName();
    }
}

