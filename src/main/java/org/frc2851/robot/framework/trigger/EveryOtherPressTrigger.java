package org.frc2851.robot.framework.trigger;

import java.util.function.BooleanSupplier;

public class EveryOtherPressTrigger extends Trigger
{
    private OnPressTrigger mOnPress;
    private boolean mIsOtherPress = false;

    public EveryOtherPressTrigger(BooleanSupplier condition)
    {
        super(condition);

        mOnPress = new OnPressTrigger(mCondition);
    }

    public boolean get()
    {
        if (mOnPress.get())
            mIsOtherPress = !mIsOtherPress;

        return mOnPress.get() && mIsOtherPress;
    }
}
