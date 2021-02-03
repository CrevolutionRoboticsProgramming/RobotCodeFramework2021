package org.frc2851.robot.framework.trigger;

import java.util.function.BooleanSupplier;

public class ToggleTrigger extends Trigger
{
    private OnPressTrigger mOnPress;
    private boolean mToggleState = false;

    public ToggleTrigger(BooleanSupplier condition)
    {
        super(condition);

        mOnPress = new OnPressTrigger(mCondition);
    }

    public boolean get()
    {
        if (mOnPress.get())
            mToggleState = !mToggleState;

        return mToggleState;
    }
}
