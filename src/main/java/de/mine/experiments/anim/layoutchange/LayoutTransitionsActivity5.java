package de.mine.experiments.anim.layoutchange;

import android.animation.LayoutTransition;
import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

import de.mine.experiments.R;

public class LayoutTransitionsActivity5 extends Activity {

    ViewLayout vl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity5_layout_transitions);
        final LinearLayout linearLayout = (LinearLayout) this.findViewById(R.id.linearLayout);

        LayoutTransition lt = new LayoutTransition();
        lt.enableTransitionType(LayoutTransition.CHANGING);
        lt.enableTransitionType(LayoutTransition.APPEARING);
        lt.disableTransitionType(LayoutTransition.CHANGE_APPEARING);
        lt.disableTransitionType(LayoutTransition.CHANGE_DISAPPEARING);
        linearLayout.setLayoutTransition(lt);

        vl = new ViewLayout(getApplicationContext());
        linearLayout.addView(vl);

        for(int i=0; i<32; i++){
            addView();
        }

        linearLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                addView();
                return false;
            }
        });
    }

    private void addView(){
        ViewLayout vl2 = new ViewLayout(getApplicationContext());
        vl.addView(vl2);
        vl = vl2;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.layout_transitions_activity5, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
