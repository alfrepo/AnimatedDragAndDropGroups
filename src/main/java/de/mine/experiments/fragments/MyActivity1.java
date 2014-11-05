package de.mine.experiments.fragments;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


public class MyActivity1 extends ActionBarActivity  implements MyActivity1Model.ModelListener {

    private String TAG = "liveCycle";

    private Fragment1Details fragmentDetails;
    private Fragment1List fragmentList;

    MyActivity1Model myActivityModel = new MyActivity1Model();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d(TAG, "onCreate");

        setContentView(de.mine.experiments.R.layout.activity1_my);

        // initiate the UI from the new Model
        subscribeToModel();

        // create a Fragment programmatically
        fragmentDetails = createDetailsFragmentProgrammatically(myActivityModel);

        // find a fragment by id
        fragmentList = findListFragment();

        // connect the fragments
        wireFragments(fragmentList);

        // set functionality for the backstack popping button
        setUpBackStackButton();

        // set functionality for the fragment creation button
        setUpCreateFragmentButton();

        // set functionality for the fragment deletion button
        setUpRemoveFragmentButton();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d(TAG, "onRestart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy");
    }


    @Override
    public void onModelUpdate(MyActivity1Model myActivityModel) {
        // update the text on model change
        updateTextFragmentTextFromModel(myActivityModel);
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
        if (id == de.mine.experiments.R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void updateTextFragmentTextFromModel(MyActivity1Model myActivityModel){
        fragmentDetails.setContent(myActivityModel.getText());
    }


    private Fragment1Details createDetailsFragmentProgrammatically(MyActivity1Model myActivityModel){
        // get Fragment manager
        FragmentManager fragmentManager = getFragmentManager();

        // ACHTUNG: when using setRetainInstance(true) on a fragment to restore it's vars on configchange - check whether the fragment already exists before recreating it
        this.fragmentDetails = (Fragment1Details) fragmentManager.findFragmentByTag("fragmentDetails");

        if(this.fragmentDetails==null) {
            // start a transaction
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            // create a fragment
            this.fragmentDetails = new Fragment1Details();
            // set the id to the fragment
            // The Fragment should not be recreated when the Application is killed
            this.fragmentDetails.setRetainInstance(true);
            // add the fragment to the view
            fragmentTransaction.add(de.mine.experiments.R.id.fragmentContainer, this.fragmentDetails, "fragmentDetails");
        //        //make transaction reversable by undoing the action on the fragmentmanager
        //        fragmentTransaction.addToBackStack(null);
            // commit the transaction
            fragmentTransaction.commit();

            // init the value from model of fragmentDetails.
            myActivityModel.setText(myActivityModel.getText());
        }else{
            Log.d(TAG,"Fragment already exists. Do not recreate it!");
        }

        return this.fragmentDetails;
    }

    private void removeDetailsFragment(){
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.remove(fragmentDetails);
        fragmentTransaction.commit();
        Log.d(TAG,"Removing fragment "+fragmentDetails);
    }

    private Fragment1List findListFragment(){
        // get Fragment manager
        FragmentManager fragmentManager = getFragmentManager();
        // start a transaction
        Fragment1List fragmentList = (Fragment1List) fragmentManager.findFragmentById(de.mine.experiments.R.id.fragmentList);
        return fragmentList;
    }

    private void wireFragments(final Fragment1List fragmentList){
        fragmentList.setOnClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                myActivityModel.setText("Click on " + ((TextView) view).getText().toString() +" , "+ myActivityModel.getText());
            }
        });
    }

    private void subscribeToModel(){
        myActivityModel.addModeListener(this);
    }

    private void setUpBackStackButton(){
        final Button button = (Button) findViewById(de.mine.experiments.R.id.buttonBackStack);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(),"popBackStack removes the Transaction which added the DetailsFragment", Toast.LENGTH_SHORT).show();
                getFragmentManager().popBackStack();
            }
        });

        FragmentManager.OnBackStackChangedListener backstackListener = new FragmentManager.OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {
                if(getFragmentManager().getBackStackEntryCount()<=0){
                    button.setEnabled(false);
                    button.setActivated(false);
                }else{
                    button.setEnabled(true);
                    button.setActivated(true);
                }
            }
        };
        getFragmentManager().addOnBackStackChangedListener(backstackListener);
        //init the buttonState
        backstackListener.onBackStackChanged();
    }

    private void setUpCreateFragmentButton(){
        final Button button = (Button) findViewById(de.mine.experiments.R.id.buttonCreateFragment);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyActivity1.this.fragmentDetails = createDetailsFragmentProgrammatically(myActivityModel);
            }
        });
    }

    private void setUpRemoveFragmentButton(){
        final Button button = (Button) findViewById(de.mine.experiments.R.id.buttonRemoveFragment);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                removeDetailsFragment();
            }
        });
    }
}
