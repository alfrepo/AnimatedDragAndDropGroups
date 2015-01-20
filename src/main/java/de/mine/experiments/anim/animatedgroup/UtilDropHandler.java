package de.mine.experiments.anim.animatedgroup;

import android.content.ClipData;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Handles drop on Views.
 * <ol>
 *     <li> Replaces the dummy by the draggable which has been dropped.
 *     <li> Broadcasts the end of drop. Views will react on this event by hiding all dummies.
 * </ol>
 *
 * Created by skip on 03.12.2014.
 */
public class UtilDropHandler {

    public static final String CLIPDATA_KEY_DRAG_VIEW_RESSOURCE_ID = "ressourceId";


    /**
     * Creates a DragShadowBuilder with shadows which look like the given view of the given size
     * @param viewRessourceId - the resource id of the layout, retrieved as R.layout.*
     * @param layoutInflater - the layout inflator
     * @param width - width of shadow
     * @param height - height of shadow
     * @return - shadow builder
     */
    public static View.DragShadowBuilder createDragShadowBuilder(int viewRessourceId, LayoutInflater layoutInflater,  int width, int height){
        View view = layoutInflater.inflate(viewRessourceId, null);
        return createDragShadowBuilder(view, width, height);
    }

    /**
     * Creates a DragShadowBuilder with shadows which look like the given view of the given size
     * @param view - view template for the shadow
     * @param width - width of shadow
     * @param height - height of shadow
     * @return - shadow builder
     */
    public static View.DragShadowBuilder createDragShadowBuilder(View view, int width, int height){
        // measure the view
        int measureSpecWidth = View.MeasureSpec.makeMeasureSpec(500, View.MeasureSpec.EXACTLY);
        int measureSpecHeight = View.MeasureSpec.makeMeasureSpec(100, View.MeasureSpec.EXACTLY);
        view.measure(measureSpecWidth, measureSpecHeight);
        // layout view according to sizes which were measured
        view.layout(0, 0, width, height);
        // now shadow builder may be created - the layout of the view will be alright
        return new View.DragShadowBuilder(view);
    }


    /**
     * Schedule dropping the dummy. Will replace the the dummy by the View from ClipData
     */
    public static void scheduleDrop(AnimatorOfDummy droppedIntoDummy, ClipData clipData) {

    }

    /**
     * Schedule hiding the dummy. Is executed after all dropping is done
     */
    public static void scheduleHideDummyOnDragEnd(AnimatorOfDummy animatorOfDummy) {

    }


    /** Replaces the view in an NOT animated way
     *
     * ACHTUNG: it will only copy the LayoutParams from "replaceIt" to "replaceBy" if
     * replaceBy.mLayoutParams==null
     *
     * @param replaceIt - the view to replace
     * @param replaceBy - the replacement
     */
    public static void replaceView(View replaceIt, View replaceBy){
        if(replaceIt.getParent() == null || !(replaceIt.getParent() instanceof ViewGroup) ){
            throw new IllegalStateException(String.format("can not replace view %s in ViewParent %s", replaceIt, replaceIt.getParent() ));
        }

        // retrieve the parent
        ViewGroup replaceItParent = (ViewGroup) replaceIt.getParent();

        // retrieve the LayoutParams from "replaceBy" or fallback to "replaceIt"
        ViewGroup.LayoutParams layoutParamsChild = replaceBy.getLayoutParams();
        if(layoutParamsChild == null){
            layoutParamsChild = replaceIt.getLayoutParams();
        }
        int index = replaceItParent.indexOfChild(replaceIt);
        replaceItParent.removeView(replaceIt);

        //remove the replaceBy from its old parent
        Utils.removeFromParent(replaceBy);
        replaceItParent.addView(replaceBy, index, layoutParamsChild);

        // measure
        int widthMeasureSpec = View.MeasureSpec.makeMeasureSpec(replaceIt.getWidth(), View.MeasureSpec.EXACTLY);
        int heightMeasureSpec = View.MeasureSpec.makeMeasureSpec(replaceIt.getHeight(), View.MeasureSpec.UNSPECIFIED);
        replaceBy.measure(widthMeasureSpec, heightMeasureSpec);

        // layout
        replaceBy.requestLayout();
    }


    // remember the AnimatorOfDummy until the drag ends
    // on on drag end - either send the ViewStack back or remove it or move it to another view


    public static AnimatorOfDummy findResponsibleAnimatorOfDummy(View viewDraggedOut){
        // if there is a previous sibling which is an owner of AnimatorOfDummy
        // else if there is a parent of type AbstractViewGroup

        return null;
    }

}
