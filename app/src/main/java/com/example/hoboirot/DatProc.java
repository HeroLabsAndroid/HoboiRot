package com.example.hoboirot;

import static androidx.core.app.ActivityCompat.startActivityForResult;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Objects;

public class DatProc {
   /* static public ArrayList<HoboiLog> loadData(Context con) {
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
    }*/

    static public boolean exportData(ArrayList<HoboiLog> logs, Uri uri, ContentResolver conres) {
        try {
            String state = Environment.getExternalStorageState();
            if(!Environment.MEDIA_MOUNTED.equals(state)) {
                return false;
            }
            ParcelFileDescriptor pfd = conres.openFileDescriptor(uri, "w");
            assert pfd != null;
            FileOutputStream fos = new FileOutputStream(pfd.getFileDescriptor());

            fos.write(dataToJSON(logs).getBytes());
            fos.flush();
            fos.close();
            pfd.close();
            return true;
        } catch(Exception e) {
            Log.e("DATEXPORT", Objects.requireNonNull(e.getMessage()));
            e.printStackTrace();
            return false;
        }
    }

    static public String dataToJSON(ArrayList<HoboiLog> logs) throws JSONException {
        JSONObject jsave = new JSONObject();
        JSONArray jarr = new JSONArray();
        for (HoboiLog hl: logs) {
            jarr.put(hl.toJSON());
        }
        jsave.put("logs", jarr);

        return jsave.toString();
    }

    static public void saveJSONData(Context con, ArrayList<HoboiLog> logs) {
        try {
            OutputStreamWriter osw = new OutputStreamWriter(con.openFileOutput("hoblog.dat", Context.MODE_PRIVATE));
            Log.d("SAVDAT_JSON", "Opened file.");


            osw.write(dataToJSON(logs));
            osw.flush();
            osw.close();
        } catch (Exception e) {
            Log.e("SAVDAT_JSON", e.toString());
        }

    }

    static public ArrayList<HoboiLog> import_data(Uri uri, ContentResolver conres) {
        try {
            String state = Environment.getExternalStorageState();
            if(!Environment.MEDIA_MOUNTED.equals(state)) {
                return new ArrayList<HoboiLog>();
            }
            ParcelFileDescriptor pfd = conres.openFileDescriptor(uri, "r");
            assert pfd != null;
            FileInputStream fis = new FileInputStream(pfd.getFileDescriptor());

            FileReader fileReader = new FileReader(fis.getFD());
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            StringBuilder stringBuilder = new StringBuilder();

            String line = bufferedReader.readLine();
            while (line != null){
                stringBuilder.append(line).append("\n");
                line = bufferedReader.readLine();
            }
            bufferedReader.close();// This response will have Json Format String

            String response = stringBuilder.toString();

            fis.close();
            pfd.close();
            return dataFromJSON(response);
        } catch(Exception e) {
            Log.e("DATEXPORT", Objects.requireNonNull(e.getMessage()));
            e.printStackTrace();
            return new ArrayList<HoboiLog>();
        }
    }

    static public ArrayList<HoboiLog> dataFromJSON(String jstring) throws JSONException {
        ArrayList<HoboiLog> logs = new ArrayList<>();
        JSONObject jsave = new JSONObject(jstring);
        JSONArray jarr = jsave.getJSONArray("logs");
        for (int i = 0; i < jarr.length(); i++) {
            HoboiLog hl = new HoboiLog(jarr.getJSONObject(i));
            logs.add(hl);
        }

        return logs;
    }

    static public ArrayList<HoboiLog> loadJSONData(Context con) throws IOException, JSONException {
        ArrayList<HoboiLog> out = new ArrayList<>();
        String result = "";
        InputStream inputStream = con.openFileInput("hoblog.dat");
        if(inputStream != null)
        {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String temp = "";
            StringBuilder stringBuilder = new StringBuilder();

            while((temp = bufferedReader.readLine()) != null)
            {
                stringBuilder.append(temp);
                stringBuilder.append("\n");
            }

            inputStream.close();
            result = stringBuilder.toString();

            try {
               out = dataFromJSON(result);
            } catch (Exception e) {
                Log.e("LOADDAT_JSON", "Error reading save data");
            }

        } else {
            Log.d("LOADDAT_JSON", "ERROR OPENING SAVEFILE");
        }

        return out;
    }

   /* static public void saveData(Context con, ArrayList<HoboiLog> logs) {
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

    }*/
}
