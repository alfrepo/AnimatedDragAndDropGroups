package de.mine.experiments.anim.animatedgroup;

import android.view.DragEvent;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by skip on 09.11.2014.
 */
public final class Utils {

    public static final int getViewIndexInParent(View child) {
        ViewGroup parent = (ViewGroup)child.getParent();
        for(int i=0; i<parent.getChildCount(); i++){
            if(parent.getChildAt(i).equals(child)){
                return i;
            }
        }
        throw new IllegalStateException("Failed to retrieve the index of the view");
    }

    public static String getDragEventName(int dragEvent){
        switch (dragEvent){
            case DragEvent.ACTION_DRAG_STARTED:
                return "ACTION_DRAG_STARTED";
            case DragEvent.ACTION_DRAG_EXITED:
                return "ACTION_DRAG_EXITED";
            case DragEvent.ACTION_DROP:
                return "ACTION_DROP";
            case DragEvent.ACTION_DRAG_ENDED:
                return "ACTION_DRAG_ENDED";
            case DragEvent.ACTION_DRAG_ENTERED:
                return "ACTION_DRAG_ENTERED";
            case DragEvent.ACTION_DRAG_LOCATION:
                return "ACTION_DRAG_LOCATION";
        }
        return "unknown event";
    }

    /** Checks whether the Event  tells something about an update of the drag position over view*/
    public static Boolean isDraggingOverFromDragEvent(DragEvent event){
        switch (event.getAction()){
            case DragEvent.ACTION_DRAG_ENTERED:
                return true;
            case DragEvent.ACTION_DRAG_EXITED:
            case DragEvent.ACTION_DRAG_ENDED:
                return false;
            default:
                return null;
        }
    }
}
