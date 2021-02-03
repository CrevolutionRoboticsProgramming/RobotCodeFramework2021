package org.frc2851.robot;

import org.frc2851.robot.framework.trigger.OnPressTrigger;
import org.frc2851.robot.framework.trigger.RawTrigger;
import org.frc2851.robot.framework.trigger.ToggleTrigger;
import org.frc2851.robot.framework.trigger.Trigger;
import org.frc2851.robot.io.Axis;
import org.frc2851.robot.io.Button;
import org.frc2851.robot.io.Controller;
import org.frc2851.robot.util.UDPHandler;

public final class Constants
{
    public static final Controller driverController = new Controller(0);
    public static final Controller operatorController = new Controller(1);

    public static final UDPHandler udpHandler = new UDPHandler(1183);
    public static String driverStationIP = "";
    public static final int sendPort = 1182;

    // Drivetrain
    public static final int drivetrainLeftMasterPort = 1;
    public static final int drivetrainLeftFollowerAPort = 2;
    public static final int drivetrainLeftFollowerBPort = 3;
    public static final int drivetrainRightMasterPort = 4;
    public static final int drivetrainRightFollowerAPort = 5;
    public static final int drivetrainRightFollowerBPort = 6;

    public static final int drivetrainShifterSolenoidForward = 0;
    public static final int drivetrainShifterSolenoidReverse = 7;

    public static final double drivetrainDeadband = 0.15;
    public static final double drivetrainInchesPerRotation = 1.0; // TODO: tune
    public static final double drivetrainP = 0.0; // TODO: tune
    public static final double drivetrainI = 0.0; // TODO: tune
    public static final double drivetrainD = 0.0; // TODO: tune
    public static final double drivetrainTurnP = 0.0; // TODO: tune

    public static final Axis drivetrainThrottleAxis = new Axis(driverController, Axis.AxisID.LEFT_Y, (input) -> -input); // Up on the controller is read as negative BrokeBack
    public static final Axis drivetrainTurnAxis = new Axis(driverController, Axis.AxisID.RIGHT_X);
    public static final ToggleTrigger drivetrainShiftGearTrigger = new ToggleTrigger(new Button(driverController, Button.ButtonID.LEFT_BUMPER)::get);

    // Intake
    public static final int intakeMotorPort = 7;

    public static final int intakeExtendSolenoidForward = 1;
    public static final int intakeExtendSolenoidReverse = 6;

    public static final RawTrigger intakeIntakeTrigger = new RawTrigger(new Button(driverController, Button.ButtonID.RIGHT_TRIGGER)::get);
    public static final RawTrigger intakeOuttakeTrigger = new RawTrigger(new Button(driverController, Button.ButtonID.LEFT_TRIGGER)::get);
    public static final ToggleTrigger intakeExtendTrigger = new ToggleTrigger(new Button(driverController, Button.ButtonID.RIGHT_BUMPER)::get);

    // Indexer
    public static final int indexerSnailMotorPort = 8;
    public static final int indexerElevatorMotorPort = 9;

    public static final RawTrigger indexerFeedShooterTrigger = new RawTrigger(new Button(operatorController, Button.ButtonID.RIGHT_BUMPER)::get);

    /* Shooter */
    public static final double shooterDeadband = 0.2;

    public static final Button shooterEnableVisionTracking = new Button(operatorController, Button.ButtonID.LEFT_TRIGGER);

    //    Turret
    public static final int shooterTurretPort = 11;
    public static final int shooterTurretAbsoluteEncoderDIOPort = 0;
    public static final int shooterTurretLimitSwitchPort = 2;

    public static final double shooterTurretKP = 0.0; // TODO: tune

    public static final Axis shooterTurretRotateAxis = new Axis(operatorController, Axis.AxisID.RIGHT_X);

    //    Angler
    public static final int shooterAnglerPort = 12;
    public static final int shooterAnglerAbsoluteEncoderDIOPort = 1;
    public static final int shooterAnglerLimitSwitchPort = 3;

    public static final double shooterAnglerKP = 0.0; // TODO: tune

    public static final Axis shooterAnglerAxis = new Axis(operatorController, Axis.AxisID.LEFT_Y);

    //    Launcher
    public static final int shooterLauncherMasterPort = 13;
    public static final int shooterLauncherFollowerPort = 14;

    public static final Axis shooterLauncherDirectDriveShootAxis = new Axis(operatorController, Axis.AxisID.RIGHT_Y);//TRIGGER, (input) -> -input);
    public static final RawTrigger shooterLauncher25PercentShooterTrigger = new RawTrigger(new Button(operatorController, Button.ButtonID.A)::get);
    public static final RawTrigger shooterLauncher50PercentShooterTrigger = new RawTrigger(new Button(operatorController, Button.ButtonID.B)::get);
    public static final RawTrigger shooterLauncher75PercentShooterTrigger = new RawTrigger(new Button(operatorController, Button.ButtonID.X)::get);
    public static final RawTrigger shooterLauncher100PercentShooterTrigger = new RawTrigger(new Button(operatorController, Button.ButtonID.Y)::get);

    // Measured in milliseconds
    public static final double shooterLauncherSpinUpTime = 2000;

    // Disker
    public static final int diskerRotatorPort = 9;

    public static final RawTrigger diskerRotateCounterTrigger = new RawTrigger(new Button(operatorController, Button.ButtonID.A)::get);
    public static final RawTrigger diskerRotateClockwiseTrigger = new RawTrigger(new Button(operatorController, Button.ButtonID.B)::get);
    public static final OnPressTrigger diskerRotateThriceTrigger = new OnPressTrigger(new Button(operatorController, Button.ButtonID.X)::get);

    public static final OnPressTrigger diskerRotateFindTrigger = new OnPressTrigger(new Button(operatorController, Button.ButtonID.Y)::get);

    // Climber
    public static final int climberSolenoidForward = 2;
    public static final int climberSolenoidReverse = 5;
    public static final int climberWinchPort = 15;

    public static final OnPressTrigger climberExtendPneumaticsTrigger = new OnPressTrigger(new Button(driverController, Button.ButtonID.X)::get);
    public static final OnPressTrigger climberRetractPneumaticsTrigger = new OnPressTrigger(new Button(driverController, Button.ButtonID.Y)::get);
    public static final RawTrigger climberWindWinchTrigger = new RawTrigger(new Button(driverController, Button.ButtonID.A)::get);
    public static final RawTrigger climberUnwindWinchTrigger = new RawTrigger(new Button(driverController, Button.ButtonID.B)::get);
}