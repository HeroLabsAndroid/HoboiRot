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

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements AddHoboiDialog.AddHoboiDialogListener, HoboiAdapter.HoboiRemoveListener {

    ArrayList<Hoboi> hobbs = new ArrayList<>();
    RecyclerView hobblist;
    Button btnAdd;
    TextView tvDebug;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        hobbs.add(new Hoboi(0, "Dummy1"));
        hobbs.add(new Hoboi(1, "Dummy2"));



        hobblist = findViewById(R.id.RCLVW_hoboilist);
        btnAdd = findViewById(R.id.BTN_addHoboi);
        tvDebug = findViewById(R.id.TXTVW_debug);

        hobblist.setLayoutManager(new LinearLayoutManager(this));
        HoboiAdapter hobAdapt = new HoboiAdapter(hobbs, this);
        hobblist.setAdapter(hobAdapt);



        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragMan = getSupportFragmentManager();
                AddHoboiDialog hobDial = new AddHoboiDialog();

                hobDial.show(fragMan, "addhob");
            }
        });

        tvDebug.setText(""+hobblist.getAdapter().getItemCount()+" Items");
    }

    @Override
    public void onAddHoboiBtnClick(DialogFragment dialog, String name) {
        hobbs.add(new Hoboi(hobbs.size(), name));
        hobblist.getAdapter().notifyItemInserted(hobbs.size()-1);
        hobblist.getAdapter().notifyItemRangeChanged(hobbs.size()-1, 1);
        tvDebug.setText(""+hobblist.getAdapter().getItemCount()+" Items");

    }

    @Override
    public void onHoboiRemove() {
        tvDebug.setText(""+hobblist.getAdapter().getItemCount()+" Items");
    }
}