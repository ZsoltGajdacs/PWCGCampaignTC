package pwcg.aar.ui.events.model;

import java.util.Date;

import lombok.Getter;
import pwcg.campaign.Campaign;

@Getter
public class MedalEvent extends AARCrewMemberEvent
{
    private String medal = "";

    public MedalEvent(Campaign campaign, String medal, int companyId, int crewMemberSerialNumber, Date date, boolean isNewsWorthy)
    {
        super(campaign, companyId, crewMemberSerialNumber, date, isNewsWorthy);
        this.medal = medal;
    }
}
