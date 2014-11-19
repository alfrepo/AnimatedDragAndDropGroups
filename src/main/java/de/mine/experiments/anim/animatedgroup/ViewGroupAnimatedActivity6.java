package de.mine.experiments.anim.animatedgroup;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.ClipData;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewPropertyAnimator;
import android.widget.LinearLayout;
import android.widget.Toast;

import de.mine.experiments.R;

public class ViewGroupAnimatedActivity6 extends Activity {

    public static final String TAG = "applog";

    View buttonAddsItem;
    View buttonAddsGroup;

    View buttonDragItem;
    View buttonDragGroup;

    LinearLayout linearLayout;

    ViewGroupAnimated viewGroupAnimated;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity6_linearlayout_parent);

        init();
    }

    void init() {
        buttonAddsItem = findViewById(R.id.addItem);
        buttonAddsGroup = findViewById(R.id.addGroup);
        buttonDragItem = findViewById(R.id.dragItem);
        buttonDragGroup = findViewById(R.id.dragGroup);

        linearLayout = (LinearLayout) findViewById(R.id.linearLayoutGroups);
        viewGroupAnimated = (ViewGroupAnimated) findViewById(R.id.group2);

        initDragItemListener();
    }

    void initDragItemListener() {
        buttonDragItem.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    // which data to pass on drop
                    ClipData clipData = ClipData.newPlainText("label", "item");

                    // create a new shadow builder
                    ViewItemAnimated viewItemAnimated = new ViewItemAnimated(getApplicationContext());

                    // TODO measure the item view and use it as shadow
//                View.DragShadowBuilder dragShadowBuilder = new View.DragShadowBuilder(viewItemAnimated);
                    View.DragShadowBuilder dragShadowBuilder = new View.DragShadowBuilder(v);

                    // start dragging
                    v.startDrag(clipData, dragShadowBuilder, null, 0);
                }
                return false;
            }
        });
    }

    void initAddItemListener() {
        // add listener
        buttonAddsItem.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    Log.d("tag", "onTouch addItem");
                    View item = createItem();
                    viewGroupAnimated.addViewAnimated(item);
                }

                return true;
            }
        });
    }

    private View createItem() {
        ViewItemAnimated item = new ViewItemAnimated(getApplicationContext());
        // TODO layout
        return item;
    }

    void createVga() {
        ViewGroup parent = (ViewGroup) findViewById(R.id.linearLayout);
        // mine
        ViewGroupAnimated vga = new ViewGroupAnimated(getApplicationContext());
        vga.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        parent.addView(vga);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
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
    public boolean onTouchEvent(MotionEvent event) {
        boolean result = super.onTouchEvent(event);

        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            Toast.makeText(getApplicationContext(), "down", Toast.LENGTH_SHORT).show();
            resize(true);
        } else if (event.getAction() == MotionEvent.ACTION_UP) {
            Toast.makeText(getApplicationContext(), "up", Toast.LENGTH_SHORT).show();
            resize(false);
        }

        return result;
    }


    public void resize(boolean isLarge) {
        final View v = findViewById(R.id.item2);
        ViewPropertyAnimator animator = v.animate();
        if (isLarge) {
//            animator.scaleY(30);
//            ObjectAnimator.ofFloat(v, "height", 100f).start();

            ValueAnimator va = ValueAnimator.ofInt(100, 400);
            va.setDuration(200);
            va.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                public void onAnimationUpdate(ValueAnimator animation) {
                    Integer value = (Integer) animation.getAnimatedValue();
                    v.getLayoutParams().height = value.intValue();
                    v.requestLayout();
                }
            });
            va.start();

        } else {
//            animator.scaleY(-30);
            ObjectAnimator.ofFloat(v, "height", 10f).start();

            ValueAnimator va = ValueAnimator.ofInt(400, 100);
            va.setDuration(200);
            va.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                public void onAnimationUpdate(ValueAnimator animation) {
                    Integer value = (Integer) animation.getAnimatedValue();
                    v.getLayoutParams().height = value.intValue();
                    v.requestLayout();
                }
            });
            va.start();
        }
    }
}
