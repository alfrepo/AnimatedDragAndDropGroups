package de.mine.experiments.anim.animatedgroup;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.view.DragEvent;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by skip on 10.09.2014.
 */
public class ViewDummyAnimated extends View implements AbstractFigure, ViewGroup.OnHierarchyChangeListener, IDragInViewIdentifier {

    private int defaultHeight = 100;
    private AbstractFigure parent;

    private ViewDummyAnimated follower;
    public boolean isDraggingOverThis = false;

    private OnDragListener onDragListener;

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

    @Override
    public boolean isDraggingWithinView() {
        return isDraggingOverThis;
    }


    private void init(){
        // TODO me de random BG
        setRandomBg();
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
    public boolean dispatchDragEvent(DragEvent event) {

        /*  check, whether we are dragging over the dummy
            use the dispatchDragEvent method to capture events internally - they arrive here life.
            The listener may be overridden from outside - there may be only one
         */
        Boolean change = Utils.isDraggingOverFromDragEvent(event);
        if(change != null && isDraggingOverThis!=change){
            isDraggingOverThis = change;
            Log.d("isDraggingOverThis", "Dummy: isDraggingOverThis: "+isDraggingOverThis);
        }
        super.dispatchDragEvent(event);

        /** Totally important! View which wish to receive drag events
         * (start, stop, location) should return true when the receive a
         * DragEvent.ACTION_DRAG_STARTED */
        return true;
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
