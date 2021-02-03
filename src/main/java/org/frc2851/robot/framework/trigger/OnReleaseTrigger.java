package org.frc2851.robot.framework.trigger;

import java.util.function.BooleanSupplier;

public class OnReleaseTrigger extends Trigger
{
    private boolean mLastRawState = false;

    public OnReleaseTrigger(BooleanSupplier condition)
    {
        super(condition);
    }

    public boolean get()
    {
        boolean returnValue = !mCondition.getAsBoolean() && mLastRawState;
        mLastRawState = mCondition.getAsBoolean();
        return returnValue;
    }
}
