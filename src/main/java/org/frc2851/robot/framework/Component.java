package org.frc2851.robot.framework;

import org.frc2851.robot.framework.command.Command;

public abstract class Component
{
    private String mSubsystemName;
    private Command mDefaultCommand;

    public Component(Class<?> subsystem)
    {
        mSubsystemName = subsystem.getSimpleName();
    }

    public void periodic()
    {
    }

    public void setDefaultCommand(Command command)
    {
        mDefaultCommand = command;
    }

    public Command getDefaultCommand()
    {
        return mDefaultCommand;
    }

    public String getName()
    {
        return mSubsystemName + "." + getClass().getSimpleName();
    }
}
