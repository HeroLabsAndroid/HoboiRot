package com.example.hoboirot.statview;

import android.content.Context;
import android.content.res.XmlResourceParser;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.example.hoboirot.Const;
import com.example.hoboirot.R;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Locale;

public class StatView extends View {

    private class CatName {
        String catID;
        int catIdx;

        public CatName(String id, int idx) {
            catID = id;
            catIdx = idx;
        }
    }

    Paint p = new Paint();
    Paint txt = new Paint();
    protected int nr_dset = 1;

    protected StatViewable data;
    protected boolean has_data = false;

    ArrayList<String> hobCats = new ArrayList<>();

    public StatView(Context context, AttributeSet attr) {
        super(context, attr);
    }

    public void attachViewable(StatViewable sv) {
        data = sv;
        nr_dset = data.get_nr_dsets();
        has_data = true;

        mkHobCats();

        invalidate();
    }

    private void mkHobCats() {
        for(int i=0; i<nr_dset; i++) {
            String catID = data.get_cat(i);

            boolean exists = false;
            for(String s: hobCats) {
                if(s.contentEquals(catID)) {
                    exists = true;
                    break;
                }
            }
            if(!exists) hobCats.add(catID);
        }
    }

    @Override
    public void onDraw(@NonNull Canvas canvas) {
        super.onDraw(canvas);
        if(has_data) {
            int h = getHeight()-128;
            int w_offset = 256;
            int w = getWidth()-w_offset;

            p.setColor(ContextCompat.getColor(StatView.this.getContext(), R.color.STVW_bg));
            canvas.drawRect(w_offset,0,w+w_offset,h,p);

            p.setColor(ContextCompat.getColor(StatView.this.getContext(), R.color.STVW_line));
            txt.setColor(ContextCompat.getColor(StatView.this.getContext(), R.color.STVW_txt));
            txt.setTextSize(28);

            float span_in_days = data.get_earliest().until(LocalDate.now(), ChronoUnit.DAYS)+1;
            float width_of_day = w/span_in_days;
            float height_of_dset = nr_dset > 0 ? (h/(float)nr_dset) : 0;

            canvas.drawLine(w_offset, 0, w_offset, h, p);
            for (int i = 0; i < span_in_days; i++) {
                canvas.drawLine(w_offset+((i + 1) * (w / (float) span_in_days)), 0, w_offset+((i + 1) * (w / (float) span_in_days)), h, p);
            }

            //canvas.drawText(String.format(Locale.getDefault(),"%.2f", data.get_maxval()), 16, 20, txt);
            canvas.drawLine(w_offset, 0, w+w_offset, 0, p);
            //canvas.drawText(String.format(Locale.getDefault(),"%.2f", data.get_maxval()/2), 16, (float)(h/2.0), txt);
            canvas.drawLine(w_offset, h, w+w_offset, h, p);


            for(int i=0; i<nr_dset; i++) {
                for(int j=0; j<hobCats.size(); j++) {
                    if(data.get_cat(i).contentEquals(hobCats.get(j))) {
                        p.setColor(Const.STVW_CAT_CLRS[j%Const.STVW_CAT_CLRS.length].toArgb());
                        break;
                    }
                }
                canvas.drawRect(w_offset, (float)i*height_of_dset, w+w_offset, (float)(i+1)*height_of_dset, p);
                p.setColor(ContextCompat.getColor(StatView.this.getContext(), R.color.STVW_line));
                canvas.drawLine(w_offset, (float)i*height_of_dset, w+w_offset, (float)i*height_of_dset, p);
                canvas.drawText(data.get_dset_name(i), 16, i*height_of_dset+height_of_dset, txt);
            }


            for(int i=0; i<span_in_days; i++) {
                for(int j=0; j<nr_dset; j++) {
                    Color barclr = Const.STVW_CLRS[j%Const.STVW_CLRS.length];
                    p.setColor(barclr.toArgb());
                    if(data.done_on_day(data.get_earliest().plusDays(i), j)) {
                        canvas.drawRect(w_offset+i*width_of_day, j*height_of_dset+1, w_offset+(i+1)*width_of_day, (j+1)*height_of_dset-1, p);
                    }
                }
            }

        }
    }
}
