package de.mine.experiments.anim.animatedgroup;

import android.content.ClipData;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import de.mine.experiments.R;

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


    // TODO skip - implement a mechanism, which will watch at the scheduled commands and do the drop and hiding


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

    public static ClipData createDragClipData(Integer ressourceIdDraggedView) {
        Log.d(ViewGroupAnimatedActivity6.TAG, ""+ressourceIdDraggedView);
        return ClipData.newPlainText(CLIPDATA_KEY_DRAG_VIEW_RESSOURCE_ID, String.valueOf(ressourceIdDraggedView) );
    }

    public static View createViewFromClipData(Context context, ClipData clipData, LayoutInflater layoutInflater) {

        // check the label
        if(!clipData.getDescription().getLabel().equals(CLIPDATA_KEY_DRAG_VIEW_RESSOURCE_ID)){
            throw new IllegalArgumentException("Wrong clipData passed");
        }

        // retrive the ressourceId
        String text = clipData.getItemAt(0).getText().toString();
        int ressourceId = Integer.valueOf(text);

        // Inflate from ressourceId
        if(ressourceId == R.layout.activity6_view_group_animated){
            ViewGroupAnimated v = new ViewGroupAnimated(context);
            /*  it is important that the ViewGroup has this LayoutParams.
                otherwise it won't adopt it's size when it's children should grow larger than the viewGroup
                ACHTUNG: just doing
                    LinearLayout.LayoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT
                does not work
              */
            v.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            return v;

        }else if(ressourceId == R.layout.activity6_view_item_animated){
            return new ViewItemAnimated(context);
        }

        // the View which needs layout
        return layoutInflater.inflate(ressourceId, null);
    }
}
