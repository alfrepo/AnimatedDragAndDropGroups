package de.mine.experiments.anim.layoutchange;

import android.animation.LayoutTransition;
import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.widget.LinearLayout;


/**
 * Created by skip on 02.09.2014.
 */
public class ViewLayout extends LinearLayout {

    public ViewLayout(Context context) {
        super(context);init(context);
    }
    public ViewLayout(Context context, AttributeSet attrs) {
        super(context, attrs);init(context);
    }
    public ViewLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    void init(Context context){
        int r = ((int) (Math.random()*255)) ;
        int g = ((int) (Math.random()*255)) ;
        int b = ((int) (Math.random()*255)) ;

        setBackgroundColor(Color.argb(255, r, g, b));

        LayoutTransition lt = new LayoutTransition();
        lt.enableTransitionType(LayoutTransition.CHANGING);
        lt.enableTransitionType(LayoutTransition.APPEARING);
        lt.enableTransitionType(LayoutTransition.CHANGE_APPEARING);
        lt.enableTransitionType(LayoutTransition.CHANGE_DISAPPEARING);
        this.setLayoutTransition(lt);

        setPadding(15,15,15,15);

        setMinimumHeight(50);
        setMinimumWidth(50);

//        LayoutInflater inflater = (LayoutInflater)   getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        inflater.inflate(R.layout.activity5_layout_viewlayout, this, true);
    }
}
