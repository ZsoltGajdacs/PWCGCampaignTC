package pwcg.aar.campaign.update;

import java.util.Date;

import pwcg.aar.data.AARContext;
import pwcg.campaign.Campaign;
import pwcg.campaign.context.CampaignFrontLines;
import pwcg.core.exception.PWCGException;

public class CampaignFrontLineUpdater
{
    private final Campaign campaign;
    private final AARContext aarContext;

    public CampaignFrontLineUpdater(Campaign campaign, AARContext aarContext)
    {
        this.campaign = campaign;
        this.aarContext = aarContext;
    }

    public void updateFrontLines() throws PWCGException
    {
        FrontlineAdvanceCalculator calculator = new FrontlineAdvanceCalculator();
        FrontlineAdvanceResult result = calculator.calculate(aarContext);
        if (!result.shouldAdvance())
        {
            return;
        }

        Date frontLineDate = aarContext.getNewDate();
        if (frontLineDate == null)
        {
            frontLineDate = campaign.getDate();
        }

        CampaignFrontLines frontLines = campaign.getOrCreateCampaignFrontLines(frontLineDate);
        frontLines.advanceFrontLines(result.getAdvancingSide(), result.getAdvanceDistanceMeters());
    }
}