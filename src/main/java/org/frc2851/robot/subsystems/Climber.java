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

public class Climber extends Subsystem
{
    private static Climber mInstance = new Climber();

    private Climber()
    {
        addComponents(new PneumaticComponent(), new WinchComponent());
    }

    public static Climber getInstance()
    {
        return mInstance;
    }

    public static class PneumaticComponent extends Component
    {
        private DoubleSolenoid mSolenoid;

        public PneumaticComponent()
        {
            super(Climber.class);

            mSolenoid = new DoubleSolenoid(Constants.climberSolenoidForward, Constants.climberSolenoidReverse);

            retract();

            CommandScheduler.getInstance().addTrigger(
                    Constants.climberExtendPneumaticsTrigger,
                    new InstantCommand(this::extend, "extend", this));
            CommandScheduler.getInstance().addTrigger(
                    Constants.climberRetractPneumaticsTrigger,
                    new InstantCommand(this::retract, "retract", this));
        }

        public void extend()
        {
            mSolenoid.set(DoubleSolenoid.Value.kForward);
        }

        public void retract()
        {
            mSolenoid.set(DoubleSolenoid.Value.kReverse);
        }
    }

    public static class WinchComponent extends Component
    {
        private TalonSRX mMotor;

        public WinchComponent()
        {
            super(Climber.class);

            mMotor = MotorControllerFactory.makeTalonSRX(Constants.climberWinchPort);

            CommandScheduler.getInstance().addTrigger(Constants.climberWindWinchTrigger, new InstantCommand(() -> mMotor.set(ControlMode.PercentOutput, 1.0), "wind", this));
            CommandScheduler.getInstance().addTrigger(Constants.climberUnwindWinchTrigger, new InstantCommand(() -> mMotor.set(ControlMode.PercentOutput, -1.0), "unwind", this));
            setDefaultCommand(new InstantCommand(() -> mMotor.set(ControlMode.PercentOutput, 0.0), "stop", this));
        }
    }
}
