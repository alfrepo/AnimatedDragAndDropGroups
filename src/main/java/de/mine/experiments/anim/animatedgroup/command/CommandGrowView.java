package de.mine.experiments.anim.animatedgroup.command;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.util.Log;
import android.view.View;

import de.mine.experiments.anim.animatedgroup.Constants;

/**
 * Created by skip on 18.10.2014.
 */
public class CommandGrowView extends AbstractCommand {

    private final View receiverView;
    private int fallbackToThisSizeWhenUndoing = 0;
    private final int finalHeight;
    private final long duration;

    private ValueAnimator valueAnimator;
    private Direction direction = Direction.EXECUTING;

    public CommandGrowView(View receiver, int finalHeight, int duration){
        this.receiverView = receiver;
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

    protected void execute(final Direction animationDirection){
        // if already executing in the given direction
        if(this.direction == animationDirection && isRunning()){
            return;
        }

        // use measuredHeight since the view may have already been measured but not drawn yet. In this case the Height would be = 0
        int startValue = receiverView.getMeasuredHeight();
        Log.d(Constants.LOGD,"receiverView.getHeight(): "+startValue);

        int finalValue = finalHeight;
        long currentDuration = duration;

        // remember new direction
        this.direction = animationDirection;

        if(animationDirection == Direction.UNDOING){
            // undo by shrinking the view back to its previous height
            finalValue = this.fallbackToThisSizeWhenUndoing;
        }

        // cancel if in progress
        if(valueAnimator != null && valueAnimator.isRunning()){
            // cancel the animation
            valueAnimator.cancel();
            Log.d(Constants.LOGD, "Cancel previous animation which was already running");
        }


        // now animate
        Log.d(Constants.LOGD,String.format("From %s px to %s px", startValue, finalValue));
        valueAnimator = getAnimator(startValue, finalValue, currentDuration);

        // listen for animation end
        valueAnimator.addListener(new Animator.AnimatorListener() {

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
        valueAnimator.start();
    }

    // explicitely tell the direction
    private ValueAnimator getAnimator(final int startHeight, final int finalHeight, final long durationOfAnimation) {

        if(valueAnimator != null && valueAnimator.isRunning()){
            valueAnimator.cancel();
        }

        // animate dummy height from current height to final height
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

// DELETE
//    private long computeRestNavigationDuration(int startValue, int endValue){
//        int currentDelta = Math.abs(startValue - endValue);
//        int initialDelta = Math.abs(startHeight - finalHeight);
//
//        // max value is the original time
//        double percentage = currentDelta / (initialDelta);
//
//        // the duration is just a part of the original duration
//        long resultingAnimationDuration = Math.round(duration * percentage);
//
//        // interval
//        resultingAnimationDuration = Math.min(resultingAnimationDuration, duration);
//        resultingAnimationDuration = Math.max(resultingAnimationDuration, 0);
//
//        return resultingAnimationDuration;
//    }

    // CLASSES

    public enum Direction {
        EXECUTING, UNDOING;

        public Direction toggle(){
            if(this == EXECUTING){
                return UNDOING;
            }
            return EXECUTING;
        };
    }
}
