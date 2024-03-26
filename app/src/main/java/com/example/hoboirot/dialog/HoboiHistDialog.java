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
import com.example.hoboirot.Const;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Locale;

public class HoboiHistDialog extends DialogFragment {


    RecyclerView hobhist;
    TextView tvHobtit;
    ArrayList<LocalDateTime> timestamps = new ArrayList<>();
    String hobtit;

    HoboiLog hl;

    ArrayList<TextView> tvFreq = new ArrayList<>();
    ArrayList<TextView> tvFreqNames = new ArrayList<>();

    public HoboiHistDialog(HoboiLog hoblog) {
        timestamps = hoblog.getTS();
        hobtit = hoblog.getHob().getName();
        hl = hoblog;
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

        View lastweek = layout.findViewById(R.id.TITFRQVW_lastweek);
        View lastmonth = layout.findViewById(R.id.TITFRQVW_lastmonth);
        View weekly = layout.findViewById(R.id.TITFRQVW_weekly);
        View monthly = layout.findViewById(R.id.TITFRQVW_monthly);

        tvFreq.add((TextView) lastweek.findViewById(R.id.TV_hob_freq));
        tvFreq.get(tvFreq.size()-1).setText(String.format(Locale.getDefault(),"x%d", hl.in_last_week()));
        tvFreqNames.add((TextView) lastweek.findViewById(R.id.TV_hobfreq_tit));

        tvFreq.add((TextView) lastmonth.findViewById(R.id.TV_hob_freq));
        tvFreq.get(tvFreq.size()-1).setText(String.format(Locale.getDefault(),"x%d", hl.in_last_month()));
        tvFreqNames.add((TextView) lastmonth.findViewById(R.id.TV_hobfreq_tit));

        tvFreq.add((TextView) weekly.findViewById(R.id.TV_hob_freq));
        String wk_avg = (hl.avg_week() == -1) ? "insuff. data" : String.format(Locale.getDefault(), "%.2f", hl.avg_week());
        tvFreq.get(tvFreq.size()-1).setText(wk_avg);
        tvFreqNames.add((TextView) weekly.findViewById(R.id.TV_hobfreq_tit));

        tvFreq.add((TextView) monthly.findViewById(R.id.TV_hob_freq));
        String mn_avg = (hl.avg_week() == -1) ? "insuff. data" : String.format(Locale.getDefault(), "%.2f", hl.avg_month());
        tvFreq.get(tvFreq.size()-1).setText(mn_avg);
        tvFreqNames.add((TextView) monthly.findViewById(R.id.TV_hobfreq_tit));

        for(int i=0; i<Const.HOBFREQTIT.length; i++) {
            tvFreqNames.get(i).setText(Const.HOBFREQTIT[i]);
        }


        // Create the AlertDialog object and return it.
        return builder.create();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

    }
}

