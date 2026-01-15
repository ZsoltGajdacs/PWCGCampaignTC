package pwcg.campaign.resupply.personnel;

import lombok.Getter;
import lombok.Setter;
import pwcg.campaign.crewmember.CrewMember;

@Getter
public class TransferRecord
{
    @Setter
    private CrewMember crewMember;
    private int transferFrom;
    private int transferTo;

    public TransferRecord(CrewMember crewMember, int transferFrom, int transferTo)
    {
        this.crewMember  = crewMember;
        this.transferFrom  = transferFrom;
        this.transferTo  = transferTo;
    }
}
