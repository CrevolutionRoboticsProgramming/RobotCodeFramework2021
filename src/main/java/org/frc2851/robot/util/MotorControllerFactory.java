package org.frc2851.robot.util;

import com.ctre.phoenix.motorcontrol.can.TalonFX;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel;

public class MotorControllerFactory
{
    public static TalonFX makeTalonFX(int port)
    {
        TalonFX returnTalon = new TalonFX(port);
        returnTalon.configFactoryDefault();
        return returnTalon;
    }

    public static TalonSRX makeTalonSRX(int port)
    {
        TalonSRX returnTalon = new TalonSRX(port);
        returnTalon.configFactoryDefault();
        //returnTalon.configPeakOutputForward(0.5);
        //returnTalon.configPeakOutputReverse(-0.5);
        return returnTalon;
    }

    public static VictorSPX makeVictorSPX(int port)
    {
        VictorSPX returnVictor = new VictorSPX(port);
        returnVictor.configFactoryDefault();
        return returnVictor;
    }

    public static CANSparkMax makeSparkMax(int port)
    {
        CANSparkMax returnSpark = new CANSparkMax(port, CANSparkMaxLowLevel.MotorType.kBrushless);
        returnSpark.restoreFactoryDefaults();
        return returnSpark;
    }

    public static void configurePIDF(TalonSRX talon, int slot, PID pid)
    {
        configurePIDF(talon, slot, pid.getP(), pid.getI(), pid.getD(), pid.getF());
    }

    public static void configurePIDF(TalonSRX talon, int slot, double p, double i, double d, double f)
    {
        talon.config_kP(slot, p);
        talon.config_kI(slot, i);
        talon.config_kD(slot, d);
        talon.config_kF(slot, f);
    }
}
