package pwcg.aar.ui.events.model;

import java.util.Date;

import lombok.Getter;
import pwcg.campaign.Campaign;
import pwcg.campaign.crewmember.Victory;

@Getter
public class VictoryEvent extends AARCrewMemberEvent
{
    private Victory victory;

    public VictoryEvent(Campaign campaign, Victory victory, int companyId, int crewMemberSerialNumber, Date date, boolean isNewsWorthy)
    {
        super(campaign, companyId, crewMemberSerialNumber, date, isNewsWorthy);
        this.victory = victory;
    }
}
