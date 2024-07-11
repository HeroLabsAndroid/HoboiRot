package com.example.hoboirot;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * Data class holding info about a Hoboi Performance
 */
public class HobPerf implements Serializable, Comparable<HobPerf> {
    public LocalDate ld;
    public boolean suppl;

    public HobPerf(LocalDate ld, boolean suppl) {
        this.ld = ld;
        this.suppl = suppl;
    }

    @Override
    public int compareTo(HobPerf o) {
        return ld.compareTo(o.ld);
    }
}
