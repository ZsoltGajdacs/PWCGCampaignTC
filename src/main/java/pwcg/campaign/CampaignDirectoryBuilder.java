package pwcg.campaign;

import java.io.File;

import pwcg.core.utils.FileUtils;

public class CampaignDirectoryBuilder
{
    public static void initializeCampaignDirectories(Campaign campaign) 
    {
        String campaignCombatReportsDir = campaign.getCampaignPathAutoCreateDirectory() + "CombatReports" + File.separator;
        FileUtils.createDirIfNeeded(campaignCombatReportsDir);

        String campaignConfigDir = campaign.getCampaignPathAutoCreateDirectory() + "config" + File.separator;
        FileUtils.createDirIfNeeded(campaignConfigDir);
        
        String campaignEquipmentDir = campaign.getCampaignPathAutoCreateDirectory() + "Equipment" + File.separator;
        FileUtils.createDirIfNeeded(campaignEquipmentDir);
        
        String campaignMissionDataDir = campaign.getCampaignPathAutoCreateDirectory() + "MissionData" + File.separator;
        FileUtils.createDirIfNeeded(campaignMissionDataDir);
        
        String campaignPersonnelDir = campaign.getCampaignPathAutoCreateDirectory() + "Personnel" + File.separator;
        FileUtils.createDirIfNeeded(campaignPersonnelDir);
    }
}
