package pwcg.aar.ui.events.model;

import java.util.Date;

import lombok.Getter;
import pwcg.campaign.Campaign;

@Getter
public class ClaimDeniedEvent extends AARCrewMemberEvent
{
	private String type = "";
	
    public ClaimDeniedEvent(Campaign campaign, String aircraftType, int companyId, int crewMemberSerialNumber, Date date, boolean isNewsWorthy)
    {
        super(campaign, companyId, crewMemberSerialNumber, date, isNewsWorthy);
        this.type = aircraftType;
    }
}
