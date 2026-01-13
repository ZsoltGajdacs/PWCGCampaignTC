package pwcg.gui.maingui;

import java.io.File;
import java.io.IOException;

import pwcg.campaign.context.PWCGDirectoryProductManager;
import pwcg.campaign.context.PWCGDirectoryUserManager;
import pwcg.campaign.context.PWCGProduct;
import pwcg.core.utils.FileUtils;
import pwcg.core.utils.PWCGPath;

public class PwcgDirectoryRestructure
{
    public static void main(String[] args)
    {
        try
        {
            PwcgDirectoryRestructure.restructureDirectories(PWCGProduct.TC);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public static void restructureDirectories(PWCGProduct product) throws IOException
    {
        String pwcgRootDir = PWCGPath.getRootDir();
        File newUserDir = new File(PWCGPath.normalize(PWCGPath.join(pwcgRootDir, "User/")));
        if (newUserDir.exists())
        {
            return;
        }
        createNewUserDirectories();

        moveCampaignContents();
        moveUserConfigContents(product);
        moveCoopDirectoryContents();
        moveAudioFiles();
    }

    private static void createNewUserDirectories()
    {
        PWCGDirectoryUserManager.getInstance();
    }

    private static void moveCampaignContents() throws IOException
    {
        String pwcgRootDir = PWCGPath.getRootDir();
        String oldDir = PWCGPath.join(pwcgRootDir, "Campaigns/");
        String newDir = PWCGDirectoryUserManager.getInstance().getPwcgCampaignsDir();

        FileUtils.copyDirectory(new File(oldDir), new File(newDir));
        FileUtils.deleteRecursive(oldDir);
    }

    private static void moveUserConfigContents(PWCGProduct product) throws IOException
    {
        PWCGDirectoryProductManager directoryProductManager = new PWCGDirectoryProductManager(product);
        String oldDir = PWCGPath.join(directoryProductManager.getPwcgProductDataDir(), "User/");
        String newDir = PWCGDirectoryUserManager.getInstance().getPwcgUserConfigDir();

        FileUtils.copyDirectory(new File(oldDir), new File(newDir));
        FileUtils.deleteRecursive(oldDir);
    }

    private static void moveCoopDirectoryContents() throws IOException
    {
        String pwcgRootDir = PWCGPath.getRootDir();
        String oldDir = PWCGPath.join(pwcgRootDir, "Coop/Users/");
        String newDir = PWCGDirectoryUserManager.getInstance().getPwcgCoopDir();

        FileUtils.copyDirectory(new File(oldDir), new File(newDir));
        String oldCoopDir = PWCGPath.join(pwcgRootDir, "Coop/");
        FileUtils.deleteRecursive(oldCoopDir);
    }

    private static void moveAudioFiles() throws IOException
    {
        String pwcgRootDir = PWCGPath.getRootDir();
        String oldDir = PWCGPath.join(pwcgRootDir, "Audio/");
        String newDir = PWCGDirectoryUserManager.getInstance().getPwcgAudioDir();

        FileUtils.copyDirectory(new File(oldDir), new File(newDir));
        FileUtils.deleteRecursive(oldDir);
    }
}
