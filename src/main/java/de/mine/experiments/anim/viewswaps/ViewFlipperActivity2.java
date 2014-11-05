package de.mine.experiments.anim.viewswaps;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ViewFlipper;

import de.mine.experiments.R;

public class ViewFlipperActivity2 extends ActionBarActivity {

    ViewFlipper viewFlipper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity2_view_flipper);

        viewFlipper = (ViewFlipper) findViewById(R.id.viewFlipper);
        setUpViewFlipper();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.view_flipper_activity2, menu);
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

    private void setUpViewFlipper(){

        Animation inAnimation = AnimationUtils.loadAnimation(getApplicationContext(), android.R.anim.fade_in);
        Animation outAnimation = AnimationUtils.loadAnimation(getApplicationContext(), android.R.anim.fade_in);

        viewFlipper.setInAnimation(inAnimation);
        viewFlipper.setOutAnimation(outAnimation);

        viewFlipper.setFlipInterval(1000);
        viewFlipper.startFlipping();
    }
}
