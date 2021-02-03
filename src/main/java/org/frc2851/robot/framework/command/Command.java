package org.frc2851.robot.framework.command;

import org.frc2851.robot.framework.Component;

import java.util.List;
import java.util.ArrayList;

public abstract class Command
{
    private String mName;
    private State mState = State.NOT_STARTED;
    private boolean mIsInterruptible;
    private ArrayList<Component> mRequirements;

    public Command(String name, boolean isInterruptible, Component... requirements)
    {
        mName = name;
        mIsInterruptible = isInterruptible;
        mRequirements = new ArrayList<>(List.of(requirements));
    }

    public void initialize()
    {
    }

    public void execute()
    {
    }

    public void end()
    {
    }

    public boolean isFinished()
    {
        return false;
    }

    public boolean isInterruptible()
    {
        return mIsInterruptible;
    }

    public final String getName()
    {
        return mName;
    }

    public final ArrayList<Component> getRequirements()
    {
        return mRequirements;
    }

    public void setState(State state)
    {
        mState = state;
    }

    public State getState()
    {
        return mState;
    }

    public enum State
    {
        NOT_STARTED, EXECUTING
    }
}
