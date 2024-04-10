package com.example.hoboirot.statview;

public interface StatViewable {
    int get_nr_dsets();
    double get_val(int pos);

    String get_label(int pos);

    String get_alt_label(int pos);

    char get_min_label(int pos);
    double get_maxval();

}
