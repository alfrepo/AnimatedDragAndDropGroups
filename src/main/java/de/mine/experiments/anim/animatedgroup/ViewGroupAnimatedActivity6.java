package de.mine.experiments.anim.animatedgroup;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import de.mine.experiments.R;
import de.mine.experiments.anim.animatedgroup.command.Invoker;
import de.mine.experiments.anim.overlay.ActionbarItemActivity8;

public class ViewGroupAnimatedActivity6 extends Activity {

    ViewGroup topMostContainer;
    LinearLayout linearLayout;
    ViewGroupAnimated viewGroupAnimated;

    int itemHeight;
    int dragshadowWidth;
    int viewGroupMinHeight;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity6_linearlayout_parent);
        init();
    }

    void init() {
        linearLayout = (LinearLayout) findViewById(R.id.parentContainerMain);
        viewGroupAnimated = (ViewGroupAnimated) findViewById(R.id.group2);
        topMostContainer = (ViewGroup)findViewById(R.id.topMostContainer);

        // init the Context. WIll switch to DI here
        Context.invoker = new Invoker(this);
        Context.activity = this;

        // setup scroll control which would control the main scrollView
        ScrollView scrollViewMain = (ScrollView)findViewById(R.id.scrollviewMain);
        ViewScrollBarControl scrollControlMain = (ViewScrollBarControl)findViewById(R.id.scrollBarControlMain);
        ViewScrollAreaTopBottom scrollControlUpMain = (ViewScrollAreaTopBottom)findViewById(R.id.scrollControlTopMain);
        ViewScrollAreaTopBottom scrollControlDownMain = (ViewScrollAreaTopBottom)findViewById(R.id.scrollControlBottomMain);

        setUpScrollControls(scrollViewMain, scrollControlMain, scrollControlUpMain, scrollControlDownMain);

        // setup scroll control which would control the archive scrollView
        ScrollView scrollViewArchive = (ScrollView)findViewById(R.id.scrollViewArchive);
        ViewScrollBarControl scrollControlArchive = (ViewScrollBarControl)findViewById(R.id.scrollBarControlArchive);
        ViewScrollAreaTopBottom scrollControlUpArchive = (ViewScrollAreaTopBottom)findViewById(R.id.scrollControlTopArchive);
        ViewScrollAreaTopBottom scrollControlDownArchive = (ViewScrollAreaTopBottom)findViewById(R.id.scrollControlBottomArchive);

        setUpScrollControls(scrollViewArchive, scrollControlArchive, scrollControlUpArchive, scrollControlDownArchive);

        itemHeight = (int) getResources().getDimension(R.dimen.viewitem_fixed_height);
        dragshadowWidth = (int) getResources().getDimension(R.dimen.drag_shadow_width);
        viewGroupMinHeight = (int) getResources().getDimension(R.dimen.viewgroup_min_height);
    }

    private void setUpScrollControls(ScrollView scrollView, ViewScrollBarControl scrollControl, ViewScrollAreaTopBottom scrollControlUp, ViewScrollAreaTopBottom scrollControlDown) {
        scrollControl.set(scrollView);
        scrollControlUp.set(scrollView, ViewScrollAreaTopBottom.ScrollDirection.UP);
        scrollControlDown.set(scrollView, ViewScrollAreaTopBottom.ScrollDirection.DOWN);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity8_actionbar_menu, menu);

        LayoutInflater inflaterView = (LayoutInflater) getApplicationContext().getSystemService(android.content.Context.LAYOUT_INFLATER_SERVICE);

        ActionbarItemActivity8 button1 = new ActionbarItemActivity8(getApplicationContext()){
            {
                super.setOnTouchListener(new OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        if(event.getAction() == MotionEvent.ACTION_DOWN){
                            ViewGroupAnimatedActivity6.this.startDrag(linearLayout, R.layout.activity6_view_item_animated, dragshadowWidth, itemHeight);
                        }
                        Log.d("motionEvent", "Event");
                        return true;
                    }
                });
            }
            @Override
            public void setOnTouchListener(OnTouchListener l) {
                // do not allow to override onTuuch
//                super.setOnTouchListener(l);
            }
        };
        button1.setText("Item");

        ActionbarItemActivity8 button2 = new ActionbarItemActivity8(getApplicationContext()){
            {
                super.setOnTouchListener(new OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        if(event.getAction() == MotionEvent.ACTION_DOWN){
                            ViewGroupAnimatedActivity6.this.startDrag(linearLayout, R.layout.activity6_view_group_animated, dragshadowWidth, viewGroupMinHeight);
                        }
                        Log.d("motionEvent", "Event");
                        return true;
                    }
                });
            }
            @Override
            public void setOnTouchListener(OnTouchListener l) {
                // do not allow to override onTuuch
//                super.setOnTouchListener(l);
            }
        };
        button2.setText("Group");


        MenuItem viewItem = menu.findItem(R.id.action_add_item);
        viewItem.setActionView(button1);
        MenuItem viewGroup = menu.findItem(R.id.action_add_group);
        viewGroup.setActionView(button2);

        return super.onCreateOptionsMenu(menu);
    }


    private void startDrag(View view, int shadowRessourceId, int shadowWidth, int shadowHeight){
        // start dragging
        DragController.create().startDragFromToolbar(topMostContainer, shadowRessourceId, shadowWidth, shadowHeight, getLayoutInflater());
    }

}
