package org.frc2851.robot.framework;

import java.util.List;
import java.util.ArrayList;

public abstract class Subsystem
{
    private ArrayList<Component> mComponents;

    public void addComponents(Component... components)
    {
        mComponents = new ArrayList<>(List.of(components));
    }

    public ArrayList<Component> getComponents()
    {
        return mComponents;
    }

    public String getName()
    {
        return getClass().getSimpleName();
    }
}
