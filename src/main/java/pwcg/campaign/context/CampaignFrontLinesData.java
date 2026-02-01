package pwcg.campaign.context;

import lombok.Getter;
import lombok.Setter;
import pwcg.core.location.LocationSet;

@Getter
@Setter
public class CampaignFrontLinesData
{
    private String mapIdentifier = "";
    private LocationSet frontLines = new LocationSet("FrontLines");

    public CampaignFrontLinesData()
    {
    }

    public CampaignFrontLinesData(String mapIdentifier, LocationSet frontLines)
    {
        this.mapIdentifier = mapIdentifier;
        this.frontLines = frontLines;
    }
}