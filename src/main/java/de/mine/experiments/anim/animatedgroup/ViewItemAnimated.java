package de.mine.experiments.anim.animatedgroup;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import de.mine.experiments.R;

/**
 * Created by skip on 20.09.2014.
 */
public class ViewItemAnimated extends RelativeLayout implements AbstractFigure, ViewGroup.OnHierarchyChangeListener{

    private Context context;
    private AbstractFigure parent;
    private int heightFixed = 100;
    private AnimatorOfDummy animatorOfDummy;


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


    private void init(Context context){
        this.context = context;
        setRandomBg();
        // set layout
        LayoutInflater inflater = LayoutInflater.from(context);
        inflater.inflate(R.layout.activity6_view_item_animated, this);

        // listen for dragging
        this.setOnDragListener(new OnDragListener() {
            @Override
            public boolean onDrag(View v, DragEvent event) {
                // delegate to UtilDrag
                UtilDrag.registerAsHoveringView(v, ViewItemAnimated.this, event);

                // react on drag by manipulating the dummy
                if(event.getAction()==DragEvent.ACTION_DRAG_ENTERED){
                    onDragIn();
                }else if(event.getAction()==DragEvent.ACTION_DRAG_EXITED){
                    onDragOut();
                }else if(event.getAction()==DragEvent.ACTION_DRAG_LOCATION){
                    return false; // do not dispatch Location events to increase productivity
                }
                return true;
            }
        });
    }

    @Override
    public AbstractFigure getParentAbstractFigure() {
        return parent;
    }

    @Override
    public void onChildViewAdded(View parent, View child) {
        // TODO me remove dummy
        Log.d("myDrag", "onChildViewAdded");
    }

    @Override
    public void onChildViewRemoved(View parent, View child) {
        // TODO me remove dummy
    }



    protected void onDragIn(){
        // check if the parent is the same
        if(getParent() != null && this.animatorOfDummy!=null){
            if(this.animatorOfDummy.getParentOfDummy() != getParent()){
                // delete old dummy
                this.animatorOfDummy.destroy();
                this.animatorOfDummy = null;
            }
        }

        // if initialisation is needed - init the dummy
        if(this.animatorOfDummy==null){
            this.animatorOfDummy = new AnimatorOfDummy(context, (ViewGroup) this.getParent());
        }

        // trigger the animation
        int index = Utils.getViewIndexInParent(this);
        animatorOfDummy.onDragInAddDummyAnimation(index+1);
    }

    // TODO
    // list all the waiting, not triggered Requiest, in each View with dummies.
    // clear the liston enter
    protected void onDragOut(){
            View containerHovering = UtilDrag.getHoveringOverContainerView();
            if(animatorOfDummy != null && containerHovering!=ViewItemAnimated.this && containerHovering!=animatorOfDummy.getViewDummyAnimated()){
                 animatorOfDummy.onDragOutRemoveDummyAnimation();
            }

        // install a thread, wait x ms and check whether we are within the child / parent
//        new Thread(new Runnable(){
//            @Override
//            public void run() {
//                try {
//                    Thread.sleep(1000);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//                // undo the animation
//                View containerHovering = UtilDrag.getHoveringOverContainerView();
//                Log.d("draggin", "--- Decide whether to Navigate out ---");
//                Log.d("draggin", "containerHovering:" + containerHovering);
//                Log.d("draggin","dummy: "+animatorOfDummy.getViewDummyAnimated());
//                Log.d("draggin","viewgroup: "+ViewItemAnimated.this);
//                Log.d("draggin", "------------");
//
//                if(animatorOfDummy != null && containerHovering!=ViewItemAnimated.this && containerHovering!=animatorOfDummy.getViewDummyAnimated()){
//
////                    post(new Runnable() {
////                        @Override
////                        public void run() {
////                            animatorOfDummy.onDragOutRemoveDummyAnimation();
////                        }
////                    });
//
//                }
//            }
//        }).start();
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
