package de.mine.experiments.anim.animatedgroup.command;

import junit.framework.Assert;

/**
 * Parameters may store command's arguments to execute it later
 * Created by skip on 29.12.2014.
 */
public class CommandGrowViewParameters implements ICommandParameters<CommandGrowView>{
    public CommandGrowView.Direction animationDirection;


    public CommandGrowViewParameters(CommandGrowView.Direction animationDirection){
        Assert.assertNotNull(animationDirection);
        this.animationDirection = animationDirection;
    }

    @Override
    public void execute(CommandGrowView command) {
        command.execute(animationDirection);
    }
}
