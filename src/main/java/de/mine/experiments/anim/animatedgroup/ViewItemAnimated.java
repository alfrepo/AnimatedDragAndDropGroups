package de.mine.experiments.anim.animatedgroup;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import de.mine.experiments.R;

/**
 * Created by skip on 20.09.2014.
 */
public class ViewItemAnimated extends RelativeLayout  implements AbstractFigure{

    private AbstractFigure parent;
    private int heightFixed = 100;

    public ViewItemAnimated(Context context) {
        super(context);
        init(context);
    }

    public ViewItemAnimated(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public ViewItemAnimated(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @Override
    public AbstractFigure getParentAbstractFigure() {
        return parent;
    }

    void init(Context context){
        setRandomBg();
        // set layout
        LayoutInflater inflater = LayoutInflater.from(context);
        inflater.inflate(R.layout.activity6_view_item_animated, this);
    }

    @Override
    protected void attachViewToParent(View child, int index, ViewGroup.LayoutParams params) {

    }

    private void setRandomBg(){
        int r = ((int) (Math.random()*255)) ;
        int g = ((int) (Math.random()*255)) ;
        int b = ((int) (Math.random()*255)) ;
        setBackgroundColor(Color.argb(255, r, g, b));
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int specHeight = MeasureSpec.makeMeasureSpec(heightFixed, MeasureSpec.EXACTLY);
        super.onMeasure(widthMeasureSpec, specHeight);
        setMeasuredDimension(getMeasuredWidth(), getMeasuredHeight());
    }
}
