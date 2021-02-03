package org.frc2851.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.revrobotics.ColorMatch;
import com.revrobotics.ColorSensorV3;
import edu.wpi.first.wpilibj.I2C;
import edu.wpi.first.wpilibj.util.Color;
import org.frc2851.robot.Constants;
import org.frc2851.robot.Robot;
import org.frc2851.robot.framework.Component;
import org.frc2851.robot.framework.Subsystem;
import org.frc2851.robot.framework.command.Command;
import org.frc2851.robot.framework.command.CommandScheduler;
import org.frc2851.robot.framework.command.RunCommand;
import org.frc2851.robot.framework.trigger.RawTrigger;
import org.frc2851.robot.framework.trigger.Trigger;
import org.frc2851.robot.util.MotorControllerFactory;

import java.util.HashMap;

public class Disker extends Subsystem
{
    private static Disker disker = new Disker();

    private Disker()
    {
        addComponents(new DiskerComponent());
    }

    public static Disker getInstance()
    {
        return disker;
    }


    public class DiskerComponent extends Component
    {
        private ColorSensor mColorSensor;
        private Color target = mRed; //Set to look for red by default

        private TalonSRX mRotatorMotator;
        private RotationMode mMode = RotationMode.CONTROL;

        private double mRotationSpeed = 0.15; //Fastest allowed = 82%, Suggested fastest = 25%
        private double mColorFinderSpeed = 0.05; //Take it back now yall

        public DiskerComponent()
        {
            super(Disker.class);

            mRotatorMotator = MotorControllerFactory.makeTalonSRX(Constants.diskerRotatorPort);
            mRotatorMotator.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative);

            mColorSensor = new ColorSensor();

            setDefaultCommand(new RunCommand(this::updateDiskerPeriodic, "rotate disker", this));
            CommandScheduler.getInstance().addTrigger(
                    new RawTrigger(() -> Constants.diskerRotateThriceTrigger.get() && mMode == RotationMode.CONTROL),
                    new Command("disker thrice", false, this)
                    {
                        @Override
                        public void initialize()
                        {
                            //System.out.println("Starting THRICE Mode");
                            mMode = RotationMode.THRICE;
                            mRotatorMotator.setSelectedSensorPosition(0);
                            mRotatorMotator.set(ControlMode.PercentOutput, mRotationSpeed);
                        }

                        @Override
                        public void end()
                        {
                            toControlMode();
                        }

                        @Override
                        public boolean isFinished()
                        {
                            boolean finished;
                            if (Robot.isReal())
                                finished = mRotatorMotator.getSelectedSensorPosition() >= 65536;
                            else
                                finished = mRotatorMotator.getSelectedSensorPosition() >= 65536 * 400;

                            //System.out.println("Thrice mode encoder @ " + mRotatorMotator.getSelectedSensorPosition());
                            //if(finished) System.out.println("THRICE mode completed");
                            return finished;
                        }
                    });

            CommandScheduler.getInstance().addTrigger(
                    new RawTrigger(() -> Constants.diskerRotateFindTrigger.get() && mMode == RotationMode.CONTROL),
                    new Command("disker find", false, this)
                    {
                        @Override
                        public void initialize()
                        {
                            //System.out.println("Starting FIND Mode");
                            mMode = RotationMode.FIND;
                            mRotatorMotator.set(ControlMode.PercentOutput, mColorFinderSpeed);
                        }

                        @Override
                        public void end()
                        {
                            toControlMode();
                        }

                        @Override
                        public boolean isFinished()
                        {
                            boolean finished;
                            if (Robot.isReal())
                                finished = (mColorSensor.isMatched() && mColorSensor.getMatch().equals(target));
                            else
                                finished = true;

                            //if(finished) System.out.println("FIND mode completed");
                            return finished;
                        }
                    });
        }

        public void updateDiskerPeriodic()
        {
            if (mMode != RotationMode.CONTROL) return;
            if ((Constants.diskerRotateCounterTrigger.get() && Constants.diskerRotateClockwiseTrigger.get()) || (!Constants.diskerRotateCounterTrigger.get() && !Constants.diskerRotateClockwiseTrigger.get()))
                mRotatorMotator.set(ControlMode.PercentOutput, 0);
            else if (Constants.diskerRotateCounterTrigger.get())
                mRotatorMotator.set(ControlMode.PercentOutput, -mRotationSpeed);
            else if (Constants.diskerRotateClockwiseTrigger.get())
                mRotatorMotator.set(ControlMode.PercentOutput, mRotationSpeed);
        }


        public void setTargetColor(Color color)
        {
            this.target = color;
        }

        private void toControlMode()
        {
            //System.out.println("Switched back to CONTROL mode");
            mRotatorMotator.set(ControlMode.PercentOutput, 0);
            mMode = RotationMode.CONTROL;
        }
    }


    public static HashMap<Color, Color> mPerpendicularColor = new HashMap<Color, Color>();
    public static final Color mRed = new Color(255, 0, 0);
    public static final Color mGreen = new Color(0, 255, 0);
    public static final Color mBlue = new Color(0, 255, 255);
    public static final Color mYellow = new Color(255, 255, 0);

    static
    {
        mPerpendicularColor.put(mGreen, mYellow);
        mPerpendicularColor.put(mBlue, mRed);
        mPerpendicularColor.put(mYellow, mGreen);
        mPerpendicularColor.put(mRed, mBlue);
    }

    public class ColorSensor
    {
        private I2C.Port mPort;
        private ColorSensorV3 mSensor;
        private ColorMatch mMatch = new ColorMatch();

        public ColorSensor()
        {
            mPort = I2C.Port.kOnboard;
            mSensor = new ColorSensorV3(mPort);
            mMatch.addColorMatch(mRed);
            mMatch.addColorMatch(mGreen);
            mMatch.addColorMatch(mBlue);
            mMatch.addColorMatch(mYellow);
        }


        public boolean isMatched()
        {
            return mMatch.matchClosestColor(mSensor.getColor()).confidence > 0.75;
        }

        public Color getMatch()
        {
            return mMatch.matchClosestColor(mSensor.getColor()).color;
        }

    }


    public enum RotationMode
    {
        CONTROL,
        THRICE,
        FIND;
    }

}