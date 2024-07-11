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
    private ArrayList<HobPerf> log = new ArrayList<>();


    public HoboiLog(Hoboi h) {
        this.hob = h;
    }

    public HoboiLog(HoboiLogSave hls) {
        this.hob = new Hoboi(hls.hobsv);
        this.log = hls.log;
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
    public ArrayList<HobPerf> getLog() {
        return log;
    }


    /**
     * Adds a new timestamp to the log.
     * @param ldt   timestamp to add
     */
    public void perform(LocalDateTime ldt) {
        log.add(new HobPerf(ldt.toLocalDate(), !ldt.toLocalDate().isEqual(LocalDate.now())));
        Collections.sort(log);

        adjust_done_today();
    }

    /**
     * Removes a timestamp from the log (if it exists).
     * @param ld    Timestamp to remove
     */
    public void unperform(LocalDate ld) {
        int rmidx = -1;
        for(int i=0; i<log.size(); i++) {
            if(ld.isEqual(log.get(i).ld)) {
                rmidx = i;
                break;
            }
        }

        if(rmidx >= 0) {
            log.remove(rmidx);
        }
    }

    /**
     * Remove the latest timestamp from the log.
     */
    public void undo() {
        if(log.size() > 0) {
            log.remove(log.size()-1);
        }
        adjust_done_today();
    }

    /**
     * Returns the timestamp at which this Hoboi was last performed.
     * @return  latest timestamp
     */
    public LocalDate get_last() {
        return log.size() > 0 ? log.get(log.size()-1).ld : null;
    }

    public Util.RecCat getReccat() {
        if(log.size()>0) return Util.getRecency(get_last());
        else return Util.RecCat.NEVER;
    }

    /**
     * @return <b><tt>true</tt></b>, if Hoboi has never been performed, <br>
     *       <b><tt>false</tt></b>, if it has.
     */
    public boolean never_performed() {
        return log.isEmpty();
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
        if(log.size() > 0) {
            if(log.get(log.size()-1).ld.isEqual(LocalDate.now())) {
                hob.setDoneToday(true);
            } else hob.setDoneToday(false);
        } else {
            hob.setDoneToday(false);
        }
    }

    /**
     * Check if hoboi was performed on a given date.
     * @param ld    The given date
     * @return      <b>true,</b> if <tt>timestamps</tt> contains an entry from the same date as <tt>ld</tt>,<br>
     *              <b>false,</b> otherwise
     */
    public boolean performed_on(LocalDate ld) {
        for(HobPerf hp: log) {
            if(hp.ld.isEqual(ld)) return true;
        }
        return false;
    }

    public int in_last_week() {
        LocalDate ldt = LocalDate.now();
        int out = 0;
        for(HobPerf hp: log) {
            if(hp.ld.isAfter(ldt.minusWeeks(1))) out++;
        }

        return out;
    }

    public int in_last_month() {
        LocalDate ldt = LocalDateTime.now().toLocalDate();
        int out = 0;
        for(HobPerf hp: log) {
            if(hp.ld.isAfter(ldt.minusMonths(1))) out++;
        }

        return out;
    }

    public float avg_week() {
        if(log.isEmpty()) return -1;
        else {
            LocalDate earliest = log.get(0).ld;
            for(HobPerf hp: log) {
                if(hp.ld.isBefore(earliest)) earliest = hp.ld;
            }

            long timeframe = (earliest.until(LocalDate.now(), ChronoUnit.WEEKS));
            return timeframe < 1 ? -1 : log.size()/(float)timeframe;
        }
    }

    public float avg_month() {
        if(log.isEmpty()) return -1;
        else {
            LocalDate earliest = log.get(0).ld;
            for(HobPerf hp: log) {
                if(hp.ld.isBefore(earliest)) earliest = hp.ld;
            }

            long timeframe = (earliest.until(LocalDate.now(), ChronoUnit.MONTHS))+1;
            return timeframe < 1 ? -1 : log.size()/(float)timeframe;
        }
    }

    /**
     * Calculates every how many days hoboi is performed on average.
     *
     * @return  average timeframe between hoboi performances
     */
    public float avg_every() {
        if(log.size()<2) return -1;

        /*long[] timeframes = new long[timestamps.size()-1];
        for(int i=0; i<timestamps.size()-1; i++) {
            timeframes[i] = timestamps.get(i).toLocalDate().until(timestamps.get(i+1).toLocalDate(), ChronoUnit.DAYS);
        }*/

        /*float out = 0;
        for(int i=0; i< timeframes.length; i++) {
            out += timeframes[i];
        }*/

        LocalDate earliest = log.get(0).ld;
        for(HobPerf hp: log) {
            if(hp.ld.isBefore(earliest)) earliest = hp.ld;
        }

        long timeframe = (earliest.until(LocalDate.now(), ChronoUnit.DAYS))+1;
        return timeframe < 1 ? -1 : log.size()/(float)timeframe;

    }

    public HoboiLogSave toSave() {
        return new HoboiLogSave(hob.toSave(), log);
    }
}
