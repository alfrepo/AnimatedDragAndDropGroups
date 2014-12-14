package de.mine.experiments.anim.animatedgroup;

import android.app.Activity;
import android.content.ClipData;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

import de.mine.experiments.R;

public class ViewGroupAnimatedActivity6 extends Activity {

    public static final String TAG = "applog";

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
        buttonDragItem = findViewById(R.id.dragItem);
        buttonDragGroup = findViewById(R.id.dragGroup);

        linearLayout = (LinearLayout) findViewById(R.id.linearLayoutGroups);
        viewGroupAnimated = (ViewGroupAnimated) findViewById(R.id.group2);

        initDragItemListener();
    }

    void initDragItemListener() {
        // drag item
        buttonDragItem.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    startDrag(v, R.layout.activity6_view_item_animated, 500, 100);
                }
                return false;
            }
        });
        // drag group
        buttonDragGroup.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    startDrag(v, R.layout.activity6_view_group_animated, 500, 200);
                }
                return false;
            }
        });
    }

    private void startDrag(View view, int shadowRessourceId, int width, int height){
        // which data to pass on drop
        ClipData clipData = ClipData.newPlainText("ressourceId", String.valueOf(shadowRessourceId) );

        //  measure the item view and use it as shadow
        View.DragShadowBuilder dragShadowBuilder = DropHandler.createDragShadowBuilder(shadowRessourceId, getLayoutInflater(), width, height);

        // start dragging
        view.startDrag(clipData, dragShadowBuilder, null, 0);
    }

}
