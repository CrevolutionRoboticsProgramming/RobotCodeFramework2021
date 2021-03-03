package org.frc2851.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import org.frc2851.robot.Constants;
import org.frc2851.robot.framework.Component;
import org.frc2851.robot.framework.Subsystem;
import org.frc2851.robot.framework.command.CommandScheduler;
import org.frc2851.robot.framework.command.InstantCommand;
import org.frc2851.robot.util.MotorControllerFactory;

public class ExampleSubsystem extends Subsystem
{
    private static ExampleSubsystem mInstance = new ExampleSubsystem();

    private ExampleSubsystem()
    {
        addComponents(new Component1(), new Component2());
    }

    public static ExampleSubsystem getInstance()
    {
        return mInstance;
    }

    private static class Component1 extends Component
    {
        private TalonSRX mMotor;

        public Component1()
        {
            super(ExampleSubsystem.class);

            mMotor = MotorControllerFactory.makeTalonSRX(Constants.ExampleSubsystem.Component1.motorPort);

            setDefaultCommand(new InstantCommand(() -> mMotor.set(ControlMode.PercentOutput, Constants.ExampleSubsystem.Component1.motorControlAxis.get()),
                    "direct drive", this));
        }
    }

    private static class Component2 extends Component
    {
        private DoubleSolenoid mSolenoid;

        public Component2()
        {
            super(ExampleSubsystem.class);

            mSolenoid = new DoubleSolenoid(Constants.ExampleSubsystem.Component2.solenoidForwardChannel, Constants.ExampleSubsystem.Component2.solenoidReverseChannel);

            CommandScheduler.getInstance().addTrigger(
                    Constants.ExampleSubsystem.Component2.solenoidTrigger,
                    new InstantCommand(() -> mSolenoid.set(DoubleSolenoid.Value.kForward), "extend solenoid", this));

            CommandScheduler.getInstance().addTrigger(
                    Constants.ExampleSubsystem.Component2.solenoidTrigger.negate(),
                    new InstantCommand(() -> mSolenoid.set(DoubleSolenoid.Value.kReverse), "retract solenoid", this));
        }
    }
}
