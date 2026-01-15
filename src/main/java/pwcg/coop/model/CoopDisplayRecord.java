package pwcg.coop.model;

import lombok.Getter;
import lombok.Setter;
import pwcg.campaign.crewmember.CrewMemberStatus;

public class CoopDisplayRecord
{
    @Getter
    @Setter
    private String username = "unknown";
    private String pilorNameAndRank = "unknown";
    @Getter
    @Setter
    private String campaignName = "unknown";
    @Getter
    @Setter
    private String companyName = "unknown";
    @Getter
    @Setter
    private int crewMemberStatus = CrewMemberStatus.STATUS_ACTIVE;
    @Getter
    @Setter
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
