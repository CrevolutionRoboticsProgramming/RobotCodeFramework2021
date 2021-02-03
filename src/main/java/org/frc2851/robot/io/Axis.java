package org.frc2851.robot.io;

public class Axis
{
    private AxisID mID;
    private AxisShaper mShaper;
    private Controller mController;

    public Axis(Controller controller, AxisID id, AxisShaper shaper)
    {
        mID = id;
        mShaper = shaper;
        mController = controller;
    }

    public Axis(Controller controller, AxisID id)
    {
        this(controller, id, (input) -> input);
    }

    public double get()
    {
        return mShaper.shape(mController.get(this));
    }

    public int getID()
    {
        return mID.getID();
    }

    public enum AxisID
    {
        LEFT_X(0), LEFT_Y(1), LEFT_TRIGGER(2), RIGHT_TRIGGER(3), RIGHT_X(4), RIGHT_Y(5),
        GUITAR_WHAMMY(4), GUITAR_EFFECTS_SWITCH(2);

        private int mID;

        AxisID(int id)
        {
            mID = id;
        }

        public int getID()
        {
            return mID;
        }
    }

    public interface AxisShaper
    {
        double shape(double input);
    }
}
