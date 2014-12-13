package de.mine.experiments.anim.animatedgroup.command;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.util.Log;
import android.view.View;

/**
 * Created by skip on 18.10.2014.
 */
public class CommandGrowView extends AbstractCommand {

    private final View receiverView;
    private final int startHeight;
    private final int finalHeight;
    private final long duration;

    private ValueAnimator valueAnimator;
    private Direction direction = Direction.EXECUTING;

    public CommandGrowView(View receiver, int startHeight, int finalHeight, int duration){
        this.receiverView = receiver;
        this.startHeight = startHeight;
        this.finalHeight = finalHeight;
        this.duration = duration;
    }

    @Override
    public boolean isRunning() {
        if(valueAnimator!= null && valueAnimator.isRunning()){
            return true;
        }
        return false;
    }


    @Override
    public void execute() {
        execute(Direction.EXECUTING);
    }

    @Override
    public void undo() {
        execute(Direction.UNDOING);
    }

    @Override
    public void cancel() {
        if(valueAnimator != null){
            valueAnimator.cancel();
        }
    }

    private void execute(final Direction animationDirection){
        int startValue = receiverView.getHeight();
        Log.d("receiverView","receiverView.getHeight(): "+startValue);

        int finalValue = finalHeight;
        long currentDuration = duration;

        // remember new direction
        this.direction = animationDirection;

        // swap start and end value if animating backward
        if(animationDirection == Direction.UNDOING){
            finalValue = 0;
        }

        // modify startHeight if in progress
        if(valueAnimator != null && valueAnimator.isRunning()){
            // cancel the animation
            valueAnimator.cancel();

            // TODO - recalculate duration. Animation time should be shorter when animating only the half distance, when breaking up prev animation
            // start the animation with another duration
//            currentDuration = computeRestNavigationDuration(startValue, finalValue);
        }


        // now animate
        Log.d("draggin",String.format("From %s px to %s px", startValue, finalValue));
        ValueAnimator va = getAnimator(startValue, finalValue, currentDuration);

        // listen for animation end
        va.addListener(new Animator.AnimatorListener() {

            @Override
            public void onAnimationStart(Animator animation) {
                if(animationDirection == Direction.EXECUTING){
                    notifyOnExecutionStartsListener();
                }else{
                    notifyOnUndoStartsListener();
                }
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if(animationDirection == Direction.EXECUTING){
                    notifyOnExecutionSuccessfullyFinishsListener();
                }else{
                    notifyOnUndoFinishsListener();
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                notifyOnExecutionCanceledListener();
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
                // nothing
            }
        });

        // start the animationNow
        va.start();
    }

    // explicitely tell the direction
    private ValueAnimator getAnimator(final int startHeight, final int finalHeight, final long durationOfAnimation) {

        if(valueAnimator != null && valueAnimator.isRunning()){
            valueAnimator.cancel();
        }

        // animate dummy height from 0 to childHeight
        valueAnimator = ValueAnimator.ofInt(startHeight, finalHeight);
        valueAnimator.setDuration(durationOfAnimation);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator animation) {
                Integer value = (Integer) animation.getAnimatedValue();
                receiverView.getLayoutParams().height = value.intValue();
                receiverView.requestLayout();
            }
        });

        return valueAnimator;
    }


    // HELPER


    private long computeRestNavigationDuration(int startValue, int endValue){
        int currentDelta = Math.abs(startValue - endValue);
        int initialDelta = Math.abs(startHeight - finalHeight);

        // max value is the original time
        double percentage = currentDelta / (initialDelta);

        // the duration is just a part of the original duration
        long resultingAnimationDuration = Math.round(duration * percentage);

        // interval
        resultingAnimationDuration = Math.min(resultingAnimationDuration, duration);
        resultingAnimationDuration = Math.max(resultingAnimationDuration, 0);

        return resultingAnimationDuration;
    }

    // CLASSES

    enum Direction {
        EXECUTING, UNDOING;

        public Direction toggle(){
            if(this == EXECUTING){
                return UNDOING;
            }
            return EXECUTING;
        };
    }
}
