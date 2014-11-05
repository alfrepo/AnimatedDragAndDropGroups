package de.mine.experiments.anim.animatedgroup.command;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by skip on 18.10.2014.
 */
public abstract class AbstractCommand implements Command {

    private List<ListenerCommand> listenerOnUndoStarts = Collections.synchronizedList(new ArrayList<ListenerCommand>());
    private List<ListenerCommand> listenerOnExecutionStarts = Collections.synchronizedList(new ArrayList<ListenerCommand>());
    private List<ListenerCommand> listenerOnUndoFinishs = Collections.synchronizedList(new ArrayList<ListenerCommand>());
    private List<ListenerCommand> listenerOnExecutionSucessfullyFinishs = Collections.synchronizedList(new ArrayList<ListenerCommand>());
    private List<ListenerCommand> listenerOnExecutionCanceled = Collections.synchronizedList(new ArrayList<ListenerCommand>());

    @Override
    public void addOnExecutionStartedListener(ListenerCommand l) {
        listenerOnExecutionStarts.add(l);
    }

    @Override
    public void removeOnExecutionStartedListener(ListenerCommand l) {
        listenerOnExecutionStarts.remove(l);
    }

    @Override
    public void addOnUndoStartedListener(ListenerCommand l) {
        listenerOnUndoStarts.add(l);
    }

    @Override
    public void removeOnUndoStartedListener(ListenerCommand l) {
        listenerOnUndoStarts.remove(l);
    }

    @Override
    public void addOnExecutionSucessfullyFinishedListener(ListenerCommand l) {
        listenerOnExecutionSucessfullyFinishs.add(l);
    }

    @Override
    public void addOnUndoFinishedListener(ListenerCommand l) {
        listenerOnUndoFinishs.add(l);
    }

    @Override
    public void removeOnExecutionSuccessfullyFinishedListener(ListenerCommand l) {
        listenerOnExecutionSucessfullyFinishs.remove(l);
    }

    @Override
    public void removeOnUndoFinishedListener(ListenerCommand l) {
        listenerOnUndoFinishs.remove(l);
    }



    @Override
    public void addOnExecutionCanceled(ListenerCommand l) {
        listenerOnExecutionCanceled.add(l);
    }

    @Override
    public void removeOnExecutionCanceled(ListenerCommand l) {
        listenerOnExecutionCanceled.remove(l);
    }


    protected void notifyOnUndoFinishsListener(){
        for(ListenerCommand l:listenerOnUndoFinishs){
            l.onTrigger();
        }
    }

    protected void notifyOnExecutionSuccessfullyFinishsListener(){
        for(ListenerCommand l: listenerOnExecutionSucessfullyFinishs){
            l.onTrigger();
        }
    }

    protected void notifyOnExecutionCanceledListener(){
        for(ListenerCommand l:listenerOnExecutionCanceled ){
            l.onTrigger();
        }
    }

    protected void notifyOnExecutionStartsListener(){
        for(ListenerCommand l:listenerOnExecutionStarts){
            l.onTrigger();
        }
    }

    protected void notifyOnUndoStartsListener(){
        for(ListenerCommand l:listenerOnUndoStarts){
            l.onTrigger();
        }
    }
}
