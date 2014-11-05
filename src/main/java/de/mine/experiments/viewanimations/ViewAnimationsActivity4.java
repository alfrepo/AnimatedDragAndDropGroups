package de.mine.experiments.viewanimations;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.ScaleAnimation;
import android.widget.RelativeLayout;

import de.mine.experiments.M;
import de.mine.experiments.R;

public class ViewAnimationsActivity4 extends ActionBarActivity {

    View active;
    View viewA;
    View viewB;
    View viewParent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity4_view_animations);

        setUpAnimations();
    }

    private void setUpAnimations() {

        viewA = findViewById(R.id.ViewA);
        viewB = findViewById(R.id.ViewB);
        viewParent = findViewById(R.id.parentContainer);

        active = viewA;

        viewParent.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if(motionEvent.getAction() == MotionEvent.ACTION_UP){
                    if(active.equals(viewA)){
                        activate(viewA, viewB);
                    }else{
                        activate(viewB, viewA);
                    }
                }
                return true;
            }
        });
    }

    private void activate(final View old, final View neww){
        M.log(viewParent.getWidth() + " "+ viewParent.getHeight());

        int mShortAnimationDuration = getResources().getInteger(
                android.R.integer.config_shortAnimTime);

        neww.animate()
                .alpha(255)
                .scaleX(1)
                .scaleY(1)
                .withStartAction(new Runnable() {
                    @Override
                    public void run() {
                        neww.setVisibility(View.VISIBLE);
                    }
                })
                .setDuration(mShortAnimationDuration)
                .start();

        old.animate()
                .alpha(0)
                .scaleX(0)
                .scaleY(0)
                .withEndAction(new Runnable() {
                    @Override
                    public void run() {
                        old.setVisibility(View.GONE);
                    }
                })
                .setDuration(mShortAnimationDuration)
                .start();

        active = neww;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.view_animations_activity4, menu);
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
