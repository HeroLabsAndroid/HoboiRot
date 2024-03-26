package com.example.hoboirot;

import android.content.Context;
import android.util.Log;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Locale;

public class DatProc {
    static public ArrayList<HoboiLog> loadData(Context con) {
        ArrayList<HoboiLog> dat = new ArrayList<>();

        try {
            FileInputStream fis = con.openFileInput("hoblog.dat");
            ObjectInputStream ois = new ObjectInputStream(fis);
            Log.d("RDDAT", String.format(Locale.getDefault(), "Opened file (%s) and Object Stream.", fis.toString()));
            int cnt = (int)ois.readObject();
            for(int i=0; i< cnt; i++) dat.add(new HoboiLog((HoboiLogSave) ois.readObject()));
            Log.d("RDDAT", String.format(Locale.getDefault(),"Read %d objects.",dat.size()));
            ois.close();
            fis.close();
        } catch(Exception e) {
            Log.e("RDDAT", e.toString());
        }

        return dat;
    }

    static public void saveData(Context con, ArrayList<HoboiLog> logs) {
        try {
            FileOutputStream fos = con.openFileOutput("hoblog.dat", Context.MODE_PRIVATE);
            Log.d("SAVDAT", "Opened file.");
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            Log.d("SAVDAT", "Opened Object Stream");
            oos.writeObject(logs.size());
            for(HoboiLog hl: logs) {
                oos.writeObject(hl.toSave());
            }
            Log.d("SAVDAT", String.format(Locale.getDefault(), "Wrote %d Objects.", logs.size()));
            oos.close();
            fos.close();
        } catch (Exception e) {
            Log.e("SAVDAT", e.toString());
        }

    }
}
