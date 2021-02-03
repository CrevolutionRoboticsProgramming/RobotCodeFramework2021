package org.frc2851.robot.framework.trigger;

import java.util.function.BooleanSupplier;

public class RawTrigger extends Trigger
{
    public RawTrigger(BooleanSupplier condition)
    {
        super(condition);
    }

    @Override
    public boolean get()
    {
        return mCondition.getAsBoolean();
    }
}
