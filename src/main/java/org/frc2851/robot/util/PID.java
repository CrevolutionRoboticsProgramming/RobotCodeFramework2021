package org.frc2851.robot.util;

public class PID
{
    private double mP, mI, mD, mF;

    public PID(double p, double i, double d, double f)
    {
        this.mP = p;
        this.mI = i;
        this.mD = d;
        this.mF = f;
    }

    public double getP()
    {
        return mP;
    }

    public void setP(double value)
    {
        mP = value;
    }

    public double getI()
    {
        return mI;
    }

    public void setI(double value)
    {
        mI = value;
    }

    public double getD()
    {
        return mD;
    }

    public void setD(double value)
    {
        mD = value;
    }

    public double getF()
    {
        return mF;
    }

    public void setF(double value)
    {
        mF = value;
    }

    public void setPID(double p, double i, double d, double f)
    {
        mP = p;
        mI = i;
        mD = d;
        mF = f;
    }

    public void setPID(double p, double i, double d)
    {
        setPID(p, i, d, 0);
    }

    @Override
    public String toString()
    {
        return "PID[" + mP + ", " + mI + ", " + mD + ", " + mF + "]";
    }
}
