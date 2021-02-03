package org.frc2851.robot.util;

public class Logger
{
    private static int longestLabelLength = 0;

    public static void println(LogLevel level, String label, String message)
    {
        if (label.length() > longestLabelLength)
            longestLabelLength = label.length();

        if (label.length() < longestLabelLength)
            label = " ".repeat(longestLabelLength - label.length()).concat(label);

        System.out.println(level.toString() + label + ": " + message);
    }

    public static void println(LogLevel level, String message)
    {
        println(level, "", message);
    }

    public enum LogLevel
    {
        DEBUG(""), WARNING("WARNING : "), ERROR("ERROR : ");

        private String mLabel;

        LogLevel(String label)
        {
            mLabel = label;
        }

        @Override
        public String toString()
        {
            return mLabel;
        }
    }
}
