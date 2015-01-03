package de.mine.experiments.anim.animatedgroup;

import android.content.ClipData;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import de.mine.experiments.anim.animatedgroup.command.CommandReplaceView;
import de.mine.experiments.anim.animatedgroup.command.CommandReplaceViewParameters;
import de.mine.experiments.anim.animatedgroup.command.CommandsDelayFilterDrop;

/**
 * Created by skip on 10.09.2014.
 */
public class ViewDummyAnimated extends View implements AbstractFigure, ViewGroup.OnHierarchyChangeListener, IDragInViewIdentifier {

    private int defaultHeight = 100;
    private AbstractFigure parent;

    private ViewDummyAnimated follower;
    public boolean isDraggingOverThis = false;

    private OnDragListener onDragListener;

    public ViewDummyAnimated(Context context) {
        super(context);
        init();
    }


    @Override
    public boolean isDraggingWithinView() {
        return isDraggingOverThis;
    }


    private void init(){
        // TODO me de random BG
        setRandomBg();
    }

    @Override
    public void onChildViewAdded(View parent, View child) {
        // dummy does nothing when it is added
    }

    @Override
    public void onChildViewRemoved(View parent, View child) {
        // dummy does nothing when it is removed
    }

    public void onDropOnDummy(ViewDummyAnimated dummyTarget, DragEvent dropEvent){

        // retrieve the dragged View
        ClipData clipData = dropEvent.getClipData();
        View view = UtilDropHandler.createViewFromClipData(getContext(), clipData, LayoutInflater.from(getContext()));

        // replace the dummy by the dragged View
        CommandReplaceView commandReplaceView = new CommandReplaceView(getContext(), this, view);

        // TODO skip - use Invoker to have the possibility to delay command execution
//      commandReplaceView.execute();
        // install a delay filter which will delay grow commands
        de.mine.experiments.anim.animatedgroup.Context.invoker.addFilter(new CommandsDelayFilterDrop(de.mine.experiments.anim.animatedgroup.Context.invoker, commandReplaceView));
        // make invoker execute the replace command
        de.mine.experiments.anim.animatedgroup.Context.invoker.executeCommand(commandReplaceView, new CommandReplaceViewParameters(CommandReplaceView.DIRECTION.EXECUTE));
    }


    @Override
    public boolean dispatchDragEvent(DragEvent event) {

        /*  check, whether we are dragging over the dummy
            use the dispatchDragEvent method to capture events internally - they arrive here life.
            The listener may be overridden from outside - there may be only one
         */
        Boolean change = Utils.isDraggingOverFromDragEvent(event);
        if(change != null && isDraggingOverThis!=change){
            isDraggingOverThis = change;
            Log.d("isDraggingOverThis", "Dummy: isDraggingOverThis: "+isDraggingOverThis);
        }

        if(event.getAction() == DragEvent.ACTION_DROP){
            Toast.makeText(getContext(), "drop", Toast.LENGTH_SHORT).show();
            onDropOnDummy(this, event);
        }

        super.dispatchDragEvent(event);

        /** Totally important! View which wish to receive drag events
         * (start, stop, location) should return true when the receive a
         * DragEvent.ACTION_DRAG_STARTED */
        return true;
    }

    @Override
    public AbstractFigure getParentAbstractFigure() {
        return parent;
    }

    public void setDefaultHeight(int height) {
        this.defaultHeight = height;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int height = defaultHeight;
        ViewGroup.LayoutParams lp = getLayoutParams();
        if(lp != null && lp.height>=0){
            height = lp.height;
        }
        int mHeightMeasureSpec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY);
        super.onMeasure(widthMeasureSpec, mHeightMeasureSpec);
        setMeasuredDimension(getMeasuredWidth(), getMeasuredHeight());
    }

    private void setRandomBg(){
        int r = ((int) (Math.random()*255)) ;
        int g = ((int) (Math.random()*255)) ;
        int b = ((int) (Math.random()*255)) ;
        setBackgroundColor(Color.argb(255, r, g, b));
    }
}
