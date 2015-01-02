package de.mine.experiments.anim.animatedgroup;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import de.mine.experiments.R;

/**
 * Created by skip on 20.09.2014.
 */
public class ViewItemAnimated extends RelativeLayout implements AbstractFigure, ViewGroup.OnHierarchyChangeListener, IDragInViewIdentifier{

    private Context context;
    private AbstractFigure parent;
    private int heightFixed = Constants.VIEWITEM_FIXED_HEIGHT_PX;
    private AnimatorOfDummy animatorOfDummy;
    private boolean isDraggingOverThis = false;
    private OnDragOutDummyUnregister onDragOutDummyUnregister;

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
    public boolean isDraggingWithinView() {
        return isDraggingOverThis;
    }

    private void init(Context context){
        this.context = context;
        setRandomBg();

        // create a dummy, but do not attach it to a parent yet. Will attach later
        this.animatorOfDummy = new AnimatorOfDummy(context);

        // create a listener which will unregister the dummy on drag out of this view and dummy
        // add it to the dummy then
        this.onDragOutDummyUnregister = new OnDragOutDummyUnregister(this, this.animatorOfDummy, new AnimatorOfDummy[]{this.animatorOfDummy});
        this.animatorOfDummy.addDummyOnDragListener(this.onDragOutDummyUnregister);

        // set layout
        LayoutInflater inflater = LayoutInflater.from(context);
        inflater.inflate(R.layout.activity6_view_item_animated, this);
    }

    @Override
    public AbstractFigure getParentAbstractFigure() {
        return parent;
    }

    @Override
    public void onChildViewAdded(View parent, View child) {
        animatorOfDummy.attachToParentOfDummyBySibling(this);
    }


    @Override
    public void onChildViewRemoved(View parent, View child) {
        // detachFromParent the dummy in old parent
        animatorOfDummy.detachFromParent();
    }

    @Override
    public boolean dispatchDragEvent(DragEvent event) {
        // check, whether we are dragging over the dummy
        Boolean change = Utils.isDraggingOverFromDragEvent(event);
        if(change != null){
            isDraggingOverThis = change;
        }

        // react on drag by manipulating the dummy
        if(event.getAction()==DragEvent.ACTION_DRAG_STARTED) {
            onDragStart(event);

        }else if(event.getAction()==DragEvent.ACTION_DRAG_ENTERED){
            onDragIn();

        }else if(event.getAction()==DragEvent.ACTION_DRAG_EXITED){
            onDragOutDummyUnregister.onDrag(this, event);

        }else if(event.getAction()==DragEvent.ACTION_DRAG_LOCATION){
            // do not dispatch Location events to increase productivity

        }else if(event.getAction()==DragEvent.ACTION_DRAG_ENDED) {
            onDragEnded(event);
        }

        return true;
    }

    private void onDragStart(DragEvent dragEvent){
        if(this.animatorOfDummy != null){
            this.animatorOfDummy.onDragStarted(dragEvent);
        }
    }

    private void onDragEnded(DragEvent dragEvent){
        if(this.animatorOfDummy != null){
            this.animatorOfDummy.onDragEnded(dragEvent);
        }
    }

    protected void onDragIn(){
        // trigger the animation
        int index = Utils.getViewIndexInParent(this);
        animatorOfDummy.onDragInAddDummyAnimation(index+1);
    }

    private void onDragOutRemoveDummyAnimation(){
        if(animatorOfDummy != null){
             animatorOfDummy.onDragOutRemoveDummyAnimation();
        }
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int specHeight = MeasureSpec.makeMeasureSpec(heightFixed, MeasureSpec.EXACTLY);
        super.onMeasure(widthMeasureSpec, specHeight);
        setMeasuredDimension(getMeasuredWidth(), getMeasuredHeight());
    }

    private void setRandomBg(){
        int r = ((int) (Math.random()*255)) ;
        int g = ((int) (Math.random()*255)) ;
        int b = ((int) (Math.random()*255)) ;
        setBackgroundColor(Color.argb(255, r, g, b));
    }

}
