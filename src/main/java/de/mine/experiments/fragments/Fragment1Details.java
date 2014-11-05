package de.mine.experiments.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by skip on 03.08.2014.
 */
public class Fragment1Details extends Fragment {

    String TAG = "liveCycle";

    TextView content;
    View fragmentView;

    int onCreateViewCalled = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: fragmentView is "+fragmentView );

        // increase counter to know, whether variables are cleared or whether setRetainInstance works
        onCreateViewCalled++;

        // creating the view if it was not yet created! It may exist, if fragment reacts on configuration change by restarting the creation cycle
        if(fragmentView == null){
            // ACHTUNG: DO NOT ADD THE VIEW TO ROOT (last argument false). It causes "The specified child already has a parent. You must call removeView() on the child's parent first"
            fragmentView = inflater.inflate(de.mine.experiments.R.layout.fragment1_details, container, false);
            // remember the textview for setting content
            content = (TextView) fragmentView.findViewById(de.mine.experiments.R.id.textViewDetails);
            content.setText(""+ onCreateViewCalled);
        }

        return fragmentView;
    }

    public void setContent(String text){
        if(content==null) return;
        content.setText(text);
    }

    public void addContent(String text){
        if(content==null) return;
        content.setText(content.getText()+text);
    }
}
