package com.example.hoboirot;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.hoboirot.datadapt.HoboiAdapter;
import com.example.hoboirot.dialog.AddHoboiDialog;
import com.example.hoboirot.dialog.HoboiStatDialog;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Random;

public class MainActivity extends AppCompatActivity implements AddHoboiDialog.AddHoboiDialogListener, HoboiAdapter.HoboiRemoveListener {

    ArrayList<Hoboi> hobbs = new ArrayList<>();
    ArrayList<HoboiLog> logs = new ArrayList<>();
    RecyclerView hobblist;
    Button btnAdd, btnStat;
    TextView tvDebug;


    public void save_dat() {
        DatProc.saveData(this, logs);
    }

    public void show_stats() {
        ArrayList<ArrayList<HoboiLog>> hobs_in_cats = new ArrayList<>();

        for(int i=0; i<Util.RecCat.values().length; i++) {
            hobs_in_cats.add(new ArrayList<>());
        }

        for(HoboiLog hl: logs) {
            hobs_in_cats.get(hl.getReccat().ordinal()).add(hl);
        }


        FragmentManager fragMan = getSupportFragmentManager();
        HoboiStatDialog hobStatDial = new HoboiStatDialog(hobs_in_cats);

        hobStatDial.show(fragMan, "hobstat");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        logs = DatProc.loadData(this);
        for(HoboiLog hl: logs) {
            hobbs.add(hl.getHob());
        }

        /*hobbs.add(new Hoboi(0, "Dummy1"));
        hobbs.add(new Hoboi(1, "Dummy2"));
        hobbs.add(new Hoboi(2, "Dummy3"));
        hobbs.add(new Hoboi(3, "Dummy4"));

        for(Hoboi h: hobbs) {
            logs.add(new HoboiLog(h));
        }
        Random r = new Random();
        for(int i=0; i<69; i++) {
            logs.get(r.nextInt(4)).perform(LocalDateTime.now().minusDays(r.nextInt() % 31));
        }*/

        hobblist = findViewById(R.id.RCLVW_hoboilist);
        btnAdd = findViewById(R.id.BTN_addHoboi);
        btnStat = findViewById(R.id.BTN_stats);
        tvDebug = findViewById(R.id.TXTVW_debug);

        hobblist.setLayoutManager(new LinearLayoutManager(this));
        HoboiAdapter hobAdapt = new HoboiAdapter(logs, this, getSupportFragmentManager());
        hobblist.setAdapter(hobAdapt);


        btnStat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                show_stats();
            }
        });

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragMan = getSupportFragmentManager();
                AddHoboiDialog hobDial = new AddHoboiDialog();

                hobDial.show(fragMan, "addhob");
            }
        });

        tvDebug.setText(String.format(Locale.getDefault(), "%d Items", hobblist.getAdapter().getItemCount()));
    }

    @Override
    public void onAddHoboiBtnClick(DialogFragment dialog, String name) {
        hobbs.add(new Hoboi(hobbs.size(), name));
        logs.add(new HoboiLog(hobbs.get(hobbs.size()-1)));
        hobblist.getAdapter().notifyItemInserted(logs.size()-1);
        hobblist.getAdapter().notifyItemRangeChanged(logs.size()-1, 1);
        tvDebug.setText(""+hobblist.getAdapter().getItemCount()+" Items");

        save_dat();
    }

    @Override
    public void onHoboiRemove() {
        tvDebug.setText(""+hobblist.getAdapter().getItemCount()+" Items");
        save_dat();
    }

    @Override
    protected void onStop() {
        save_dat();
        super.onStop();
    }
}