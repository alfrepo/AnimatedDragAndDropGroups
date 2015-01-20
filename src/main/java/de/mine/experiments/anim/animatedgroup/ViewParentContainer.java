package de.mine.experiments.anim.animatedgroup;

import android.content.Context;
import android.util.AttributeSet;
import android.view.DragEvent;
import android.view.View;
import android.widget.LinearLayout;

import java.util.List;

/**
* Created by skip on 27.09.2014.
*/
public class ViewParentContainer extends LinearLayout implements AbstractViewGroup {

    public ViewParentContainer(Context context) {
        super(context);
        init();
    }

    public ViewParentContainer(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ViewParentContainer(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }


    private void init(){
        this.setOnHierarchyChangeListener(getOnHierarchyChangeListener());
    }

    // ENFORCE OnHierarchyChangeListener as children

    @Override
    public void addView(View child) {
        // check whether the view implements the OnHierarchyChangeListener
        if(!(child instanceof  OnHierarchyChangeListener)){
            throw new IllegalArgumentException(String.format("The child %s has to implemement OnHierarchyChangeListener", child.getClass().getSimpleName()));
        }
        super.addView(child);
    }

    private OnHierarchyChangeListener getOnHierarchyChangeListener(){
        return new OnHierarchyChangeListener() {
            @Override
            public void onChildViewAdded(View parent, View child) {
                // notify  child if, that it was added to a new parent. Let them REMOVE Dummies
                ((OnHierarchyChangeListener)child).onChildViewAdded(parent, child);
            }

            @Override
            public void onChildViewRemoved(View parent, View child) {
                // notify  child if, that it was added to a new parent. Let them REMOVE Dummies
                ((OnHierarchyChangeListener)child).onChildViewRemoved(parent, child);
            }
        };
    }

    void add(AbstractFigure figure, int index){
        // TODO
    }

    void addAfter(AbstractFigure figure){
        // TODO
    }

    @Override
    public boolean dispatchDragEvent(DragEvent event) {
        // notify the filters
        notifyDragFilter(event);
        return super.dispatchDragEvent(event);
    }

    /* sends all Drag Events to the DragControler, which gives it the ability to handle on DRAG_START
       and DRAG_END events      */
    private void notifyDragFilter(DragEvent event){
        DragController dragController = de.mine.experiments.anim.animatedgroup.Context.dragController;
        if(dragController != null){
            dragController.notifyDragFilter(this, event);
        }
    }

    // INTERFACES

    @Override
    public void addSuccessor(AbstractFigure figure) {
        // TODO
    }

    @Override
    public void replace(AbstractFigure child) {
        // TODO
    }

    @Override
    public List<AbstractFigure> getChildren() {
        // TODO
        return null;
    }

    @Override
    public void addChild(AbstractFigure child) {
        // TODO
    }

    @Override
    public AbstractFigure getParentAbstractFigure() {
        // TODO
        return null;
    }

}
