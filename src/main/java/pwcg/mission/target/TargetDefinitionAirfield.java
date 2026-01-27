package pwcg.mission.target;

import lombok.Getter;
import lombok.Setter;
import pwcg.campaign.api.ICountry;
import pwcg.campaign.group.Block;

@Getter
@Setter
public class TargetDefinitionAirfield
{
    private Block departureStation;
    private ICountry country;
}
