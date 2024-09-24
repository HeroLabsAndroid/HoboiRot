package com.example.hoboirot;

import org.json.JSONException;
import org.json.JSONObject;

import java.time.LocalDateTime;

public class LDTSave {

    //------------------ I/O --------------------------------------//
    int year, month, day, hour, minute, second;

    public JSONObject toJSON() throws JSONException {
        JSONObject jsave = new JSONObject();

        jsave.put("year", year);
        jsave.put("month", month);
        jsave.put("day", day);
        jsave.put("hour", hour);
        jsave.put("minute", minute);
        jsave.put("second", second);

        return jsave;
    }

    public LDTSave(JSONObject jsave) throws JSONException {
        year = jsave.getInt("year");
        month = jsave.getInt("month");
        day = jsave.getInt("day");
        hour = jsave.getInt("hour");
        minute = jsave.getInt("minute");
        second = jsave.getInt("second");
    }

    public LDTSave(LocalDateTime ldt) {
        year = ldt.getYear();
        month = ldt.getMonth().getValue();
        day = ldt.getDayOfMonth();
        hour = ldt.getHour();
        minute = ldt.getMinute();
        second = ldt.getSecond();
    }

    public LocalDateTime toLDT() {
        return LocalDateTime.of(year, month, day, hour, minute, second);
    }

    //-------------------------------------------------------------//
}
