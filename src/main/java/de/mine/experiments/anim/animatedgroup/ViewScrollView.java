package de.mine.experiments.anim.animatedgroup;

import android.content.Context;
import android.util.AttributeSet;
import android.view.DragEvent;
import android.widget.ScrollView;

import de.mine.experiments.R;

/**
 * Created by skip on 18.02.2015.
 */
public class ViewScrollView extends ScrollView {

    Context context;
    private float OFFSET_WHERE_TO_START_SCROLLING;
    private int touchPositionY;
    private ScrollRunnable scrollRunnable;

    public ViewScrollView(android.content.Context context) {
        super(context);
        init(context);
    }

    public ViewScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public ViewScrollView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    void init(Context context){
        this.context = context;
        this.OFFSET_WHERE_TO_START_SCROLLING = context.getResources().getDimension(R.dimen.scrollview_up_down_offset);
    }

    @Override
    public boolean dispatchDragEvent(DragEvent event) {
        super.dispatchDragEvent(event);

        if(event.getAction() == DragEvent.ACTION_DRAG_STARTED){
            if(this.scrollRunnable != null){
                scrollRunnable.isRunning = false;
            }
            scrollRunnable = new ScrollRunnable();
            new Thread(scrollRunnable).start();

        } else if(event.getAction() == DragEvent.ACTION_DRAG_LOCATION){
            // what to do onmovement event
            onTouchpointMove(event);

        } else if(event.getAction() == DragEvent.ACTION_DRAG_ENDED){
            if(this.scrollRunnable != null){
                scrollRunnable.isRunning = false;
            }

        }

        return true;
    }

    private void onTouchpointMove(DragEvent event){
        float x = event.getX();
        float y = event.getY();

        int height = getHeight();
    }

    private class ScrollRunnable implements Runnable{
        boolean isRunning = true;

        @Override
        public void run() {
//            while(isRunning){
//                try {
//                    Log.d("onTouch", "Scrolling");
//
//                    synchronized (touchPositionY){
//
//                        if(touchPositionY <0 || heightScrollView< touchPositionY){
//                            return;
//                        }
//
//                        // compute the position percentage relative to height of scroll-control
//                        double scrollToPosPercentage, scrollToPosPx;
//                        scrollToPosPercentage = (touchPositionY - SCROLL_CONTROL_UP_DOWN_OFFSET_PX) / (heightScrollView-(2* SCROLL_CONTROL_UP_DOWN_OFFSET_PX));
//                        scrollToPosPx = heightScrollView * scrollToPosPercentage;
//                        scrollView.smoothScrollTo(0, (int)scrollToPosPx);
//
//                    }
//
//                    Thread.sleep(pauseBetweenSrollsMs);
//                }catch (InterruptedException exc){
//                    // nothing
//                }
//            }
        }
    }
}
