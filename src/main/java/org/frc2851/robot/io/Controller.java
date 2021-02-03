package org.frc2851.robot.io;

import edu.wpi.first.wpilibj.Joystick;

public class Controller
{
    private Joystick mJoystick;
    private double mAxisThreshold = 0.15;
    private double mDeadband = 0.02;

    public Controller(int channel)
    {
        mJoystick = new Joystick(channel);
    }

    public boolean get(Button.ButtonID buttonID)
    {
        switch (buttonID.getButtonType())
        {
            case NORMAL:
                return mJoystick.getRawButton(buttonID.getID());
            case TRIGGER:
                return Math.abs(mJoystick.getRawAxis(buttonID.getID())) > mAxisThreshold;
            case POV:
                return mJoystick.getPOV() == buttonID.getID();
            default:
                return false;
        }
    }

    public double get(Axis axis)
    {
        return deadband(mJoystick.getRawAxis(axis.getID()));
    }

    private double deadband(double input)
    {
        return Math.abs(input) > mDeadband ? input : 0;
    }
}
