package org.frc2851.robot;

import badlog.lib.BadLog;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.RobotBase;
import edu.wpi.first.wpilibj.TimedRobot;
import org.frc2851.robot.framework.command.CommandScheduler;
import org.frc2851.robot.subsystems.*;
import org.frc2851.robot.util.Logger;
import org.frc2851.robot.util.UDPHandler;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Optional;

public final class Robot extends TimedRobot
{
    private double mLastGameDataSend = 0;
    private boolean mFirstGameDataSend = true;

    private BadLog mBadLog;

    public static void main(String... args)
    {
        RobotBase.startRobot(Robot::new);
    }

    @Override
    public void robotInit()
    {
        initializeBadLog();

        Constants.udpHandler.addReceiver(new UDPHandler.MessageReceiver("IP:", (message) -> Constants.driverStationIP = message));

        CommandScheduler.getInstance().addSubsystem(Drivetrain.getInstance());
        CommandScheduler.getInstance().addSubsystem(Intake.getInstance());
        CommandScheduler.getInstance().addSubsystem(Indexer.getInstance());
        CommandScheduler.getInstance().addSubsystem(Shooter.getInstance());
        CommandScheduler.getInstance().addSubsystem(Climber.getInstance());
        //CommandScheduler.getInstance().addSubsystem(Disker.getInstance());

        BadLog.createValue("Match Number", String.valueOf(DriverStation.getInstance().getMatchNumber()));
        BadLog.createTopic("Match Time", "s", DriverStation.getInstance()::getMatchTime);
        mBadLog.finishInitialization();
    }

    @Override
    public void robotPeriodic()
    {
        String gameData;
        gameData = DriverStation.getInstance().getGameSpecificMessage();
        if (gameData.length() > 0 && System.currentTimeMillis() - mLastGameDataSend >= 1000)
        {
            if (mFirstGameDataSend)
            {
                Logger.println(Logger.LogLevel.DEBUG, "COLOR: " + gameData);
                mFirstGameDataSend = false;
            }
            if (!Constants.driverStationIP.equals(""))
                Constants.udpHandler.sendTo("COLOR:" + gameData.charAt(0), Constants.driverStationIP, Constants.sendPort);
            mLastGameDataSend = System.currentTimeMillis();
        }

        CommandScheduler.getInstance().run();
    }

    private void initializeBadLog()
    {
        LocalDateTime localDateTime = new Timestamp(System.currentTimeMillis()).toLocalDateTime();
        String date = localDateTime.toLocalDate().format(DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT))
                .replace("/", "-");
        String time = localDateTime.toLocalTime().format(DateTimeFormatter.ofLocalizedTime(FormatStyle.MEDIUM))
                .replace(":", "-").replace(" AM", "").replace(" PM", "");

        // Makes a new log in the "logs" directory in the directory where the application was executed
        File badLogRootDir = new File(Paths.get((Robot.isReal() ? "/home/lvuser" : System.getProperty("user.dir")) + File.separator + "logs" + File.separator + "badlog").toString());
        File badLogFile = new File(badLogRootDir.getAbsolutePath() + File.separator + date, time + ".bag");

        if (!badLogFile.mkdirs())
        {
            Logger.println(Logger.LogLevel.ERROR, "BadLog", "Could not create log directory " + badLogFile.getAbsolutePath().substring(0, badLogFile.getAbsolutePath().lastIndexOf(File.separator) - 1));
            return;
        }

        try
        {
            // While there are still X or more files in the logs...
            while (Files.walk(badLogRootDir.toPath())
                    .filter(Files::isRegularFile)
                    .count() >= 20)
            {
                // Delete the oldest file
                Optional<Path> stream = Files.walk(badLogRootDir.toPath())
                        .filter(Files::isRegularFile)
                        .min((path1, path2) ->
                        {
                            try
                            {
                                return Files.getLastModifiedTime(path1).compareTo(Files.getLastModifiedTime(path2));
                            } catch (IOException e)
                            {
                                e.printStackTrace();
                            }
                            return 0;
                        });

                if (stream.isPresent())
                {
                    if (!stream.get().toFile().delete())
                    {
                        Logger.println(Logger.LogLevel.ERROR, "BadLog", "Could not delete old log " + stream.get().toAbsolutePath().toString());
                    }
                }
            }

            // Delete any empty folders
            Files.walk(badLogRootDir.toPath())
                    .filter(Files::isDirectory)
                    .forEach((path) ->
                    {
                        String[] list = path.toFile().list();

                        if (list != null && list.length == 0)
                        {
                            if (!path.toFile().delete())
                            {
                                Logger.println(Logger.LogLevel.ERROR, "BadLog", "Could not delete old log folder " + path.toAbsolutePath().toString());
                            }
                        }
                    });
        } catch (IOException e)
        {
            e.printStackTrace();
        }

        try
        {
            if (!badLogFile.createNewFile())
            {
                Logger.println(Logger.LogLevel.ERROR, "BadLog", "Could not create new BadLog " + badLogFile.getAbsolutePath());
                return;
            }
        } catch (IOException e)
        {
            e.printStackTrace();
        }

        mBadLog = BadLog.init(badLogFile.getPath());
    }

    @Override
    public void disabledInit()
    {
    }

    @Override
    public void disabledPeriodic()
    {
    }

    @Override
    public void autonomousInit()
    {
    }

    @Override
    public void autonomousPeriodic()
    {
    }

    @Override
    public void teleopInit()
    {
    }

    @Override
    public void teleopPeriodic()
    {
    }

    @Override
    public void testPeriodic()
    {
    }
}