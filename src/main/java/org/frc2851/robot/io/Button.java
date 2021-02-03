package org.frc2851.robot.io;

import java.util.List;
import java.util.ArrayList;

public class Button
{
    private Controller mController;
    private ButtonID mID;
    private ArrayList<Button> mComboButtons;

    public Button(Controller controller, ButtonID id, Button... comboButtons)
    {
        mController = controller;
        mID = id;
        mComboButtons = new ArrayList<>(List.of(comboButtons));
    }

    public boolean get()
    {
        boolean allComboButtonsPressed = true;
        for (Button comboButton : mComboButtons)
        {
            if (!comboButton.get())
                allComboButtonsPressed = false;
        }

        return mController.get(mID) && allComboButtonsPressed;
    }

    public ButtonType getButtonType()
    {
        return mID.getButtonType();
    }

    public final int getID()
    {
        return mID.getID();
    }

    public enum ButtonType
    {
        NORMAL, TRIGGER, POV
    }

    public enum ButtonID
    {
        A(1, ButtonType.NORMAL), B(2, ButtonType.NORMAL), X(3, ButtonType.NORMAL), Y(4, ButtonType.NORMAL),
        LEFT_BUMPER(5, ButtonType.NORMAL), RIGHT_BUMPER(6, ButtonType.NORMAL), SELECT(7, ButtonType.NORMAL),
        START(8, ButtonType.NORMAL), LEFT_STICK(9, ButtonType.NORMAL), RIGHT_STICK(10, ButtonType.NORMAL),

        LEFT_TRIGGER(2, ButtonType.TRIGGER), RIGHT_TRIGGER(3, ButtonType.TRIGGER),

        POV_CENTER(-1, ButtonType.POV), POV_UP(0, ButtonType.POV), POV_UP_RIGHT(45, ButtonType.POV), POV_RIGHT(90, ButtonType.POV),
        POV_DOWN_RIGHT(135, ButtonType.POV), POV_DOWN(180, ButtonType.POV), POV_DOWN_LEFT(225, ButtonType.POV),
        POV_LEFT(270, ButtonType.POV), POV_UP_LEFT(-45, ButtonType.POV),

        GUITAR_GREEN(1, ButtonType.NORMAL), GUITAR_RED(2, ButtonType.NORMAL),
        GUITAR_BLUE(3, ButtonType.NORMAL), GUITAR_YELLOW(4, ButtonType.NORMAL),
        GUITAR_ORANGE(5, ButtonType.NORMAL), GUITAR_TOGGLE(6, ButtonType.NORMAL),
        GUITAR_BACK(7, ButtonType.NORMAL), GUITAR_START(8, ButtonType.NORMAL),

        GUITAR_STRUM_NEUTRAL(-1, ButtonType.POV), GUITAR_STRUM_UP(0, ButtonType.POV),
        GUITAR_STRUM_DOWN(180, ButtonType.POV),

        DRUM_GREEN(1, ButtonType.NORMAL), DRUM_RED(2, ButtonType.NORMAL),
        DRUM_BLUE(3, ButtonType.NORMAL), DRUM_YELLOW(4, ButtonType.NORMAL),
        DRUM_PEDAL(5, ButtonType.NORMAL), DRUM_COMBO(10, ButtonType.NORMAL);

        private int mID;
        private ButtonType mButtonType;

        ButtonID(int id, ButtonType buttonType)
        {
            mID = id;
            mButtonType = buttonType;
        }

        public int getID()
        {
            return mID;
        }

        public ButtonType getButtonType()
        {
            return mButtonType;
        }
    }
}
