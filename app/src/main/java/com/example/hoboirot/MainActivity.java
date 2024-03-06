package com.example.hoboirot;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.hoboirot.datadapt.HoboiAdapter;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements AddHoboiDialog.AddHoboiDialogListener {

    ArrayList<Hoboi> hobbs = new ArrayList<>();
    RecyclerView hobblist;
    Button btnAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        hobbs.add(new Hoboi(0, "Dummy1"));
        hobbs.add(new Hoboi(1, "Dummy2"));

        HoboiAdapter hobAdapt = new HoboiAdapter(hobbs);

        hobblist = (RecyclerView) findViewById(R.id.RCLVW_hoboilist);
        btnAdd = (Button) findViewById(R.id.BTN_addHoboi);

        hobblist.setAdapter(hobAdapt);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragMan = getSupportFragmentManager();
                AddHoboiDialog hobDial = new AddHoboiDialog();

                hobDial.show(fragMan, "addhob");
            }
        });
    }

    @Override
    public void onAddHoboiBtnClick(DialogFragment dialog, String name) {
        hobbs.add(new Hoboi(hobbs.size(), name));
        hobblist.getAdapter().notifyItemInserted(hobbs.size()-1);
        hobblist.getAdapter().notifyItemRangeChanged(hobbs.size()-1, 1);

    }
}