package com.example.hoboirot;

import static java.time.temporal.ChronoUnit.WEEKS;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.chrono.ChronoLocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;

public class HoboiLog {
    private Hoboi hob;
    private ArrayList<LocalDateTime> timestamps = new ArrayList<>();

    public HoboiLog(Hoboi h) {
        this.hob = h;
    }

    public HoboiLog(HoboiLogSave hls) {
        this.hob = new Hoboi(hls.hobsv);
        this.timestamps = hls.log;
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
        Collections.sort(timestamps);
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
            if(timestamps.get(timestamps.size()-1).toLocalDate().isEqual(LocalDate.now())) {
                hob.setDoneToday(true);
            } else hob.setDoneToday(false);
        } else {
            hob.setDoneToday(false);
        }
    }

    public int in_last_week() {
        LocalDate ldt = LocalDate.now();
        int out = 0;
        for(LocalDateTime l: timestamps) {
            if(l.toLocalDate().isAfter(ldt.minusWeeks(1))) out++;
        }

        return out;
    }

    public int in_last_month() {
        LocalDate ldt = LocalDateTime.now().toLocalDate();
        int out = 0;
        for(LocalDateTime l: timestamps) {
            if(l.toLocalDate().isAfter(ldt.minusMonths(1))) out++;
        }

        return out;
    }

    public float avg_week() {
        if(timestamps.size()<1) return -1;
        else {
            LocalDate earliest = timestamps.get(0).toLocalDate();
            for(LocalDateTime ldt: timestamps) {
                if(ldt.toLocalDate().isBefore(earliest)) earliest = ldt.toLocalDate();
            }

            long timeframe = (earliest.until(LocalDate.now(), ChronoUnit.WEEKS));
            return timeframe < 1 ? -1 : timestamps.size()/(float)timeframe;
        }
    }

    public float avg_month() {
        if(timestamps.size()<1) return -1;
        else {
            LocalDate earliest = timestamps.get(0).toLocalDate();
            for(LocalDateTime ldt: timestamps) {
                if(ldt.toLocalDate().isBefore(earliest)) earliest = ldt.toLocalDate();
            }

            long timeframe = (earliest.until(LocalDate.now(), ChronoUnit.MONTHS))+1;
            return timeframe < 1 ? -1 : timestamps.size()/(float)timeframe;
        }
    }

    /**
     * Calculates every how many days hoboi is performed on average.
     *
     * @return  average timeframe between hoboi performances
     */
    public float avg_every() {
        if(timestamps.size()<2) return -1;

        long[] timeframes = new long[timestamps.size()-1];
        for(int i=0; i<timestamps.size()-1; i++) {
            timeframes[i] = timestamps.get(i).toLocalDate().until(timestamps.get(i+1).toLocalDate(), ChronoUnit.DAYS);
        }

        float out = 0;
        for(int i=0; i< timeframes.length; i++) {
            out += timeframes[i];
        }

        return out/(float)timeframes.length;
    }

    public HoboiLogSave toSave() {
        return new HoboiLogSave(hob.toSave(), timestamps);
    }
}
