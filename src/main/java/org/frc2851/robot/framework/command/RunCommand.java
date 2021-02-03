package org.frc2851.robot.framework.command;

import org.frc2851.robot.framework.Component;

public class RunCommand extends Command
{
    private Runnable mToRun;

    public RunCommand(Runnable toRun, String name, boolean isInterruptible, Component... requirements)
    {
        super(name, isInterruptible, requirements);
        mToRun = toRun;
    }

    public RunCommand(Runnable toRun, String name, Component... requirements)
    {
        this(toRun, name, true, requirements);
    }

    @Override
    public void execute()
    {
        mToRun.run();
    }
}
