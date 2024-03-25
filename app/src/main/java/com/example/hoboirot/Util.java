package com.example.hoboirot;

import androidx.annotation.NonNull;

import java.time.LocalDateTime;
import java.util.Locale;

public class Util {

    public enum RecCat {

        LAST_WEEK, LAST_FORTNITE, LONGER, NEVER;


        @NonNull
        @Override
        public String toString() {
            switch (this) {
                case LAST_WEEK:
                    return "Last Week";
                case LAST_FORTNITE:
                    return "Last Fortnight";
                case LONGER:
                    return "Longer";
                default:
                    return "Never";
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
        if(ldt.isAfter(LocalDateTime.now().minusDays(7))) return RecCat.LAST_WEEK;
        else if(ldt.isAfter(LocalDateTime.now().minusDays(14))) return RecCat.LAST_FORTNITE;
        else return RecCat.LONGER;
    }
    public static String DateTimeToString(LocalDateTime ldt) {
        return String.format(Locale.getDefault(),"%2d.%2d.%4d", ldt.getDayOfMonth(), ldt.getMonth().getValue(), ldt.getYear());
    }
}
