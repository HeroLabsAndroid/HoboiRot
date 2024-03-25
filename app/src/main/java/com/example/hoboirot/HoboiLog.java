package com.example.hoboirot;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.chrono.ChronoLocalDateTime;
import java.util.ArrayList;

public class HoboiLog implements Serializable {
    private Hoboi hob;
    private ArrayList<LocalDateTime> timestamps = new ArrayList<>();

    public HoboiLog(Hoboi h) {
        this.hob = h;
    }

    /**
     * @return reference to Hoboi handled by this HoboiLog
     */
    public Hoboi getHob() {
        return hob;
    }

    /**
     * @return reference to timestamps handled by this HoboiLog
     */
    public ArrayList<LocalDateTime> getTS() {
        return timestamps;
    }

    /**
     * Adds a new timestamp to the log.
     * @param ldt   timestamp to add
     */
    public void perform(LocalDateTime ldt) {
        timestamps.add(ldt);
        adjust_done_today();
    }

    /**
     * Remove the latest timestamp from the log.
     */
    public void undo() {
        if(timestamps.size() > 0) {
            timestamps.remove(timestamps.size()-1);
        }
        adjust_done_today();
    }

    /**
     * Returns the timestamp at which this Hoboi was last performed.
     * @return  latest timestamp
     */
    public LocalDateTime get_last() {
        return timestamps.size() > 0 ? timestamps.get(timestamps.size()-1) : null;
    }

    public Util.RecCat getReccat() {
        if(timestamps.size()>0) return Util.getRecency(get_last());
        else return Util.RecCat.NEVER;
    }

    /**
     * @return <b><tt>true</tt></b>, if Hoboi has never been performed, <br>
     *       <b><tt>false</tt></b>, if it has.
     */
    public boolean never_performed() {
        return timestamps.isEmpty();
    }

    /**
     * @return <b><tt>true</tt></b>, if Hoboi has been done today, <br>
     * <b><tt>false</tt></b> if not.
     */
    public boolean done_today() {
        adjust_done_today();
        return hob.isDoneToday();
    }

    /**
     * Updates Hoboi hobs doneToday flag.
     */
    public void adjust_done_today() {
        if(timestamps.size() > 0) {
            if(timestamps.get(timestamps.size()-1).getDayOfYear() == LocalDateTime.now().getDayOfYear()
            && timestamps.get(timestamps.size()-1).getYear() == LocalDateTime.now().getYear()) {
                hob.setDoneToday(true);
            } else hob.setDoneToday(false);
        } else {
            hob.setDoneToday(false);
        }
    }
}
