package com.example.hoboirot;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class HoboiLogSave implements Serializable {
   HoboiSave hobsv;
   ArrayList<LocalDateTime> log;

    public HoboiLogSave(HoboiSave hobsv, ArrayList<LocalDateTime> log) {
        this.hobsv = hobsv;
        this.log = log;
    }
}
