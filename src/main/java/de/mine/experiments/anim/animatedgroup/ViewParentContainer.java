package de.mine.experiments.anim.animatedgroup;

import android.content.Context;
import android.util.AttributeSet;
import android.view.DragEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.List;

/**
* Created by skip on 27.09.2014.
*/
public class ViewParentContainer extends LinearLayout implements AbstractViewGroup, IDummyContainer, ViewGroup.OnHierarchyChangeListener{

    AnimatorOfDummy animatorOfDummyInsider;

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
        // pass the information about additon of new Children to the children themselves if they implement OnHierarchyChangeListener
        this.setOnHierarchyChangeListener(Utils.getOnHierarchyChangeListenerWhichNotifiesChildren());

        // create the dummy with this as parent
        animatorOfDummyInsider = new AnimatorOfDummy(getContext(), this);
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

        /*
            TODO: handle onDragEnter only when the stack becomes visible / is visible at dragStart
            this would avoid to moving the whole stack down on every drag
          */

        // notify the dummy
        this.animatorOfDummyInsider.onDrag(this, event);

        /** It is important to call the super.dispatch(). Or the children will never be able to receive drag.         */
        super.dispatchDragEvent(event);

        /** It is important to return "true" here. Or this view will not be marked as one, which wishes to receive drag.*/
        return true;
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

    // IDummyContainer


    @Override
    public AnimatorOfDummy findResponsibleAnimatorOfDummy(View view) {
        boolean isReplaceableByInsiderAnimatorOfDummy = UtilDropHandler.isReplaceableByInsiderAnimatorOfDummy(this, view, animatorOfDummyInsider);
        if(isReplaceableByInsiderAnimatorOfDummy){
            return animatorOfDummyInsider;
        }
        return null;
    }

    // ViewGroup.OnHierarchyChangeListener


    @Override
    public void onChildViewAdded(View parent, View child) {
        ((OnHierarchyChangeListener)child).onChildViewAdded(parent, child);
    }

    @Override
    public void onChildViewRemoved(View parent, View child) {
        ((OnHierarchyChangeListener)child).onChildViewRemoved(parent, child);
    }
}
