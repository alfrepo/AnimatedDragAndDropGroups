package de.mine.experiments.anim.animatedgroup;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.view.DragEvent;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by skip on 10.09.2014.
 */
public class ViewGroupAnimated extends ViewGroup implements AbstractViewGroup, ViewGroup.OnHierarchyChangeListener, IDragInViewIdentifier {

    private List<AbstractFigure> children = new ArrayList<AbstractFigure>();
    private AbstractFigure parent;
    private Context context;
    private AnimatorOfDummy animatorOfDummyInsider;
    private AnimatorOfDummy animatorOfDummyFollower;

    // is triggered when the drag leaves the view or dummy to hide the dummy
    private OnDragOutDummyUnregister onDragOutDummyUnregister;

    private int minHeight = Constants.VIEWGROUP_MIN_HEIGHT_PX;
    private int minWidth = 100;
    private int mPadding_left = Constants.VIEWGROUP_PADDING_LEFT_PX;
    private int mPadding_top = Constants.VIEWGROUP_PADDING_TOP_PX;
    private int mPadding_bottom = Constants.VIEWGROUP_PADDING_BOTTOM_PX;

    private boolean isDraggingOverThis = false;



    public ViewGroupAnimated(Context context) {
        super(context);
        init(context);
    }

    public ViewGroupAnimated(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public ViewGroupAnimated(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }


    private void init(Context context){
        this.context = context;

        // pass the information about additon of new Children to the children themselves if they implement OnHierarchyChangeListener
        this.setOnHierarchyChangeListener(getOnHierarchyChangeListener());

        // dummy insider - the parent is this
        this.animatorOfDummyInsider = new AnimatorOfDummy(context, this);

        // dummy follower has the same parent as the view. Parent is attached later when the view is attached
        this.animatorOfDummyFollower = new AnimatorOfDummy(context);

        // create a listener which would hide, when drag leaves this view and dummy and add it to the follower
        this.onDragOutDummyUnregister = new OnDragOutDummyUnregister(this, this.animatorOfDummyFollower, new AnimatorOfDummy[]{this.animatorOfDummyInsider, this.animatorOfDummyFollower});
        this.animatorOfDummyFollower.addDummyOnDragListener(this.onDragOutDummyUnregister);

        setMinimumHeight(minHeight);
        setMinimumWidth(minWidth);

        // padding
        setPadding(mPadding_left, mPadding_top, 0, mPadding_bottom);

        setRandomBg();
    }


    private OnHierarchyChangeListener getOnHierarchyChangeListener(){
        return new OnHierarchyChangeListener() {
            @Override
            public void onChildViewAdded(View parent, View child) {
                // notify child if, that it was added to a new parent. Let them REMOVE Dummies
                ((OnHierarchyChangeListener)child).onChildViewAdded(parent, child);
            }

            @Override
            public void onChildViewRemoved(View parent, View child) {
                // notify  child if, that it was added to a new parent. Let them REMOVE Dummies
                ((OnHierarchyChangeListener)child).onChildViewRemoved(parent, child);
            }
        };
    }

    @Override
    public boolean isDraggingWithinView() {
        return isDraggingOverThis;
    }

    private void setRandomBg(){
        int r = ((int) (Math.random()*255)) ;
        int g = ((int) (Math.random()*255)) ;
        int b = ((int) (Math.random()*255)) ;
        setBackgroundColor(Color.argb(255, r, g, b));
    }

    @Override
    public AbstractFigure getParentAbstractFigure() {
        return parent;
    }


    @Override
    public boolean dispatchDragEvent(DragEvent event) {
        // check, whether we are dragging over the dummy
        Boolean change = Utils.isDraggingOverFromDragEvent(event);
        if(change != null) {
            isDraggingOverThis = change;
        }

        // react on drag by manipulating the dummy
        if(event.getAction() == DragEvent.ACTION_DRAG_STARTED ){
            animatorOfDummyFollower.onDragStarted(event);

        }else if(event.getAction() == DragEvent.ACTION_DRAG_ENTERED){
            Log.d("draggin", "Drag in");
            onDragInAddDummyAnimation();

        }else if(event.getAction() == DragEvent.ACTION_DRAG_EXITED){
            Log.d("draggin", "Drag out");
            onDragOutDummyUnregister.onDrag(this, event);

        }else if(event.getAction()==DragEvent.ACTION_DRAG_LOCATION){
            // do not dispatch Location events to increase productivity

        }else if(event.getAction()==DragEvent.ACTION_DROP){
            // do not dispatch Location events to increase productivity

        } else if(event.getAction() == DragEvent.ACTION_DRAG_ENDED ){
            animatorOfDummyFollower.onDragEnded(event);
        }

        /** It is important to call the super.dispatch(). Or the children will never be able to receive drag.         */
        super.dispatchDragEvent(event);

        /** It is important to return "true" here. Or this view will not be marked as one, which wishes to receive drag.*/
        return true;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int resultWidth = getMeasuredWidth();
        int resultHeight = getMeasuredHeight();

        Log.d("yes","---");
        Log.d("yes","measuredWidth: "+getMeasuredWidth());
        Log.d("yes","measuredHeight: "+getMeasuredHeight());

        // min height or parent's constraints
        int minWidth = getSuggestedMinimumWidth();
        int minHeight = getSuggestedMinimumHeight();
        int defaultWidth = getDefaultSize(minWidth, widthMeasureSpec);
        int defaultHeight = getDefaultSize(minHeight, heightMeasureSpec);
        Log.d("yes","minWidth: "+minWidth);
        Log.d("yes","minHeight: "+minHeight);
        Log.d("yes","defaultWidth: "+defaultWidth);
        Log.d("yes","defaultHeight: "+defaultHeight);

        // data passed to us from parent
        int specHeight = View.MeasureSpec.getSize( heightMeasureSpec );
        int specHeightMode = View.MeasureSpec.getMode( heightMeasureSpec );
        int specWidth = View.MeasureSpec.getSize( widthMeasureSpec );
        int specWidthMode = View.MeasureSpec.getMode( widthMeasureSpec );

        Log.d("yes","specHeight: "+specHeight);
        Log.d("yes","specHeightMode: "+specHeightMode);
        Log.d("yes","specWidth: "+specWidth);
        Log.d("yes","specWidthMode: "+specWidthMode);


//        MeasureSpec.AT_MOST; // -2147483648
//        MeasureSpec.UNSPECIFIED; // 0
//        MeasureSpec.EXACTLY; // 1073741824

        // measure the children. Have to explicitely call that! Otherwise the children will not measure themselves and will have width and height of 0
        /*
         * - iterates all VISIBLE children
         * - measures them with the given measureSpec (what the parent told us to be!) There will be problems if parent tells to be max. x and all children will try to fill the child
         *  - calls measureChild() which considers padding of the current view
         */
        measureChildren(widthMeasureSpec, heightMeasureSpec);


        int childHeightSum = 0;
        int childWidthMax = 0;
        for(int i=0; i<getChildCount(); i++){
            View child = getChildAt(i);
            childHeightSum += child.getMeasuredHeight();
            childWidthMax = Math.max(childWidthMax, child.getMeasuredWidth());
        }
        // don't forget the padding of this view which was already considered for measuring the child,
        // to make the child smaller, if the space inside this view is limited
        childHeightSum += getPaddingTop() + getPaddingBottom();

        Log.d("yes","child Count: "+getChildCount());
        Log.d("yes","childHeightSum: "+childHeightSum);
        Log.d("yes","childWidthMax: "+childWidthMax);

        // respect the parent's MeasureSpec constraints
        switch (specHeightMode){
            case MeasureSpec.EXACTLY:
                resultHeight = specHeight;
                break;

            case MeasureSpec.UNSPECIFIED:
                resultHeight = childHeightSum;
                break;

            case MeasureSpec.AT_MOST:
                // respect MAX constraints (received from parent)
                resultHeight = Math.min(childHeightSum, specHeight);
                break;
        }

        // respect the MIN constraints (set on view). Asume that minsize is set so that it does not injure parents constrains
        resultHeight = Math.max(minHeight, resultHeight);

        // store the size
        setMeasuredDimension(resultWidth, resultHeight);
    }


    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int left = mPadding_left;
        int top=mPadding_top;
        for(int i=0; i<getChildCount(); i++){
            View child = getChildAt(i);
            int width = child.getMeasuredWidth();
            int height = child.getMeasuredHeight();
            child.layout(left, top, left + width, top + height);
            // increase counter
            top += child.getMeasuredHeight();
        }
    }



