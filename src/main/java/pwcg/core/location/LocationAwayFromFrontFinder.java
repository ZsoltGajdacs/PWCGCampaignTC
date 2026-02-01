package pwcg.core.location;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import pwcg.campaign.Campaign;
import pwcg.campaign.api.Side;
import pwcg.campaign.context.FrontLinesForMap;
import pwcg.campaign.context.PWCGContext;
import pwcg.core.exception.PWCGException;

public class LocationAwayFromFrontFinder
{
    
    public static List<PWCGLocation> getLocationsAwayFromFront(List<PWCGLocation> possibleLocations, Side side, Date date) throws PWCGException
    {
        List<PWCGLocation> locationsAwayFromFront = new ArrayList<>();
        Campaign campaign = PWCGContext.getInstance().getCampaign();
        FrontLinesForMap frontlines = (campaign != null) ? campaign.getFrontLinesForCampaign(date)
            : PWCGContext.getInstance().getCurrentMap().getFrontLinesForMap(date);
        for (PWCGLocation raidTarget : possibleLocations)
        {
            if (frontlines.isFarFromFront(raidTarget.getPosition(), side, date))
            {
                locationsAwayFromFront.add(raidTarget);
            }
        }
        
        if (locationsAwayFromFront.size() < 2)
        {
            return possibleLocations;
        }
        else
        {
            return locationsAwayFromFront;
        }
    }
}
