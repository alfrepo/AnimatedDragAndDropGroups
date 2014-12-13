package de.mine.experiments.anim.animatedgroup;

import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.View;

/**
 * Handles drop on Views.
 * <ol>
 *     <li> Replaces the dummy by the draggable which has been dropped.
 *     <li> Broadcasts the end of drop. Views will react on this event by hiding all dummies.
 * </ol>
 *
 * Created by skip on 03.12.2014.
 */
public class DropHandler {


    public static void onDropOnDummy(ViewDummyAnimated dummyTarget, DragEvent dropEvent){

        // retrieve the dragged View
        // replace the dummy by the widget in an animated way

        // TODO
//        dummyTarget.replaceBy(draggedView);

        // broadcast the end of drag - everybody may hide their dummies if available
    }

    /**
     * Creates a DragShadowBuilder with shadows which look like the given view of the given size
     * @param viewRessourceId - the resource id of the layout, retrieved as R.layout.*
     * @param layoutInflater - the layout inflator
     * @param width - width of shadow
     * @param height - height of shadow
     * @return - shadow builder
     */
    public static  View.DragShadowBuilder createDragShadowBuilder(int viewRessourceId, LayoutInflater layoutInflater,  int width, int height){
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
}
