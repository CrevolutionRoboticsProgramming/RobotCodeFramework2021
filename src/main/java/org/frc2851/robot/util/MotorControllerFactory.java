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
}
