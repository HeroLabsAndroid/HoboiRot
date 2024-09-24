package com.example.hoboirot;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class Hoboi {

    private String catID = "";
    private int id;
    private String name;
    private boolean doneToday;


    //------------------ I/O --------------------------------------//

    public JSONObject toJSON() throws JSONException {
        JSONObject jsave = new JSONObject();

        jsave.put("catID", catID);
        jsave.put("id", id);
        jsave.put("name", name);

        return jsave;
    }

    public Hoboi(JSONObject jsave) throws JSONException {
        catID = jsave.optString("catID", "NONE");
        id = jsave.getInt("id");
        name = jsave.getString("name");
    }

    //-------------------------------------------------------------//

    public Hoboi(int id, String name, String catID) {
        this.id = id;
        this.name = name;
        this.doneToday = false;
        this.catID = catID;
    }


    public Hoboi(HoboiSave hs) {
        id = hs.id;
        name = hs.name;
        catID = hs.catID;
        doneToday = false;
    }

    public String getCatID() {
        return catID;
    }

    public void setCatID(String catID) {
        this.catID = catID;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isDoneToday() {
        return doneToday;
    }

    public void setDoneToday(boolean doneToday) {
        this.doneToday = doneToday;
    }

    public HoboiSave toSave() {
        return new HoboiSave(id, name, catID);
    }
}
