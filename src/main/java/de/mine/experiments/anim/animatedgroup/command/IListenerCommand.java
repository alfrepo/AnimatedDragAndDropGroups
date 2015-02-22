package de.mine.experiments.anim.animatedgroup.command;

public interface IListenerCommand {
//        void onTrigger();

        void onExecutionStarted();

        void onUndoStarted();

        void onExecutionSucessfullyFinished();

        void onUndoFinished();

        void onExecutionCanceled();
    }