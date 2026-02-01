package pwcg.campaign.io.json;

import java.io.File;

import pwcg.campaign.Campaign;
import pwcg.campaign.context.CampaignFrontLines;
import pwcg.campaign.context.CampaignFrontLinesData;
import pwcg.campaign.context.PWCGDirectoryUserManager;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.PWCGPath;

public class CampaignFrontLinesIOJson
{
    private static final String FRONTLINES_FILE = "CampaignFrontLines.json";

    public static void writeJson(Campaign campaign) throws PWCGException
    {
        CampaignFrontLines campaignFrontLines = campaign.getCampaignFrontLines();
        if (campaignFrontLines == null)
        {
            return;
        }

        String campaignDir = PWCGDirectoryUserManager.getInstance().getPwcgCampaignsDir()
                + campaign.getCampaignData().getName() + File.separator;

        PwcgJsonWriter<CampaignFrontLinesData> jsonWriter = new PwcgJsonWriter<>();
        jsonWriter.writeAsJson(campaignFrontLines.toData(), campaignDir, FRONTLINES_FILE);
    }

    public static void readJson(Campaign campaign) throws PWCGException
    {
        String campaignDir = PWCGDirectoryUserManager.getInstance().getPwcgCampaignsDir()
                + campaign.getCampaignData().getName() + File.separator;

        File frontLineFile = new File(PWCGPath.normalize(campaignDir + File.separator + FRONTLINES_FILE));
        if (!frontLineFile.exists())
        {
            return;
        }

        JsonObjectReader<CampaignFrontLinesData> jsonReader = new JsonObjectReader<>(CampaignFrontLinesData.class);
        CampaignFrontLinesData data = jsonReader.readJsonFile(campaignDir, FRONTLINES_FILE);
        CampaignFrontLines campaignFrontLines = CampaignFrontLines.fromData(data);
        campaign.setCampaignFrontLines(campaignFrontLines);
    }
}