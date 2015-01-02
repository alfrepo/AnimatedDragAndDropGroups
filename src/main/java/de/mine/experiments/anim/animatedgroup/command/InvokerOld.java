package de.mine.experiments.anim.animatedgroup.command;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by skip on 19.10.2014.
 */
public class InvokerOld {
    public static List<Command> undoStack = Collections.synchronizedList(new ArrayList<Command>());
    public static List<Command> redoStack = Collections.synchronizedList(new ArrayList<Command>());


    public static Command executeCommmand(Command command){
        // start execution
        undoStack.add(command);

        // clear all on new command
        redoStack.clear();

        // start
        command.execute();

        return command;
    }

    public static Command undoLastCommmand(){
        if(undoStack.isEmpty()){
            return null;
        }
        // remove last from undo stack
        Command command = undoStack.remove(undoStack.size()-1);

        // prepend command
        redoStack.add(0, command);

        // do the undo
        command.undo();

        // return
        return command;
    }

    public static Command redoLastCommmand(){
        // start execution

        if(redoStack.isEmpty()){
            return null;
        }

        // remove last first from redo stack
        Command command = redoStack.remove(0);

        // append it to the undo stack
        undoStack.add(command);

        /* execute command again.
           it is assumed, that the command remembers it's inbetween state, if the undo was interrupted and goes on now.       */
        command.execute();

        return command;
    }
}
