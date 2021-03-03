package org.frc2851.robot.framework.command;

import edu.wpi.first.wpilibj.DriverStation;
import org.frc2851.robot.framework.Component;
import org.frc2851.robot.framework.Subsystem;
import org.frc2851.robot.framework.trigger.Trigger;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;

public final class CommandScheduler
{
    private static CommandScheduler mInstance = new CommandScheduler();

    private ArrayList<Subsystem> mSubsystems;
    private HashMap<Trigger, Command> mCommands;
    private ArrayList<Command> mScheduledCommands;

    private CommandScheduler()
    {
        mSubsystems = new ArrayList<>();
        mCommands = new HashMap<>();
        mScheduledCommands = new ArrayList<>();
    }

    public static CommandScheduler getInstance()
    {
        return mInstance;
    }

    public void addSubsystem(Subsystem... subsystems)
    {
        mSubsystems.addAll(List.of(subsystems));
    }

    public void addTrigger(Trigger trigger, Command command)
    {
        if (trigger != null && command != null)
            mCommands.put(trigger, command);
    }

    public void schedule(Command command)
    {
        mScheduledCommands.add(command);
    }

    public void run()
    {
        if (DriverStation.getInstance().isDisabled())
        {
            for (Command command : mScheduledCommands)
            {
                command.end();
                command.setState(Command.State.NOT_STARTED);
            }
            mScheduledCommands.clear();
            return;
        }

        // If the trigger was tripped and no other command has the same requirements, schedule the command
        for (HashMap.Entry<Trigger, Command> pair : mCommands.entrySet())
        {
            if (pair.getKey().get())
            {
                boolean componentNotBeingUsed = true;

                Iterator<Command> scheduledCommandsIterator = mScheduledCommands.iterator();
                while (scheduledCommandsIterator.hasNext())
                {
                    Command scheduledCommand = scheduledCommandsIterator.next();
                    if (pair.getValue() == scheduledCommand)
                        continue;

                    for (Component requiredComponent : pair.getValue().getRequirements())
                    {
                        for (Component usedComponent : scheduledCommand.getRequirements())
                        {
                            if (requiredComponent == usedComponent)
                            {
                                if (scheduledCommand.isInterruptible())
                                {
                                    scheduledCommand.end();
                                    scheduledCommand.setState(Command.State.NOT_STARTED);
                                    scheduledCommandsIterator.remove();
                                } else
                                {
                                    componentNotBeingUsed = false;
                                    break;
                                }
                            }
                        }
                    }
                }
                if (componentNotBeingUsed)
                    schedule(pair.getValue());
            }
        }

        for (Subsystem subsystem : mSubsystems)
        {
            for (Component component : subsystem.getComponents())
            {
                component.periodic();

                boolean componentIsNotBeingUsed = true;
                for (Command command : mScheduledCommands)
                {
                    for (Component componentInUse : command.getRequirements())
                    {
                        if (componentInUse == component)
                        {
                            componentIsNotBeingUsed = false;
                            break;
                        }
                    }
                }

                if (componentIsNotBeingUsed && component.getDefaultCommand() != null)
                    schedule(component.getDefaultCommand());
            }
        }

        Iterator<Command> scheduledCommandsIterator = mScheduledCommands.iterator();
        while (scheduledCommandsIterator.hasNext())
        {
            Command command = scheduledCommandsIterator.next();

            if (command.getState() == Command.State.NOT_STARTED)
                command.initialize();

            command.execute();
            command.setState(Command.State.EXECUTING);

            if (command.isFinished())
            {
                command.end();
                command.setState(Command.State.NOT_STARTED);
                scheduledCommandsIterator.remove();
            }
        }
    }
}
