package com.example.hoboirot.LtdRecView;

import android.content.Context;
import android.util.AttributeSet;
import android.util.DisplayMetrics;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hoboirot.Util;

public class LtdRecyclerView extends RecyclerView {

    private int maxheight = 360;


    public int getMaxheight() {
        return maxheight;
    }

    public void setMaxheight(int maxheight) {
        this.maxheight = maxheight;
    }

    private static float dpsToPx(float dps, Context c) {
        return dps * ((float) c.getResources().getDisplayMetrics().densityDpi / DisplayMetrics.DENSITY_DEFAULT);
    }

    private static float pxToDps(float px, Context c) {
        return px / ((float) c.getResources().getDisplayMetrics().densityDpi / DisplayMetrics.DENSITY_DEFAULT);
    }



    public LtdRecyclerView(@NonNull Context context) {
        super(context);
    }

    public LtdRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public LtdRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthSpec, int heightSpec) {
        heightSpec = MeasureSpec.makeMeasureSpec((int) dpsToPx((float)maxheight, getContext()), MeasureSpec.AT_MOST);
        super.onMeasure(widthSpec, heightSpec);
    }
}
