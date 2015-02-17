package de.mine.experiments.anim.overlay;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import de.mine.experiments.R;

/**
 * Created by skip on 06.02.2015.
 */
public class ActionbarItemActivity8 extends LinearLayout {

    Context mContext;
    TextView mTextView;

    public ActionbarItemActivity8(Context context) {
        super(context);
        init(context);
    }

    public ActionbarItemActivity8(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public ActionbarItemActivity8(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    void init(Context context){
        this.mContext = context;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.activity8_menuitem, this, true);

        mTextView = (TextView) findViewById(R.id.text);
    }

    public void setText(String text){
        this.mTextView.setText(text);
    }

}
