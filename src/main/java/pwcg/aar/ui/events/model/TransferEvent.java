package pwcg.aar.ui.events.model;

import java.util.Date;

import lombok.Getter;
import pwcg.campaign.Campaign;

@Getter
public class TransferEvent extends AARCrewMemberEvent
{
    private int transferFrom;
    private int transferTo;
    private int leaveTime = 0;
	
    public TransferEvent(Campaign campaign, int transferFrom, int transferTo, int leaveTime, int crewMemberSerialNumber, Date date, boolean isNewsWorthy)
    {
        super(campaign, transferTo, crewMemberSerialNumber, date, isNewsWorthy);
        this.transferFrom = transferFrom;
        this.transferTo = transferTo;
        this.leaveTime = leaveTime;
    }
}
