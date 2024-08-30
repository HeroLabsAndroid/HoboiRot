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
import java.util.Locale;

public class StatView extends View {



    Paint p = new Paint();
    Paint txt = new Paint();
    protected int nr_dset = 1;

    protected StatViewable data;
    protected boolean has_data = false;



    public StatView(Context context, AttributeSet attr) {
        super(context, attr);
    }

    public void attachViewable(StatViewable sv) {
        data = sv;
        nr_dset = data.get_nr_dsets();
        has_data = true;

        invalidate();
    }


    @Override
    public void onDraw(@NonNull Canvas canvas) {
        super.onDraw(canvas);
        if(has_data) {
            int h = getHeight()-128;
            int w_offset = 128;
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
                canvas.drawLine(w_offset, (float)i*height_of_dset, w+w_offset, (float)i*height_of_dset, p);
                canvas.drawText(data.get_dset_name(i), 4, i*height_of_dset+height_of_dset/2, txt);
            }


            for(int i=0; i<span_in_days; i++) {
                for(int j=0; j<nr_dset; j++) {
                    Color barclr = Const.STVW_CLRS[j%Const.STVW_CLRS.length];
                    p.setColor(barclr.toArgb());
                    if(data.done_on_day(data.get_earliest().plusDays(i), j)) {
                        canvas.drawRect(w_offset+i*width_of_day, j*height_of_dset, w_offset+(i+1)*width_of_day, (j+1)*height_of_dset, p);
                    }
                }
            }

            /*double max = data.get_maxval();
            for (int i = 0; i < nr_dset; i++) {
                String label = data.get_label(i);
                double val = data.get_val(i);

                canvas.drawRect(w_offset+((i) * (w / (float) nr_dset)+4), (float) ((max-val) / max)*h, w_offset+((i + 1) * (w / (float) nr_dset)-4), h, p);
                if(show_label) canvas.drawText(label, w_offset+((i) * (w / (float) nr_dset)+8), h+32, txt);
                else if(show_min_label) canvas.drawText(String.valueOf(data.get_min_label(i)), w_offset+((i) * (w / (float) nr_dset)+8), h+32, txt);
                if(show_alt_label) canvas.drawText(data.get_alt_label(i), w_offset+((i) * (w / (float) nr_dset)+8), h+64, txt);
            }*/
        }
    }
}
