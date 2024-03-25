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

import com.example.hoboirot.R;
import com.example.hoboirot.datadapt.HoboiHistAdapter;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class HoboiHistDialog extends DialogFragment {


    RecyclerView hobhist;
    TextView tvHobtit;
    ArrayList<LocalDateTime> timestamps = new ArrayList<>();
    String hobtit;

    public HoboiHistDialog(ArrayList<LocalDateTime> ts, String tit) {
        timestamps = ts;
        hobtit = tit;
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction.
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());

        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View layout = inflater.inflate(R.layout.dlg_showhoboihistory, null);

        tvHobtit = (TextView) layout.findViewById(R.id.TV_hobtit);
        tvHobtit.setText(hobtit);

        hobhist = (RecyclerView) layout.findViewById(R.id.RCLVW_hobhist);
        hobhist.setLayoutManager(new LinearLayoutManager(requireActivity()));

        HoboiHistAdapter hobhistAdapt = new HoboiHistAdapter(timestamps, requireContext());
        hobhist.setAdapter(hobhistAdapt);

        builder.setView(layout);


        // Create the AlertDialog object and return it.
        return builder.create();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

    }
}

