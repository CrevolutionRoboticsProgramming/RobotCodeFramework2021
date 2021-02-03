package org.frc2851.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import org.frc2851.robot.Constants;
import org.frc2851.robot.framework.Component;
import org.frc2851.robot.framework.Subsystem;
import org.frc2851.robot.framework.command.Command;
import org.frc2851.robot.framework.command.CommandScheduler;
import org.frc2851.robot.framework.command.InstantCommand;
import org.frc2851.robot.framework.command.RunCommand;
import org.frc2851.robot.util.MotorControllerFactory;

public class Intake extends Subsystem
{
    private static Intake mInstance = new Intake();

    private Intake()
    {
        addComponents(new RollBar(), new Extender());
    }

    public static Intake getInstance()
    {
        return mInstance;
    }

    public static class RollBar extends Component
    {
        private TalonSRX mMotor;

        public RollBar()
        {
            super(Intake.class);

            mMotor = MotorControllerFactory.makeTalonSRX(Constants.intakeMotorPort);

            CommandScheduler.getInstance().addTrigger(
                    Constants.intakeIntakeTrigger,
                    new InstantCommand(this::intake, "intake", this));
            CommandScheduler.getInstance().addTrigger(
                    Constants.intakeOuttakeTrigger,
                    new InstantCommand(this::outtake, "outtake", this));
            setDefaultCommand(new RunCommand(this::stop, "stop", this));
        }

        private void intake()
        {
            mMotor.set(ControlMode.PercentOutput, 1.0);
        }

        private void outtake()
        {
            mMotor.set(ControlMode.PercentOutput, -1.0);
        }

        private void stop()
        {
            mMotor.set(ControlMode.PercentOutput, 0.0);
        }
    }

    public static class Extender extends Component
    {
        private DoubleSolenoid mExtenderSolenoid;

        public Extender()
        {
            super(Intake.class);

            mExtenderSolenoid = new DoubleSolenoid(Constants.intakeExtendSolenoidForward, Constants.intakeExtendSolenoidReverse);

            CommandScheduler.getInstance().addTrigger(
                    Constants.intakeExtendTrigger,
                    new Command("extend", true, this)
                    {
                        private long mLastMessageSend = 0;

                        @Override
                        public void initialize()
                        {
                            mExtenderSolenoid.set(DoubleSolenoid.Value.kReverse);

                            if (System.currentTimeMillis() - mLastMessageSend >= 1000)
                            {
                                Constants.udpHandler.sendTo("INTAKE-EXTEND", Constants.driverStationIP, Constants.sendPort);
                                mLastMessageSend = System.currentTimeMillis();
                            }
                        }

                        @Override
                        public boolean isFinished()
                        {
                            return true;
                        }
                    });
            CommandScheduler.getInstance().addTrigger(
                    Constants.intakeExtendTrigger.negate(),
                    new Command("retract", true, this)
                    {
                        private long mLastMessageSend = 0;

                        @Override
                        public void initialize()
                        {
                            mExtenderSolenoid.set(DoubleSolenoid.Value.kForward);

                            if (System.currentTimeMillis() - mLastMessageSend >= 1000)
                            {
                                Constants.udpHandler.sendTo("INTAKE-RETRACT", Constants.driverStationIP, Constants.sendPort);
                                mLastMessageSend = System.currentTimeMillis();
                            }
                        }

                        @Override
                        public boolean isFinished()
                        {
                            return true;
                        }
                    });
        }
    }
}
