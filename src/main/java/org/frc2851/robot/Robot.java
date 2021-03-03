package org.frc2851.robot;

import edu.wpi.first.wpilibj.RobotBase;
import edu.wpi.first.wpilibj.TimedRobot;
import org.frc2851.robot.framework.command.CommandScheduler;
import org.frc2851.robot.subsystems.ExampleSubsystem;

public final class Robot extends TimedRobot
{
    public static void main(String... args)
    {
        RobotBase.startRobot(Robot::new);
    }

    @Override
    public void robotInit()
    {
        CommandScheduler.getInstance().addSubsystem(ExampleSubsystem.getInstance());
    }

    @Override
    public void robotPeriodic()
    {
        CommandScheduler.getInstance().run();
    }

    @Override
    public void disabledInit()
    {
    }

    @Override
    public void disabledPeriodic()
    {
    }

    @Override
    public void autonomousInit()
    {
    }

    @Override
    public void autonomousPeriodic()
    {
    }

    @Override
    public void teleopInit()
    {
    }

    @Override
    public void teleopPeriodic()
    {
    }

    @Override
    public void testPeriodic()
    {
    }
}