package de.mine.experiments.anim.viewpager;

import android.support.v4.app.FragmentStatePagerAdapter;

import de.mine.experiments.anim.viewpager.Fragment3ScreenSlider;

/**
 * A simple pager adapter that represents 5 ScreenSlidePageFragment objects, in
 * sequence.
 */
public class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {

    private int NUM_PAGES = 2;

public ScreenSlidePagerAdapter(android.support.v4.app.FragmentManager fm) {
    super(fm);
}

@Override
    public android.support.v4.app.Fragment getItem(int position) {
        return new Fragment3ScreenSlider();
    }


    @Override
    public int getCount() {
        return NUM_PAGES;
    }
}