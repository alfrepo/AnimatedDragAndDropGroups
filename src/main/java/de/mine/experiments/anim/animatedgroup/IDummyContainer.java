package de.mine.experiments.anim.animatedgroup;

import android.view.View;

/**
 * Created by skip on 04.01.2015.
 */
public interface IDummyContainer {

    /** Returns the #AnimatorOfDummy which's spacen the given View does occupy */
    AnimatorOfDummy findResponsibleAnimatorOfDummy(View view);
}
