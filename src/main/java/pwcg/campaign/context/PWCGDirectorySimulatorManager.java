package pwcg.campaign.context;

import java.io.File;

import pwcg.campaign.Campaign;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.FileUtils;
import pwcg.core.utils.PWCGPath;

public class PWCGDirectorySimulatorManager
{
    private String simulatorRootDir = "";

    private static PWCGDirectorySimulatorManager instance = new PWCGDirectorySimulatorManager();
    
    public static PWCGDirectorySimulatorManager getInstance()
    {
        if (instance.simulatorRootDir.isEmpty())
        {
            instance.createSimulatorDir();
        }
        
        return instance;
    }

    private void createSimulatorDir()
    {
        File rootDir = new File(PWCGPath.getRootDirNoTrailingSlash());
        File simulatorDir = rootDir.getParentFile();
        if (simulatorDir == null)
        {
            simulatorDir = rootDir;
        }
        simulatorRootDir = PWCGPath.ensureTrailingSlash(PWCGPath.normalize(simulatorDir.getAbsolutePath()));
    }

    public String getMissionFilePath(Campaign campaign) throws PWCGException
    {
        String filepath = getSinglePlayerMissionFilePath();
        if (campaign.isCoop())
        {
            filepath = getCoopMissionFilePath();
        }

        return filepath;
    }

    public String getSinglePlayerMissionFilePath() throws PWCGException
    {
        String filepath = PWCGPath.join(getSimulatorDataDir(), "Missions/PWCG/");
        FileUtils.createDirIfNeeded(filepath);
        return filepath;
    }

    public String getCoopMissionFilePath() throws PWCGException
    {
        String filepath = PWCGPath.join(getSimulatorDataDir(), "Multiplayer/Cooperative/");
        FileUtils.createDirIfNeeded(filepath);
        return filepath;
    }

    public String getMissionBinPath() throws PWCGException
    {
        return PWCGPath.join(getSimulatorRootDir(), "bin/");
    }

    public String getMissionRewritePath() throws PWCGException
    {
        return PWCGPath.join(getSimulatorRootDir(), "bin/resaver/");
    }

    public String getSimulatorRootDir()
    {
        return simulatorRootDir;
    }

    public String getSimulatorDataDir()
    {
        return PWCGPath.join(simulatorRootDir, "data/");
    }

    public String getSkinsDir()
    {
        return PWCGPath.join(getSimulatorDataDir(), "graphics/skins/");
    }

}
