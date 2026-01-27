package pwcg.mission.flight.waypoint.patterns;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;

import pwcg.campaign.api.Side;
import pwcg.core.location.Coordinate;
import pwcg.mission.Mission;

@Getter
@Setter
public class PathAlongFrontData
{
    private Mission mission;
    private Coordinate targetGeneralLocation;
    private Date date;
    private Side side = Side.ALLIED;
    private int pathDistance = 15000;
    private int offsetTowardsEnemy = 0;
    private boolean returnAlongRoute = false;
}
