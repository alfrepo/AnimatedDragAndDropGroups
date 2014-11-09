package de.mine.experiments.anim.animatedgroup.command;

/**
 * Created by skip on 18.10.2014.
 */
public interface Command {
    void execute();
    void undo();
    void cancel();
    boolean isRunning();

    void addOnExecutionStartedListener(ListenerCommand l);
    void removeOnExecutionStartedListener(ListenerCommand l);

    void addOnUndoStartedListener(ListenerCommand l);
    void removeOnUndoStartedListener(ListenerCommand l);

    void addOnExecutionSucessfullyFinishedListener(ListenerCommand l);
    void removeOnExecutionSuccessfullyFinishedListener(ListenerCommand l);

    void addOnUndoFinishedListener(ListenerCommand l);
    void removeOnUndoFinishedListener(ListenerCommand l);

    void addOnExecutionCanceled(ListenerCommand l);
    void removeOnExecutionCanceled(ListenerCommand l);


    public interface ListenerCommand {
        void onTrigger();
    }
}
