package de.mine.experiments.anim.animatedgroup;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.view.DragEvent;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import de.mine.experiments.anim.animatedgroup.command.CommandGrowView;

/**
 * Created by skip on 10.09.2014.
 */
public class ViewGroupAnimated extends ViewGroup implements AbstractViewGroup {

    private int minHeight = 100;
    private int minWidth = 100;

    private int mPadding_left = 50;
    private int mPadding_top = 50;

    private List<AbstractFigure> children = new ArrayList<AbstractFigure>();

    private AbstractFigure parent;

    // DUMMIES

    public static final int INITIAL_DUMMY_HEIGHT_BEFORE_EXPANDING = 0;

    public static final int dummyHeight =  250; // fixed height of dummies before drop
    public static final int dummyAnimationDuration =  500; // ms

    private ViewDummyAnimated placeholderFollower = null; // dummy which is appended to the parent which is added to the view on drag
    CommandGrowView placeholderFollowerCommandGrowView; // command which is used to animate the dummy

    private ViewDummyAnimated placeholderInsider = null;
    CommandGrowView placeholderInsiderCommandGrowView; // command which is used to animate the dummy

    private Context context;


    // REPLACEMENT
    AnimatorOfDummy animatorOfDummyInsider;
    AnimatorOfDummy animatorOfDummyFollower;


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

        // dummy insider
        this.animatorOfDummyInsider = new AnimatorOfDummy(context, this);

        setMinimumHeight(minHeight);
        setMinimumWidth(minWidth);

        // padding
        setPadding(mPadding_left, mPadding_top, 0, 0);

        setRandomBg();

        // listeners will be triggered on drag
        setUpDragListeners();


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
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        if(getChildCount() == 1){
            Log.d("yes","ooooo - ooooo");
        }

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
        // TODO use get getChildMeasureSpec()
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


    private View viewDummyAnimatedOnDrag = null;

    /*  This enables dummy animation on drag in.
        Drop is NOT handled by the Group - it is handled by the dummy itself.     */
    private void setUpDragListeners(){
        this.setOnDragListener(new OnDragListener() {
            @Override
            public boolean onDrag(View v, DragEvent event) {
                if(event.getAction() == DragEvent.ACTION_DRAG_ENTERED){
                    Log.d("draggin", "Drag in");
                    onDragInAddDummyAnimation();
                }else if(event.getAction() == DragEvent.ACTION_DRAG_EXITED){
                    Log.d("draggin", "Drag out");
                    onDragOutRemoveDummyAnimation();
                }
                return true;
            }
        });
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
        initializeFollowerDummyOnNewDummy();
        this.animatorOfDummyInsider.onDragInAddDummyAnimation(0);

        int index = getViewIndexInParent((ViewGroup)getParent());
        this.animatorOfDummyFollower.onDragInAddDummyAnimation(index+1);
    }

    /**
     * Since there is no way to check, when the current view is attached to a new parent
     * we will check for a new parent on every drag in.
     * This method will reinitialize
     */
    private void initializeFollowerDummyOnNewDummy(){
        if(getParent() != null){

            // dummy does not exist yet
            if(animatorOfDummyFollower == null){
                animatorOfDummyFollower = new AnimatorOfDummy(context, (ViewGroup)getParent());
            }

            // dummy not empty but has wrong parent
            if(animatorOfDummyFollower != null && (animatorOfDummyFollower.getParentOfDummy() != getParent())){
                animatorOfDummyFollower.destroy();
            }
        }
    }

    // start resizing shrinking the view slowly
    private void onDragOutRemoveDummyAnimation(){
        this.animatorOfDummyInsider.onDragOutRemoveDummyAnimation();
        this.animatorOfDummyFollower.onDragOutRemoveDummyAnimation();
    }

    private int getViewIndexInParent(ViewGroup parent) {
        for(int i=0; i<parent.getChildCount(); i++){
            if(parent.getChildAt(i).equals(ViewGroupAnimated.this)){
                return i;
            }
        }
        throw new IllegalStateException("Failed to retrieve the index of the view");
    }


    // should be inside the dummy
    private void onDropInReplaceDummyAndStopCommand(){
        viewDummyAnimatedOnDrag.setOnDragListener(new OnDragListener() {
            @Override
            public boolean onDrag(View v, DragEvent event) {
                if(event.getAction()==DragEvent.ACTION_DROP){
                    // TODO alf
                    // replace the dummy with the view!
                    return true;
                }
                return false;
            }
        });
    }



    public void addViewAnimated(final View child) {
        // measure the child as x height.

        // retrieve the measure specs width / height . Use matchparent / unspecified
            // provide infos how the this view as parent wants to see the child
            int measureSpecWidth = MeasureSpec.makeMeasureSpec(getMeasuredWidth(), MeasureSpec.AT_MOST);
            int measureSpecHeight = MeasureSpec.makeMeasureSpec(0 ,MeasureSpec.EXACTLY);
            // provide infos how the child wants to lay out itselfe (normally via xml)
            LayoutParams lpChild = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
            child.setLayoutParams(lpChild);
            // measure the child
            child.measure(measureSpecWidth, measureSpecHeight);
            final int childWidth = child.getMeasuredWidth();
            final int childHeight = child.getMeasuredHeight();

        // add dummy with height 0
            final ViewDummyAnimated itemDummy = new ViewDummyAnimated(context);
            // initial lp to avoid nullPointerException
            // assign the height of 0 to the dummy. WIll expand it soon
            LayoutParams lp = new LayoutParams(childWidth, 0);
            itemDummy.setLayoutParams(lp);
            addView(itemDummy);

        // animate dummy height from 0 to childHeight
            ValueAnimator va = ValueAnimator.ofInt(0, childHeight);
            va.setDuration(200);
            va.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                public void onAnimationUpdate(ValueAnimator animation) {
                    Integer value = (Integer) animation.getAnimatedValue();
                    itemDummy.getLayoutParams().height = value.intValue();
                    itemDummy.requestLayout();
                }
            });

            va.addListener(new AnimatorListenerAdapter() {
                           @Override
                           public void onAnimationEnd(Animator animation) {
                               // replace dummy by child. use attachLayoutAnimationParameters, detachViewFromParent
                               LayoutParams layoutParamsChild = child.getLayoutParams();
                               int index = indexOfChild(itemDummy);
                               detachViewFromParent(itemDummy);
                               attachViewToParent(child,index, layoutParamsChild);
                               child.requestLayout();
                           }
            });
            va.start();
    }


    // INTERFACES

    @Override
    public List<AbstractFigure> getChildren() {
        // TODO alf
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

    // MOVE THIS
}
