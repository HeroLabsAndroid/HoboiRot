package com.example.hoboirot;

import java.util.ArrayList;
import java.util.Locale;

public class HobCat {

    public ArrayList<HoboiLog> hob;
    public String name;
    public boolean open = true;

    public HobCat(ArrayList<HoboiLog> hob, String name) {
        this.hob = hob;
        this.name = name;
    }

    public String get_hobcat_string() {
        int nr_today=0, nr_week=0, nr_fortnite=0, nr_month=0;
        for (HoboiLog hl: hob) {
            switch (hl.getReccat()) {
                case TODAY:
                    nr_today++;
                    break;
                case LAST_WEEK:
                    nr_week++;
                    break;
                case LAST_FORTNITE:
                    nr_fortnite++;
                    break;
                case MONTH:
                    nr_month++;
                    break;
                default:
                    break;
            }
        }

        String out = String.format(
                Locale.getDefault(),
                "%d hobbs,\r\n " +
                        "%d today, " +
                        "%d this week, "+
                        "%d this fortnite, "+
                        "%d this month",
                hob.size(),
                nr_today,
                nr_week,
                nr_fortnite,
                nr_month

        );

        return out;
    }
}
