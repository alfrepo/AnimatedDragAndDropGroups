package de.mine.experiments.anim.animatedgroup.command;

import android.animation.ValueAnimator;
import android.content.Context;
import android.view.View;

import junit.framework.Assert;

import de.mine.experiments.anim.animatedgroup.Constants;
import de.mine.experiments.anim.animatedgroup.Utils;
import de.mine.experiments.anim.animatedgroup.ViewDummyAnimated;

/**
 * Created by skip on 19.10.2014.
 */
public class CommandReplaceView extends AbstractCommand {

    private ValueAnimator valueAnimator;
    private Context context;
    private View replaceIt;
    private View replaceBy;

    /**
     * @param replaceIt
     * @param replaceBy the View should not have any parent, otherwise replacing wont work
     */
    public CommandReplaceView(Context context, View replaceIt, View replaceBy){
        Assert.assertNotNull(replaceIt);
        Assert.assertNotNull(replaceBy);

        this.replaceBy = replaceBy;
        this.replaceIt = replaceIt;
        this.context = context;
    }

    @Override
    public void execute() {
        execute(DIRECTION.EXECUTE);
    }

    @Override
    public void undo() {
        execute(DIRECTION.UNDO);
    }

    @Override
    public void cancel() {
        this.valueAnimator.cancel();
    }

    @Override
    public boolean isRunning() {
        return false;
    }

    protected void execute(final DIRECTION direction){
        View tempreplaceIt = replaceIt;
        View tempreplaceBy = replaceBy;

        // check whether undo was called and swap the VIews
        if(DIRECTION.UNDO == direction){
            View temp = tempreplaceBy;
            tempreplaceBy = tempreplaceIt;
            tempreplaceIt = temp;
        }

        // check whether the command is already in the right state
        if(tempreplaceIt.getParent() == null && tempreplaceBy.getParent() != null){
            // already done
            return;
        }

        // replace the current view by a dummy
        final ViewDummyAnimated viewDummyAnimated = new ViewDummyAnimated(context);

        // replace view by dummy. Keep size
        Utils.replaceView(tempreplaceIt, viewDummyAnimated);


        // MEASURING
        // width won't change
        int widthReplaceIt  = replaceIt.getWidth();
        // measure spec
        int measureSpecWidth = View.MeasureSpec.makeMeasureSpec(widthReplaceIt, View.MeasureSpec.AT_MOST);
        int measureSpecHeight = View.MeasureSpec.makeMeasureSpec(-1, View.MeasureSpec.UNSPECIFIED);
        // measure now
        replaceBy.measure(measureSpecWidth, measureSpecHeight);
        // now I know the new size of the view
        int widthReplaceByInNewParent  = replaceBy.getMeasuredWidth();
        int heightReplaceByInNewParent = replaceBy.getMeasuredHeight();


        // grow the dummy to the size of replaceIt view
        CommandGrowView commandGrowView = new CommandGrowView(viewDummyAnimated, heightReplaceByInNewParent , Constants.DUMMY_TIME_ANIMATION_DURATION_MS);
        commandGrowView.execute();

        // now, when the dummy has reached the intended size - replace it by the replaceBy
        final View finalTempreplaceBy = tempreplaceBy;
        commandGrowView.addOnExecutionSucessfullyFinishedListener(new ListenerCommand() {
            @Override
            public void onTrigger() {
                Utils.replaceView(viewDummyAnimated, finalTempreplaceBy);

                // notify that we are finished
                if(direction.equals(DIRECTION.EXECUTE)){
                    notifyOnExecutionSuccessfullyFinishsListener();

                }else if(direction.equals(DIRECTION.UNDO)){
                    notifyOnUndoFinishsListener();
                }

            }
        });
    }


    public enum DIRECTION { EXECUTE, UNDO};
}
