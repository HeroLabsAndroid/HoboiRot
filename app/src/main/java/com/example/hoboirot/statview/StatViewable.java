package com.example.hoboirot.statview;

import java.time.LocalDate;
import java.time.LocalDateTime;

public interface StatViewable {
    int get_nr_dsets();
    boolean done_on_day(LocalDate date, int dset);

    LocalDate get_earliest();

    String get_dset_name(int dset);

}
