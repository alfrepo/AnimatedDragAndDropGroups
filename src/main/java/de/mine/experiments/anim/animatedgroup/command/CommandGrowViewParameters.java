package de.mine.experiments.anim.animatedgroup.command;

/**
 * Created by skip on 29.12.2014.
 */
public class CommandGrowViewParameters implements ICommandParameters<CommandGrowView>{
    public CommandGrowView.Direction animationDirection;

    @Override
    public void execute(CommandGrowView command) {
        command.execute(animationDirection);
    }
}
