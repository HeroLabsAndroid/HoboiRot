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

import com.example.hoboirot.HoboiLog;
import com.example.hoboirot.R;
import com.example.hoboirot.datadapt.HoboiHistAdapter;
import com.example.hoboirot.datadapt.HoboiStatAdapter;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class HoboiStatDialog extends DialogFragment {


    RecyclerView rcvReccats;
    ArrayList<ArrayList<HoboiLog>> hobois_in_cats;

    public HoboiStatDialog(ArrayList<ArrayList<HoboiLog>> hobs_reccat) {
        hobois_in_cats = hobs_reccat;
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

        builder.setView(layout);


        // Create the AlertDialog object and return it.
        return builder.create();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

    }
}

