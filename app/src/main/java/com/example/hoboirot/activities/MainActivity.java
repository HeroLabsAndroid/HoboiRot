package com.example.hoboirot.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.hoboirot.DatProc;
import com.example.hoboirot.HobCat;
import com.example.hoboirot.Hoboi;
import com.example.hoboirot.HoboiLog;
import com.example.hoboirot.R;
import com.example.hoboirot.Util;
import com.example.hoboirot.datadapt.HobCatAdapter;
import com.example.hoboirot.datadapt.HoboiAdapter;
import com.example.hoboirot.dialog.AddHoboiDialog;
import com.example.hoboirot.dialog.HoboiStatDialog;
import com.google.android.material.snackbar.Snackbar;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements AddHoboiDialog.AddHoboiDialogListener, HoboiAdapter.HoboiRemoveListener, HoboiAdapter.SnackbarListener, HobCatAdapter.CatRemoveListener {

    ArrayList<Hoboi> hobbs = new ArrayList<>();
    ArrayList<HobCat> hobcats = new ArrayList<>();
    ArrayList<HoboiLog> logs = new ArrayList<>();
    RecyclerView hobbcat;
    Button btnAddCat, btnStat;
    TextView tvDebug;


    public void save_dat() {
        DatProc.saveData(this, logs);

    }


    public void show_stats() {
        ArrayList<ArrayList<HoboiLog>> hobs_in_cats = new ArrayList<>();

        for(int i = 0; i< Util.RecCat.values().length; i++) {
            hobs_in_cats.add(new ArrayList<>());
        }

        for(HoboiLog hl: logs) {
            hobs_in_cats.get(hl.getReccat().ordinal()).add(hl);
        }

        FragmentManager fragMan = getSupportFragmentManager();
        HoboiStatDialog hobStatDial = new HoboiStatDialog(hobs_in_cats);

        hobStatDial.show(fragMan, "hobstat");
    }

    private int hobCatID(String catID) {
        for(int i=0; i<hobcats.size(); i++) {
            if(hobcats.get(i).name.contentEquals(catID)) return i;
        }

        return -1;
    }

    private void mkHobCats() {
        hobcats = new ArrayList<>();
        for(HoboiLog hl: logs) {
            int hidx = hobCatID(hl.getHob().getCatID());
            if(hidx < 0) {
                ArrayList<HoboiLog> cathobs = new ArrayList<>();
                cathobs.add(hl);
                hobcats.add(new HobCat(cathobs, hl.getHob().getCatID()));
            } else {
                hobcats.get(hidx).hob.add(hl);
            }
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        logs = DatProc.loadData(this);
        for(HoboiLog hl: logs) {
            hobbs.add(hl.getHob());
        }

        logs = Util.sort_hobois_by_name(logs);
        for(HoboiLog hl: logs) {
            Collections.sort(hl.getLog());
        }
        for(HoboiLog hl: logs) {
            ArrayList<Integer> rmv = new ArrayList<>();
            LocalDate last = null;
            for(int i=0; i<hl.getLog().size(); i++) {
                if(last==null) {
                    last = hl.getLog().get(i).ld;
                } else {
                    if(last.isEqual(hl.getLog().get(i).ld))
                       rmv.add(i);
                    last = hl.getLog().get(i).ld;
                }
            }
            for(int r=rmv.size()-1; r>=0; r--) {
                hl.getLog().remove((int)rmv.get(r));
            }
        }

        mkHobCats();
        for(HobCat hc: hobcats) {
            Log.d("HOBCATS", String.format(Locale.getDefault(),"Cat. %s contains %d hobois.", hc.name, hc.hob.size()));
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

        hobbcat = findViewById(R.id.RCLVW_hoboilist);
        btnAddCat = findViewById(R.id.BTN_addHoboiCat);
        btnStat = findViewById(R.id.BTN_stats);
        tvDebug = findViewById(R.id.TXTVW_debug);

        hobbcat.setLayoutManager(new LinearLayoutManager(this));
        HobCatAdapter hobCatAdapt = new HobCatAdapter(this, hobcats, getSupportFragmentManager());
        hobbcat.setAdapter(hobCatAdapt);


        btnStat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                show_stats();
            }
        });

        btnAddCat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragMan = getSupportFragmentManager();
                AddHoboiDialog hobDial = new AddHoboiDialog(true);

                hobDial.show(fragMan, "addcat");
            }
        });

        tvDebug.setText(String.format(Locale.getDefault(), "%d Cats", hobbcat.getAdapter().getItemCount()));
    }

    @Override
    public void onAddHoboiBtnClick(DialogFragment dialog, String name, String catID) {
        int hidx = hobCatID(catID);
        if(hidx<0) Log.e("MAINACTIVITY: hoboiAddedToCat", "cat doesnt exist.");
        else {
            boolean exists = false;
            for(Hoboi hb: hobbs) {
                if(hb.getName().contentEquals(name)) {
                    exists = true;
                    break;
                }
            }
            if(!exists) {
                hobbs.add(new Hoboi(hobbs.size(), name, catID));
                int idx = 0;
                if(!logs.isEmpty()) {
                    while(logs.get(idx).getHob().getName().compareToIgnoreCase(name) < 0 && idx < logs.size()-1) idx++;
                }
                if(idx == logs.size()-1) {
                    if(logs.get(idx).getHob().getName().compareToIgnoreCase(name) < 0) idx++;
                }
                logs.add(idx, new HoboiLog(hobbs.get(hobbs.size()-1)));

                mkHobCats();

                hobbcat.setAdapter(new HobCatAdapter(this, hobcats, getSupportFragmentManager()));

                save_dat();
            } else {
                Snackbar.make(btnAddCat, "Det jibbit schoo", Snackbar.LENGTH_SHORT).show();
            }

            tvDebug.setText(String.format(Locale.getDefault(), "%d cats", hobcats.size()));

        }
    }

    @Override
    public void onAddCatBtnClick(DialogFragment dialog, String name) {
        boolean exists = false;
        for(HobCat hc: hobcats) {
            if(hc.name.compareToIgnoreCase(name) == 0) {
                exists = true;
                break;
            }
        }

        if(!exists) {
            hobcats.add(new HobCat(new ArrayList<>(), name));

            hobbcat.getAdapter().notifyDataSetChanged();
            tvDebug.setText(String.format(Locale.getDefault(), "%d cats", hobcats.size()));
            save_dat();
        } else {
            Snackbar.make(btnAddCat, "Det jibbit schoo", Snackbar.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onHoboiRemove(int idx, String catID) {
        tvDebug.setText(""+hobbcat.getAdapter().getItemCount()+" Items");

        save_dat();
    }

    @Override
    protected void onStop() {
        save_dat();
        super.onStop();
    }

    //Implementation of SnackbarListener Interface from HoboiAdapter
    @Override
    public void sendMsg(String s) {
        Snackbar.make(hobbcat, s, Snackbar.LENGTH_SHORT).show();
    }


    @Override
    public void onCatRemoved(String catID) {
        ArrayList<Integer> toRemove = new ArrayList<>();
        for(int i=0; i<hobbs.size(); i++) {
            if(hobbs.get(i).getCatID().contentEquals(catID)) {
                toRemove.add(i);
            }
        }
        for(int r= toRemove.size()-1; r>=0; r--) hobbs.remove((int)toRemove.get(r));

        ArrayList<Integer> toRemoveLogs = new ArrayList<>();
        for(int i=0; i<logs.size(); i++) {
            if(logs.get(i).getHob().getCatID().contentEquals(catID)) {
                toRemoveLogs.add(i);
            }
        }
        for(int r= toRemoveLogs.size()-1; r>=0; r--) logs.remove((int)toRemoveLogs.get(r));

        mkHobCats();
        hobbcat.setAdapter(new HobCatAdapter(this, hobcats, getSupportFragmentManager()));
    }
}