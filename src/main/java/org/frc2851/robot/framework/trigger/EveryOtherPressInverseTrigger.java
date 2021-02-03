package org.frc2851.robot.framework.trigger;

import java.util.function.BooleanSupplier;

public class EveryOtherPressInverseTrigger extends Trigger
{
    private OnPressTrigger mOnPress;
    private boolean mIsOtherPress = true;

    public EveryOtherPressInverseTrigger(BooleanSupplier condition)
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
