package de.mine.experiments.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import de.mine.experiments.anim.animatedgroup.Constants;

/**
 * Created by skip on 03.08.2014.
 */
public class Fragment1List extends android.app.ListFragment{

    private static final String TAG = "lifecycle";
    private AdapterView.OnItemClickListener onItemClickListener;


    @Override
    public void onInflate(Activity activity, AttributeSet attrs, Bundle savedInstanceState) {
        super.onInflate(activity, attrs, savedInstanceState);
        Log.d(Constants.LOGD,"onInflate");
        createContent();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        Log.d(Constants.LOGD,"onAttach");
        createContent();
    }
    

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(Constants.LOGD,"onCreate");
        createContent();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        createContent();
        View v = super.onCreateView(inflater, container, savedInstanceState);
        Log.d(Constants.LOGD,"onCreateView");
        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d(Constants.LOGD,"onActivityCreated");
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(Constants.LOGD,"onStart");
        createContent();
    }


    @Override
    public void onResume() {
        super.onResume();
        Log.d(Constants.LOGD,"onResume");
    }


    @Override
    public void onPause() {
        super.onPause();
        Log.d(Constants.LOGD,"onPause");
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.d(Constants.LOGD,"onSaveInstanceState");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(Constants.LOGD,"onStop");
        createContent();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d(Constants.LOGD,"onDestroyView");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(Constants.LOGD,"onDestroy");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.d(Constants.LOGD,"onDetach");
    }

    // HELPER

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        if(onItemClickListener!=null){
            onItemClickListener.onItemClick(l,v,position, id);
        }
        Log.d(Constants.LOGD,"Click");
    }

    public void setOnClickListener(AdapterView.OnItemClickListener onItemClickListener){
        this.onItemClickListener = onItemClickListener;
    }

    private void createContent(){
        try{
            // fill the list
            final TextView textView = new TextView(getActivity());
            String[] content = new String[]{"Brust","Arsch","Rücken", "Beine", "Arme", "Nacken", "Gelenke"};

            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, content);
            setListAdapter(adapter);

        }catch (Exception e){
            Log.d(Constants.LOGD, "Not created yet: " + e);
        }
    }
}
