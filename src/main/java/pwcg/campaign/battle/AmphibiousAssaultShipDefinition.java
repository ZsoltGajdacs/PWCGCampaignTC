package pwcg.campaign.battle;

import lombok.Getter;
import pwcg.core.location.Coordinate;
import pwcg.core.location.Orientation;
import pwcg.mission.ground.org.GroundUnitCollection;

@Getter
public class AmphibiousAssaultShipDefinition
{
    private String shipType;
    private Coordinate destination;
    private Orientation orientation;
    private GroundUnitCollection landingCraftGroundUnit;

    public void setGroundUnit(GroundUnitCollection landingCraftGroundUnit)
    {
        this.landingCraftGroundUnit = landingCraftGroundUnit;
    }

}
