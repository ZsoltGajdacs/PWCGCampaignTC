package pwcg.coop.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CoopPersonaOld
{
    private String username;
    private String campaignName;
    private String crewMemberName;
    private String crewMemberRank;
    private int serialNumber;
    private int companyId;
    private boolean approved;
    private String note;
}
