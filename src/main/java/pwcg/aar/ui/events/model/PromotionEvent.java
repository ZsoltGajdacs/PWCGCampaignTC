package pwcg.aar.ui.events.model;

import java.util.Date;

import lombok.Getter;
import pwcg.campaign.Campaign;

@Getter
public class PromotionEvent extends AARCrewMemberEvent
{
    private String oldRank = "";
    private String newRank = "";
    private String promotingGeneral = "";

    public PromotionEvent(Campaign campaign, String oldRank, String newRank, String promotingGeneral, int companyId, int crewMemberSerialNumber, Date date, boolean isNewsWorthy)
    {
        super(campaign, companyId, crewMemberSerialNumber, date, isNewsWorthy);
        this.oldRank = oldRank;
        this.newRank = newRank;
        this.promotingGeneral = promotingGeneral;
    }
}
