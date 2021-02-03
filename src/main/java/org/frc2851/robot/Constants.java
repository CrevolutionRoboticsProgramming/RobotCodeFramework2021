package org.frc2851.robot;

import org.frc2851.robot.framework.trigger.ToggleTrigger;
import org.frc2851.robot.io.Axis;
import org.frc2851.robot.io.Button;
import org.frc2851.robot.io.Controller;

public final class Constants
{
    public static final Controller driverController = new Controller(0);
    public static final Controller operatorController = new Controller(1);

    public static final class ExampleSubsystem
    {
        public static final class Component1
        {
            public static final int motorPort = 0;
            public static final Axis motorControlAxis = new Axis(driverController, Axis.AxisID.LEFT_Y);
        }

        public static final class Component2
        {
            public static final int solenoidForwardChannel = 0;
            public static final int solenoidReverseChannel = 1;
            public static final ToggleTrigger solenoidTrigger = new ToggleTrigger(new Button(operatorController, Button.ButtonID.A)::get);
        }
    }
}