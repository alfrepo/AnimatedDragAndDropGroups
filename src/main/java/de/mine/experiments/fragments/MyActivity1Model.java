package de.mine.experiments.fragments;

import java.util.ArrayList;

/**
 * The model which functions as the data source for the fragments.
 * The model is always reset on configuration change, e.g. turning the tablet around.
 *
 * If the Fragment is set to be persistent on configuration change, the model has to be stored outside the activity,
 * or it will be discarded on configuration change.
 */
public class MyActivity1Model {

    ArrayList<ModelListener> modelListeners = new ArrayList<ModelListener>();

    public String text = "initial Fragment Value";

    public void setText(String text) {
        this.text = text;
        updateModelListeners();
    }

    public String getText() {
        return text;
    }

    void updateModelListeners(){
        for(ModelListener l :modelListeners){
            l.onModelUpdate(this);
        }
    }

    void addModeListener(ModelListener modelListener){
        this.modelListeners.add(modelListener);
    }

    interface ModelListener{
        void onModelUpdate(MyActivity1Model myActivityModel);
    }
}
