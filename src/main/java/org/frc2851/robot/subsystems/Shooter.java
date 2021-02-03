package org.frc2851.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.TalonFXFeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.TalonFX;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import edu.wpi.first.wpilibj.DutyCycleEncoder;
import org.frc2851.robot.Constants;
import org.frc2851.robot.framework.Component;
import org.frc2851.robot.framework.Subsystem;
import org.frc2851.robot.framework.command.CommandScheduler;
import org.frc2851.robot.framework.command.InstantCommand;
import org.frc2851.robot.framework.trigger.OnPressTrigger;
import org.frc2851.robot.framework.trigger.RawTrigger;
import org.frc2851.robot.framework.trigger.Trigger;
import org.frc2851.robot.util.Logger;
import org.frc2851.robot.util.MotorControllerFactory;
import org.frc2851.robot.util.UDPHandler;

public class Shooter extends Subsystem
{
    private static Shooter mInstance = new Shooter();
    private Turret mTurret;
    private Angler mAngler;
    private Launcher mLauncher;

    private Shooter()
    {
        mTurret = new Turret();
        mAngler = new Angler();
        mLauncher = new Launcher();
        addComponents(mTurret, mAngler, mLauncher);
    }

    public static Shooter getInstance()
    {
        return mInstance;
    }

    private static class Turret extends Component
    {
        private TalonSRX mMotor;
        private DutyCycleEncoder mEncoder;
        //private DigitalInput mLimitSwitch;

        public Turret()
        {
            super(Shooter.class);

            mMotor = MotorControllerFactory.makeTalonSRX(Constants.shooterTurretPort);
            mMotor.setNeutralMode(NeutralMode.Brake);

            mEncoder = new DutyCycleEncoder(Constants.shooterTurretAbsoluteEncoderDIOPort);

            //mLimitSwitch = new DigitalInput(Constants.shooterTurretLimitSwitchPort);
/*
            CommandScheduler.getInstance().addTrigger(
                    new OnPressTrigger(mLimitSwitch::get),
                    new InstantCommand(() -> mMotor.setSelectedSensorPosition(0), "zero encoder", this));
*/
            // When we receive the target offset from the RPi, schedule a new command that rotates us to the target with a P controller
            Constants.udpHandler.addReceiver(new UDPHandler.MessageReceiver("X OFFSET:", (message) ->
            {
                if (Constants.shooterEnableVisionTracking.get())
                {
                    CommandScheduler.getInstance().schedule(new InstantCommand(() ->
                    {
                        mMotor.set(ControlMode.PercentOutput, Double.parseDouble(message) * Constants.shooterTurretKP);
                        System.out.println(mEncoder.getDistance());
                    }, "rotate to vision target", this));
                }
            }));

            setDefaultCommand(new InstantCommand(() -> mMotor.set(ControlMode.PercentOutput, deadband(Constants.shooterTurretRotateAxis.get())),
                    "direct drive", this));
        }

        private double deadband(double value)
        {
            return Math.abs(value) > Constants.shooterDeadband ? value : 0;
        }
    }

    private static class Angler extends Component
    {
        private TalonSRX mMotor;
        private DutyCycleEncoder mEncoder;
        //private DigitalInput mLimitSwitch;

        public Angler()
        {
            super(Shooter.class);

            mMotor = MotorControllerFactory.makeTalonSRX(Constants.shooterAnglerPort);
            mMotor.setNeutralMode(NeutralMode.Brake);

            mEncoder = new DutyCycleEncoder(Constants.shooterAnglerAbsoluteEncoderDIOPort);

            //mLimitSwitch = new DigitalInput(Constants.shooterAnglerLimitSwitchPort);
/*
            CommandScheduler.getInstance().addTrigger(
                    new OnPressTrigger(mLimitSwitch::get),
                    new InstantCommand(() -> mMotor.setSelectedSensorPosition(0), "zero encoder", this));
*/
            // When we receive the target offset from the RPi, schedule a new command that angles us to the target with a P controller
            Constants.udpHandler.addReceiver(
                    new UDPHandler.MessageReceiver("Y OFFSET:", (message) ->
                    {
                        if (Constants.shooterEnableVisionTracking.get())
                        {
                            CommandScheduler.getInstance().schedule(new InstantCommand(() ->
                                    mMotor.set(ControlMode.PercentOutput, Double.parseDouble(message) * Constants.shooterAnglerKP), "angle to vision target", this));
                        }
                    }));

            setDefaultCommand(new InstantCommand(() -> mMotor.set(ControlMode.PercentOutput, deadband(Constants.shooterAnglerAxis.get())),
                    "direct drive", this));
        }

        private double deadband(double value)
        {
            return Math.abs(value) > Constants.shooterDeadband ? value : 0;
        }
    }

    private static class Launcher extends Component
    {
        private TalonFX mMasterMotor, mFollowerMotor;

        private long mBeginShootingTime;

        public Launcher()
        {
            super(Shooter.class);

            mMasterMotor = MotorControllerFactory.makeTalonFX(Constants.shooterLauncherMasterPort);
            mMasterMotor.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative);

            mFollowerMotor = MotorControllerFactory.makeTalonFX(Constants.shooterLauncherFollowerPort);
            mFollowerMotor.follow(mMasterMotor);
            mFollowerMotor.setInverted(true);

            CommandScheduler.getInstance().addTrigger(new OnPressTrigger(() -> Constants.shooterLauncherDirectDriveShootAxis.get() != 0.0),
                    new InstantCommand(() -> mBeginShootingTime = System.currentTimeMillis(), "set start shooting time", this));

            mMasterMotor.configSelectedFeedbackSensor(TalonFXFeedbackDevice.IntegratedSensor, 0, 0);

            CommandScheduler.getInstance().addTrigger(new RawTrigger(() -> Constants.shooterLauncherDirectDriveShootAxis.get() != 0.0),
                    new InstantCommand(() ->
                    {
                        launch(Constants.shooterLauncherDirectDriveShootAxis.get() * Math.min((System.currentTimeMillis() - mBeginShootingTime) / Constants.shooterLauncherSpinUpTime, 1.0));
                        Logger.println(Logger.LogLevel.DEBUG, "launcher output", String.valueOf(mMasterMotor.getMotorOutputPercent()));
                    }, "run", this));
            setDefaultCommand(new InstantCommand(this::stop, "stop", this));
        }

        public void launch(double output)
        {
            mMasterMotor.set(ControlMode.PercentOutput, output);
        }

        public void stop()
        {
            mMasterMotor.set(ControlMode.PercentOutput, 0.0);
        }
    }
}
