package de.mine.experiments.anim.animatedgroup;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.DragEvent;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by skip on 10.09.2014.
 */
public class ViewDummyAnimated extends View implements AbstractFigure, ViewGroup.OnHierarchyChangeListener {

    private int defaultHeight = 100;
    private AbstractFigure parent;

    private ViewDummyAnimated follower;

    public ViewDummyAnimated(Context context) {
        super(context);
        init();
    }

    public ViewDummyAnimated(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ViewDummyAnimated(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init(){
        // TODO me de random BG
        setRandomBg();

        // listen for drop
        super.setOnDragListener(new OnDragListener() {
            @Override
            public boolean onDrag(View v, DragEvent event) {
                UtilDrag.registerAsHoveringView(v, ViewDummyAnimated.this, event);
                return true;
            }
        });
    }

    @Override
    public void onChildViewAdded(View parent, View child) {
        // dummy does nothing when it is added
    }

    @Override
    public void onChildViewRemoved(View parent, View child) {
        // dummy does nothing when it is removed
    }

    @Override
    public void setOnDragListener(OnDragListener l) {
        // do not allow do override my listeners
    }

    @Override
    public boolean dispatchDragEvent(DragEvent event) {
        return super.dispatchDragEvent(event);
    }

    @Override
    public AbstractFigure getParentAbstractFigure() {
        return parent;
    }

    public void setDefaultHeight(int height) {
        this.defaultHeight = height;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int height = defaultHeight;
        ViewGroup.LayoutParams lp = getLayoutParams();
        if(lp != null && lp.height>=0){
            height = lp.height;
        }
        int mHeightMeasureSpec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY);
        super.onMeasure(widthMeasureSpec, mHeightMeasureSpec);
        setMeasuredDimension(getMeasuredWidth(), getMeasuredHeight());
    }

    private void setRandomBg(){
        int r = ((int) (Math.random()*255)) ;
        int g = ((int) (Math.random()*255)) ;
        int b = ((int) (Math.random()*255)) ;
        setBackgroundColor(Color.argb(255, r, g, b));
    }
}
