package pwcg.campaign.personnel;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Rank
{
    private int rankId = 0;
    private String rankName = "";
    private String rankAbbrev = "";
    private int rankCountry = 0;
    private int rankService = 0;
}
