package de.mine.experiments.anim.viewpager;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ScrollView;

import de.mine.experiments.R;


public class ViewPagerActivity3 extends FragmentActivity implements Fragment3ScreenSlider.OnFragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity3_view_pager);

        ScrollView s;

        setUpViewPager();
    }

    private void setUpViewPager() {
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewPager);

        //transition
        viewPager.setPageTransformer(true, new ZoomOutPageTransformer());

        // getSUPPORTfragmentManager - "SUPPORT" is important. This returns a android.support.v4.app.FragmentManager which is compatible with android.support.v4.view.ViewPager
        FragmentManager fragmentManager = getSupportFragmentManager();

        // data source
        ScreenSlidePagerAdapter screenSlidePagerAdapter = new ScreenSlidePagerAdapter(fragmentManager);
        viewPager.setAdapter(screenSlidePagerAdapter);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.view_pager_activity3, menu);
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

    @Override
    public void onFragmentInteraction(Uri uri) {
        //nothing
    }
}
