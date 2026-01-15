package pwcg.coop.model;

import lombok.Getter;
import lombok.Setter;
import pwcg.campaign.crewmember.CrewMemberStatus;

@Getter
@Setter
public class CoopDisplayRecord
{
    private String username = "unknown";
    private String pilorNameAndRank = "unknown";
    private String campaignName = "unknown";
    private String companyName = "unknown";
    private int crewMemberStatus = CrewMemberStatus.STATUS_ACTIVE;
    private int crewMemberSerialNumber = 0;

    public String getCrewMemberNameAndRank()
    {
        return pilorNameAndRank;
    }

    public void setPilorNameAndRank(String pilorNameAndRank)
    {
        this.pilorNameAndRank = pilorNameAndRank;
    }
}
