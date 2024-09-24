package com.example.hoboirot;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * Data class holding info about a Hoboi Performance
 */
public class HobPerf implements Serializable, Comparable<HobPerf> {
    public LocalDate ld;
    public boolean suppl;

    //------------------ I/O --------------------------------------//

    public JSONObject toJSON() throws JSONException {
        JSONObject jsave = new JSONObject();

        jsave.put("ldt", new LDTSave(ld.atStartOfDay()).toJSON());
        jsave.put("suppl", suppl);

        return jsave;
    }

    public HobPerf(JSONObject jsave) throws JSONException {
        suppl = jsave.getBoolean("suppl");
        ld = (new LDTSave(jsave.getJSONObject("ldt"))).toLDT().toLocalDate();
    }

    //-------------------------------------------------------------//

    public HobPerf(LocalDate ld, boolean suppl) {
        this.ld = ld;
        this.suppl = suppl;
    }

    @Override
    public int compareTo(HobPerf o) {
        return ld.compareTo(o.ld);
    }
}