    /**
     * MOVE!
     * Animates the dummies, when the dragShadow is moved into the group.
     * Creates the dummy if necessary,
     * adds the dummy to the parent at position x,
     * removes the view, when animation ends,
     *
     */
    private void onDragInAddDummyAnimation(){
        this.animatorOfDummyInsider.onDragInAddDummyAnimation(0);

        int index = Utils.getViewIndexInParent(this);
        this.animatorOfDummyFollower.onDragInAddDummyAnimation(index + 1);
    }

    @Override
    public void onChildViewAdded(View parent, View child) {
        // this view was added to a new parent - recreate it
        this.animatorOfDummyFollower.attachToParentOfDummyBySibling(this);
    }

    @Override
    public void onChildViewRemoved(View parent, View child) {
        // detachFromParent the dummy in old parent
        animatorOfDummyFollower.detachFromParent();
    }


    // should be inside the dummy
    private void onDropInReplaceDummyAndStopCommand(){

    }

    @Override
    public void addView(View child) {
        // check whether the view implements the OnHierarchyChangeListener
        if(!(child instanceof  OnHierarchyChangeListener)){
            throw new IllegalArgumentException(String.format("The child %s has to implemement OnHierarchyChangeListener", child.getClass().getSimpleName()));
        }
        // enforce height of the group to have the value WRAP_CONTENT
        // otherwise the ViewGroup will not react on (animated) changes of child heights
        if(getLayoutParams() != null){
            getLayoutParams().height = LayoutParams.WRAP_CONTENT;
        }

        super.addView(child);
    }



    // INTERFACES

    @Override
    public List<AbstractFigure> getChildren() {
        return null;
    }

    @Override
    public void addChild(AbstractFigure child) {
        // TODO
    }

    @Override
    public void addSuccessor(AbstractFigure figure) {
        // TODO
    }

    @Override
    public void replace(AbstractFigure child) {
        // TODO
    }


}
