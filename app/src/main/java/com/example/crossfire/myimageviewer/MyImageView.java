package com.example.crossfire.myimageviewer;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by CrossFire on 2016/7/11.
 */
public class MyImageView extends ImageView {
    private OnMeasureListener onMeasureListener;

    public void setOnMeasureListener(OnMeasureListener onMeasureListener) {
        this.onMeasureListener = onMeasureListener;
    }

    public MyImageView(Context context) {
        super(context);
    }

    public MyImageView(Context context, AttributeSet attrs) {
        super(context,attrs);
    }

    public MyImageView(Context context, AttributeSet attrs,int defStyle) {
        super(context,attrs,defStyle);
    }

    @Override
    protected void onMeasure(int width,int height){
        super.onMeasure(width,height);

        if(onMeasureListener!=null){
            onMeasureListener.onMeasureSize(getMeasuredWidth(),getMeasuredHeight());
        }
    }

    public interface OnMeasureListener{
        void onMeasureSize(int width,int height);
    }
}
