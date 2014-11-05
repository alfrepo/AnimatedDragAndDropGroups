package de.mine.experiments.anim.animatedgroup;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import java.util.List;

/**
* Created by skip on 27.09.2014.
*/
public class ViewParentContainer extends LinearLayout implements AbstractViewGroup {

    ViewDummyAnimated firstDummy;

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
        //TODO
    }

    void add(AbstractFigure figure, int index){
        // TODO
    }

    void addAfter(AbstractFigure figure){
        // TODO
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
