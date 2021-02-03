package org.frc2851.robot.framework.trigger;

import java.util.function.BooleanSupplier;

public abstract class Trigger
{
    protected BooleanSupplier mCondition;

    public Trigger(BooleanSupplier condition)
    {
        mCondition = condition;
    }

    public abstract boolean get();

    public Trigger negate()
    {
        return new Trigger(mCondition)
        {
            @Override
            public boolean get()
            {
                return !Trigger.this.get();
            }
        };
    }
}
