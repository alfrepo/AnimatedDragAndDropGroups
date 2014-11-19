package de.mine.experiments.anim.animatedgroup;

import android.view.DragEvent;
import android.view.View;

/**
 * Views register them selfes to store the info who we currently are hovering over
*/
public class UtilDrag {

    private static View hoveringOverContainerView = null;

    public static View getHoveringOverContainerView() {
        return hoveringOverContainerView;
    }

    public static void registerAsHoveringView(View draggedView, View dragContainerView, DragEvent dragEvent){
        switch (dragEvent.getAction()){
            case DragEvent.ACTION_DRAG_ENTERED:
                hoveringOverContainerView = dragContainerView;
                break;
            case DragEvent.ACTION_DRAG_EXITED:
            case DragEvent.ACTION_DRAG_ENDED:
                if(hoveringOverContainerView == dragContainerView){
                    hoveringOverContainerView = null;
                }
                break;
        };
    }
}
