package de.mine.experiments.anim.viewswaps;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ViewAnimator;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import de.mine.experiments.R;

public class ViewAnimatorActivity2 extends ActionBarActivity {

    ViewAnimator viewAnimator;
    private static final String TAG = "mytag";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity2_view_animator);

        viewAnimator = (ViewAnimator) findViewById(R.id.viewAnimator);
        setUpViewAnimator(viewAnimator);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.view_animator_activity2, menu);
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

    private void setUpViewAnimator(final ViewAnimator viewAnimator){

        final View viewA = viewAnimator.findViewById(R.id.ViewA);
        final View viewB = viewAnimator.findViewById(R.id.ViewB);

        viewAnimator.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG,"onClick");
            }
        });

        viewAnimator.setOnTouchListener(new View.OnTouchListener() {

            // all animation ids
            List<Integer> animationIds = getAnimationIds();
            Iterator<Integer> iterator = animationIds.iterator();

            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                Log.d(TAG,"ontouch");

                if(motionEvent.getAction() == MotionEvent.ACTION_UP){
                    if(viewAnimator.getCurrentView().equals(viewA)){
                        viewAnimator.showNext();
                    }else{
                        viewAnimator.showPrevious();
                    }
                    setAnimation(iterator.next());

                    if(!iterator.hasNext()){
                        iterator = animationIds.iterator();
                    }
                }
                return true;
            }
        });
    }

    private List<Integer> getAnimationIds(){
        ArrayList<Integer> result = new ArrayList<Integer>();
//        for(Field field:android.R.anim.class.getDeclaredFields()){
//            if(!Modifier.isStatic(field.getModifiers())){
//                continue;
//            }
//            try{
//                result.add(field.getInt(null));
//                Log.d(TAG, field.getInt(null) + "");
//            }catch (Exception e){
//                // not an integer
//            }
//        }

        result.add(android.R.anim.fade_in);
        result.add(android.R.anim.fade_out);
        result.add(android.R.anim.slide_in_left);
        result.add(android.R.anim.slide_out_right);
        return result;
    }

    private void setAnimation(int id){
        setAnimation(id, id);
    }

    private void setAnimation(int idIn, int idOut){
        Animation animationIn = AnimationUtils.loadAnimation(getApplicationContext(), idIn);
        Animation animationOut = AnimationUtils.loadAnimation(getApplicationContext(), idOut);

        viewAnimator.setInAnimation(animationIn);
        viewAnimator.setOutAnimation(animationOut);
    }
}
