package pwcg.mission.io;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import pwcg.campaign.Campaign;
import pwcg.campaign.context.PWCGDirectorySimulatorManager;
import pwcg.core.config.ConfigItemKeys;
import pwcg.core.config.ConfigManagerGlobal;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.FileUtils;
import pwcg.core.utils.PWCGLogger;
import pwcg.core.utils.PWCGLogger.LogLevel;
import pwcg.gui.dialogs.HelpDialog;

public class MissionFileBinaryBuilder implements buildCommandPath
{
    public static void buildMissionBinaryFile(Campaign campaign, String fileName) throws PWCGException, InterruptedException
    {
        String fullCommand = "";
        try
        {
            if (canRunResaver())
            {
                List<String> commandParts = createCommandParts(campaign, fileName);
                fullCommand = formatCommandForLog(commandParts);
                buildBinaryFile(commandParts, fullCommand);
            }
        }
        catch (PWCGException pwcge)
        {
            new  HelpDialog(pwcge.getMessage());
        }
        catch (Exception e)
        {
            new  HelpDialog("Failed to create binary mission file for " + fullCommand + ". Error: " + e.getMessage());
        }
    }

    private static List<String> createCommandParts(Campaign campaign, String fileName) throws PWCGException
    {
        List<String> commandParts = new ArrayList<>();
        commandParts.add(formResaverExeCommand());
        commandParts.add("-t");
        commandParts.add("-d");
        commandParts.add(formDataDirArg(campaign));
        commandParts.add("-f");
        commandParts.add(formMissionFilePathArg(campaign, fileName));
        return commandParts;
    }
    
    private static void buildBinaryFile(List<String> commandParts, String fullCommand) throws PWCGException
    {
        try
        {
            File workingDir = new File(PWCGDirectorySimulatorManager.getInstance().getMissionRewritePath());
            ProcessBuilder processBuilder = new ProcessBuilder(commandParts);
            processBuilder.directory(workingDir);
            Process process = processBuilder.start();
            int binaryBuildTimeout = ConfigManagerGlobal.getInstance().getIntConfigParam(ConfigItemKeys.BuildBinaryTimeoutKey);
            boolean status = process.waitFor(binaryBuildTimeout, TimeUnit.MINUTES);
            if (status == true)
            {
                PWCGLogger.log(LogLevel.INFO, "Succeeded creating binary mission file for: " + fullCommand);
            }
            else
            {
                PWCGLogger.log(LogLevel.INFO, "Failed to create binary mission file for: " + fullCommand);
                new  HelpDialog("Failed to create binary mission file for " + fullCommand + ". Error: Unknown error");
            }
        }
        catch (IOException ioe)
        {
            new  HelpDialog("Failed to create binary mission file for " + fullCommand + ". Error: " + ioe.getMessage());
        }
        catch (InterruptedException ioe)
        {
            new  HelpDialog("Timed out trying to create binary mission file for " + fullCommand);
        }
    }

    public static boolean canRunResaver() throws PWCGException
    {
        String binPath = PWCGDirectorySimulatorManager.getInstance().getMissionBinPath();
        if (!FileUtils.findInDirectory(binPath, "resaver"))
        {
            return false;
        }
        
        String resaverPath = PWCGDirectorySimulatorManager.getInstance().getMissionRewritePath();
        if (!FileUtils.findInDirectory(resaverPath, "MissionResaver.exe"))
        {
            return false;
        }

        return true;
    }

    private static String formResaverExeCommand() throws PWCGException
    {
        Path resaverExePath = Paths.get(PWCGDirectorySimulatorManager.getInstance().getMissionRewritePath(), "MissionResaver.exe");
        return resaverExePath.toString();
    }

    private static String formDataDirArg(Campaign campaign) throws PWCGException
    {
        String dataDir = PWCGDirectorySimulatorManager.getInstance().getSimulatorDataDir();
        Path dataPath = Paths.get(dataDir);
        String normalized = dataPath.normalize().toString();
        if (normalized.endsWith(File.separator))
        {
            normalized = normalized.substring(0, normalized.length() - 1);
        }
        return normalized;
    }

    private static String formMissionFilePathArg(Campaign campaign, String fileName) throws PWCGException
    {
        String filepath = PWCGDirectorySimulatorManager.getInstance().getMissionFilePath(campaign);
        Path missionPath = Paths.get(filepath, fileName + ".mission");
        return missionPath.normalize().toString();
    }

    private static String formatCommandForLog(List<String> commandParts)
    {
        StringBuilder builder = new StringBuilder();
        for (String part : commandParts)
        {
            if (builder.length() > 0)
            {
                builder.append(' ');
            }
            if (part.contains(" "))
            {
                builder.append('"').append(part).append('"');
            }
            else
            {
                builder.append(part);
            }
        }
        PWCGLogger.log(LogLevel.INFO, builder.toString());
        return builder.toString();
    }

}
