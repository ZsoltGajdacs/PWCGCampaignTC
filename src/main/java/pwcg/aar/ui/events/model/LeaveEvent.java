package pwcg.aar.ui.events.model;

import java.util.Date;

import lombok.Getter;
import pwcg.campaign.Campaign;

@Getter
public class LeaveEvent extends AARCrewMemberEvent
{
    private int leaveTime = 0;
	
    public LeaveEvent(Campaign campaign, int leaveTime, int companyId, int crewMemberSerialNumber, Date date, boolean isNewsWorthy)
    {
        super(campaign, companyId, crewMemberSerialNumber, date, isNewsWorthy);
        this.leaveTime = leaveTime;
    }
}
