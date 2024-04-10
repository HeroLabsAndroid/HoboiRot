package com.example.hoboirot;

import android.content.Context;
import android.util.DisplayMetrics;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Locale;

public class Util {

    public enum RecCat {

        LAST_WEEK, LAST_FORTNITE, LONGER, NEVER;


        @NonNull
        @Override
        public String toString() {
            switch (this) {
                case LAST_WEEK:
                    return "This Week";
                case LAST_FORTNITE:
                    return "This Fortnight";
                case LONGER:
                    return "Been a while";
                default:
                    return "Never";
            }
        }

        public int toColor(Context c) {
            switch(this) {
                case LAST_WEEK:
                    return ContextCompat.getColor(c, R.color.RECCAT_1);
                case LAST_FORTNITE:
                    return ContextCompat.getColor(c, R.color.RECCAT_2);
                case LONGER:
                    return ContextCompat.getColor(c, R.color.RECCAT_3);
                default:
                    return ContextCompat.getColor(c, R.color.RECCAT_4);
            }
        }
    }

    public static RecCat CatFromOrdinal(int ord) {
        switch(ord) {
            case 0:
                return RecCat.LAST_WEEK;
            case 1:
                return RecCat.LAST_FORTNITE;
            case 2:
                return RecCat.LONGER;
            default:
                return RecCat.NEVER;
        }
    }

    public static RecCat getRecency(LocalDateTime ldt) {
        if(ldt.isAfter(LocalDateTime.now().minusWeeks(1))) return RecCat.LAST_WEEK;
        else if(ldt.isAfter(LocalDateTime.now().minusDays(14))) return RecCat.LAST_FORTNITE;
        else return RecCat.LONGER;
    }
    public static String DateTimeToString(LocalDateTime ldt) {
        return String.format(Locale.getDefault(),"%2d.%2d.%4d", ldt.getDayOfMonth(), ldt.getMonth().getValue(), ldt.getYear());
    }

    public static long days_since(LocalDateTime ldt) {
        return ldt.toLocalDate().until(LocalDate.now(), ChronoUnit.DAYS);
    }

    public static ArrayList<HoboiLog> sort_hobois_by_name(ArrayList<HoboiLog> hl) {
        ArrayList<HoboiLog> tmp = new ArrayList<>();

        int len = hl.size();
        int idx = 0;
        while(idx < len) {
            int minidx = 0;

            for(int i=1; i<hl.size(); i++) {
                if(hl.get(minidx).getHob().getName().compareToIgnoreCase(hl.get(i).getHob().getName())>0) {
                    minidx =i;
                }
            }

            tmp.add(hl.get(minidx));
            hl.remove(minidx);
            idx++;
        }

        return tmp;
    }

    public static LocalDateTime end_of_day(LocalDateTime ldt) {
        return ldt.withHour(23).withMinute(59).withSecond(59);
    }

    public static LocalDateTime start_of_day(LocalDateTime ldt) {
        return ldt.withHour(23).withMinute(59).withSecond(59);
    }

    public static boolean same_day(LocalDateTime l1, LocalDateTime l2) {
        return (l1.getDayOfYear() == l2.getDayOfYear() && l1.getYear() == l2.getYear());
    }

    public static float dpsToPx(float dps, Context c) {
        return dps * ((float) c.getResources().getDisplayMetrics().densityDpi / DisplayMetrics.DENSITY_DEFAULT);
    }

    public static float pxToDps(float px, Context c) {
        return px / ((float) c.getResources().getDisplayMetrics().densityDpi / DisplayMetrics.DENSITY_DEFAULT);
    }
}
