package de.mine.experiments.anim.animatedgroup;

import android.content.Context;
import android.graphics.Point;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import de.mine.experiments.anim.animatedgroup.command.Command;
import de.mine.experiments.anim.animatedgroup.command.CommandGrowView;

/**
 * Adds a dummy in an animated way when the method #onDragInAddDummyAnimation() is called
 * Removes the a dummy in an animated way, when method #onDragOutRemoveDummyAnimation() is called
 * Created by skip on 08.11.2014.
 */
public class AnimatorOfDummy {

    public static final int INITIAL_DUMMY_HEIGHT_BEFORE_EXPANDING = 0;
    public static final int DUMMY_HEIGHT =  250; // fixed height of dummies before drop
    public static final int DUMMY_ANIMATION_DURATION =  500; // ms

    // retrieve
    private Context context;
    private ViewGroup parentOfDummy;

    // the dummy which is added on drag in and removed on out
    private ViewDummyAnimated viewDummyAnimated = null;

    // command which is used to animate the dummy
    private CommandGrowView commandGrowView;

    public AnimatorOfDummy(Context context, ViewGroup parentOfDummy){
        this.context = context;
        this.parentOfDummy = parentOfDummy;
    }

    public void onDragInAddDummyAnimation(int dummyPositionInParent) {

        int parentWidth = parentOfDummy.getWidth();

        // create the dummy command if they are null
        boolean wasJustInitiatedTheDummy = this.initiatePlaceholderFollowerDummyAndCommand(parentWidth);
        Log.d("anim", "Width: "+parentWidth);

        /*  add the dummy to the group.
            It will be removed from the group when the undo animation finishes
            prepend the follower at the very first positon        */
        if (viewDummyAnimated.getParent() == null) {
            parentOfDummy.addView(viewDummyAnimated, dummyPositionInParent);
        }

        // remove the dummies on animation undo hook
        if (wasJustInitiatedTheDummy) {
            // when the command is reverted - the viewDummy should be removed from the group
            commandGrowView.addOnUndoFinishedListener(new Command.ListenerCommand() {
                @Override
                public void onTrigger() {
                    parentOfDummy.removeView(viewDummyAnimated);
                }
            });
        }

        // animate the addition of the view
        commandGrowView.execute();
    }


    public void onDragOutRemoveDummyAnimation(){
        if(viewDummyAnimated != null){
            commandGrowView.undo();
        }
    }

    // PUBLIC HELPER

    public ViewGroup getParentOfDummy() {
        return parentOfDummy;
    }

    public void destroy(){
        // undo animation
        this.commandGrowView.cancel();
        // remove the dummy from parent
        if(parentOfDummy!=null){
            parentOfDummy.removeView(viewDummyAnimated);
        }
    }

    // PRIVATE
    private boolean initiatePlaceholderFollowerDummyAndCommand(int maxDummyWidth){
        // create the command if necessary
        if(commandGrowView == null){

            // create a dummy
            viewDummyAnimated = createADummy(INITIAL_DUMMY_HEIGHT_BEFORE_EXPANDING, maxDummyWidth);

            // create the command
            commandGrowView = new CommandGrowView(viewDummyAnimated, INITIAL_DUMMY_HEIGHT_BEFORE_EXPANDING, DUMMY_HEIGHT, DUMMY_ANIMATION_DURATION);

            return true;
        }
        return false;
    }

    private ViewDummyAnimated createADummy(int initialDummyHeight, int dummyWidth) {
        ViewDummyAnimated dummy  = new ViewDummyAnimated(context);
        int itemDummyWidth = measureHowLargeTheViewWouldBeAsChild(dummy, initialDummyHeight, dummyWidth).x;

        // initial lp to avoid nullPointerException
        // assign the height of 0 to the dummy. WIll expand it soon
        ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(itemDummyWidth, initialDummyHeight);
        dummy.setLayoutParams(lp);

        return dummy;
    }

    public Point measureHowLargeTheViewWouldBeAsChild(View child, int exactHeightAdvice, int dummyWidth){
        // retrieve the measure specs width / height . Use matchparent / unspecified
        // provide infos how the this view as parent wants to see the child
        int measureSpecWidth = View.MeasureSpec.makeMeasureSpec(dummyWidth, View.MeasureSpec.AT_MOST);
        int measureSpecHeight = View.MeasureSpec.makeMeasureSpec(exactHeightAdvice, View.MeasureSpec.EXACTLY);

        // provide infos how the child wants to lay out itselfe (normally via xml)
        ViewGroup.LayoutParams lpChild = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        child.setLayoutParams(lpChild);

        // measure the child
        child.measure(measureSpecWidth, measureSpecHeight);
        final int childWidth = child.getMeasuredWidth();
        final int childHeight = child.getMeasuredHeight();
        return new Point(childWidth, childHeight);
    }
}
