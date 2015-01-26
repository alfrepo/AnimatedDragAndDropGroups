package de.mine.experiments.anim.animatedgroup;

import android.view.View;

/**
 * An interface which is implemented by views, which contain a dummy.
 * A dummContainer may be asked for a dummy, e.g. to display the dummy when a view is dragged out
 */
public interface IDummyContainer {

    /** Returns the #AnimatorOfDummy which's spacen the given View does occupy */
    AnimatorOfDummy findResponsibleAnimatorOfDummy(View view);
}
