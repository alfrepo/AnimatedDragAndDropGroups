package de.mine.experiments.anim.animatedgroup;

import android.content.Context;
import android.graphics.Point;
import android.util.Log;
import android.view.DragEvent;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import de.mine.experiments.anim.animatedgroup.command.Command;
import de.mine.experiments.anim.animatedgroup.command.CommandGrowView;

/**
 * Adds a dummy in an animated way when the method #onDragInAddDummyAnimation() is called
 * Removes the a dummy in an animated way, when method #onDragOutRemoveDummyAnimation() is called
 * Created by skip on 08.11.2014.
 */
public class AnimatorOfDummy implements IDragInViewIdentifier {

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

    // last event which may be passed to the dummy
    private DragEvent dragEvent;

    // dragListeners which are registered on the dummy
    private List<View.OnDragListener> dummyOnDragListeners = new ArrayList<View.OnDragListener>();

    /**
     * Create a new AnimatorOfDummy
     * @param context - the context
     * @param parentOfDummy - the parent which dummies will be added to
     * */
    public AnimatorOfDummy(Context context, ViewGroup parentOfDummy){
        this.context = context;
        attachToParent(parentOfDummy);
    }

    /**
     * Use this constructor only with method #attachToParent, when the parent is not known
     * at creation time.
     * Call #attachToParent as soon as the parent is known, otherwise animation will not be possible
     * @param context
     */
    public AnimatorOfDummy(Context context){
        this.context = context;
    }

    /**
     * Attachement to a new parent. Happens if a dummy is moved from one Parent to another,
     * together with it's view
     * @param parentOfDummy
     */
    public void attachToParent(ViewGroup parentOfDummy){
        // first detach from parent
        detachFromParent();

        // reattach to the new parent
        this.parentOfDummy = parentOfDummy;
    }

    /**
     * This method will reinitialize the Dummy animator every time when this View is added to a new
     * parent, since the dummy animator must know the parent to add the dummy follower to it.
     */
    public void attachToParentOfDummyBySibling(View viewSiblingOfDummy){
        if(viewSiblingOfDummy.getParent() != null){

            // dummy not empty but has wrong parent
            if((this.getParentOfDummy() != viewSiblingOfDummy.getParent())){
                this.detachFromParent();
            }

            // dummy does not have a parent yet
            if(this.getParentOfDummy() == null){
                // reattach to the new parent
                this.attachToParent((ViewGroup) viewSiblingOfDummy.getParent());
            }
        }
    }

    /** Cancel the animation and remove the dummy from parent. */
    public void detachFromParent(){
        // undo animation
        if(commandGrowView != null){
            this.commandGrowView.cancel();
        }

        // remove the dummy from parent
        if(parentOfDummy!=null){
            parentOfDummy.removeView(viewDummyAnimated);
        }
    }

    /** The AnimatorOfDummy has to be notified about drag start, to remember the DragEvent.
     *  It is needed to switch new dummies to drag mode.
     *  @param dragEvent
     */
    public void onDragStarted(DragEvent dragEvent){
        this.dragEvent = dragEvent;
    }

    /** The AnimatorOfDummy has to be notified about drag end, to remove the dummy from the parent.
     *  @param dragEvent
     */
    public void onDragEnded(DragEvent dragEvent){
        this.dragEvent = null;
    }

    /** Notify the AnimatorOfDummy when som object is dragged into the parent.
     *  The dummy will be added to the Parent and grown to a large size
     * @param dummyPositionInParent
     */
    public void onDragInAddDummyAnimation(int dummyPositionInParent) {
        if(parentOfDummy == null){
            Log.e(Constants.LOGE, "The Parent should be set by using #attachToParent(). Otherwise no animation will occur.");
            return;
        }

        // lazy creation of dumm on drag in
        initDummy(dummyPositionInParent);

        // animate the addition of the view
        commandGrowView.execute();
    }

    /**
     * notify about dragOut, so that the dummy may be removed from parent
     * */
    public void onDragOutRemoveDummyAnimation(){
        if(viewDummyAnimated != null){
            commandGrowView.undo();
        }
    }

    /** On Drag Listener which will be triggered on the dummy to the list of listeners */
    public void addDummyOnDragListener(View.OnDragListener dummyOnDragListener) {
        this.dummyOnDragListeners.add(dummyOnDragListener);
    }
    public void removeDummyOnDragListener(View.OnDragListener dummyOnDragListener) {
        this.dummyOnDragListeners.remove(dummyOnDragListener);
    }
    public List<View.OnDragListener> getDummyOnDragListener() {
        return dummyOnDragListeners;
    }

    // PUBLIC HELPER


    @Override
    public boolean isDraggingWithinView() {
        if( viewDummyAnimated != null){
            return viewDummyAnimated.isDraggingWithinView();
        }
        return false;
    }


    /** Returns the current ViewGroup which the dummy will be added to, when the method #onDragInAddDummyAnimation is called */
    public ViewGroup getParentOfDummy() {
        return parentOfDummy;
    }

    /** Returns the encapsulated ViewDummyAnimated */
    public ViewDummyAnimated getViewDummyAnimated() {
        return viewDummyAnimated;
    }

    // PRIVATE

    private void initDummy(int dummyPositionInParent) {
        int parentWidth = parentOfDummy.getWidth();

        // create the dummy command if they are null
        boolean wasJustInitiatedTheDummy = this.initiatePlaceholderFollowerDummyAndCommand(parentWidth);
        Log.d("anim", "Width: " + parentWidth);

        /*  add the dummy to the group.
            It will be removed from the group when the undo animation finishes
            prepend the follower at the very first positon        */
        if (viewDummyAnimated.getParent() == null) {
            parentOfDummy.addView(viewDummyAnimated, dummyPositionInParent);

            /* after adding  a Child to the parent, when dragging is already happening -
             * DRAGEVENT.START has to be passed to the parent again,
             * so that it may be passed to all children and to the new child too.
             * It will activate the new child and switch it to drag mode.
             */
            if(dragEvent != null){
                parentOfDummy.dispatchDragEvent(dragEvent);
            }else{
                Log.e(ViewGroupAnimatedActivity6.TAG, "Something went wrong - there is no DragEvent to switch new dummy to drag mode");
            }
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
    }

    private boolean initiatePlaceholderFollowerDummyAndCommand(int maxDummyWidth){
        // create the command if necessary
        if(commandGrowView == null){

            Log.d("isDraggingOverThis", "initiatePlaceholderFollowerDummyAndCommand");

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
        addOnDragListener(dummy);
        int itemDummyWidth = measureHowLargeTheViewWouldBeAsChild(dummy, initialDummyHeight, dummyWidth).x;

        // initial lp to avoid nullPointerException
        // assign the height of 0 to the dummy. WIll expand it soon
        ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(itemDummyWidth, initialDummyHeight);
        dummy.setLayoutParams(lp);

        return dummy;
    }

    private void addOnDragListener(ViewDummyAnimated dummy){
        dummy.setOnDragListener(new View.OnDragListener() {
            @Override
            public boolean onDrag(View v, DragEvent event) {
                if(!dummyOnDragListeners.isEmpty()){
                    for(View.OnDragListener l:dummyOnDragListeners){
                        l.onDrag(v, event);
                    }
                }
                return false;
            }
        });
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
