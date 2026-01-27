package pwcg.campaign.skirmish;

import lombok.Getter;
import pwcg.campaign.tank.PwcgRole;
import pwcg.mission.flight.FlightTypes;
import pwcg.mission.target.TargetType;

@Getter
public class SkirmishProfileElement
{
    private SkirmishProfileAirAssociation association;
    private PwcgRole role;
    private FlightTypes preferredFlightType;
    private TargetType targetType;
}
