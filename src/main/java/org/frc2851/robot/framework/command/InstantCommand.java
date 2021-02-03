package org.frc2851.robot.framework.command;

import org.frc2851.robot.framework.Component;

public class InstantCommand extends Command
{
    private Runnable mToRun;

    public InstantCommand(Runnable toRun, String name, Component... requirements)
    {
        super(name, true, requirements);
        mToRun = toRun;
    }

    @Override
    public void initialize()
    {
        mToRun.run();
    }

    @Override
    public boolean isFinished()
    {
        return true;
    }
}
