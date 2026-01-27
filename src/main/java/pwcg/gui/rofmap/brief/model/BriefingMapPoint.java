package pwcg.gui.rofmap.brief.model;

import lombok.Getter;
import lombok.Setter;
import pwcg.core.location.Coordinate;

@Getter
@Setter
public class BriefingMapPoint
{
    private static long masterNewWaypointId = 1000000;

    private long waypointID = 0;

    private Coordinate position;
    private int cruisingSpeed;
    private int distanceToNextPoint;
    private boolean editable = true;
    private boolean isTarget = false;
    private boolean isWaypoint = false;
    private String desc;

    public BriefingMapPoint(long waypointID)
    {
        this.waypointID = waypointID;
        ++masterNewWaypointId;
    }

    public BriefingMapPoint copy()
    {
        BriefingMapPoint copy = new BriefingMapPoint(masterNewWaypointId);
        copy.position = this.position.copy();
        copy.cruisingSpeed = this.cruisingSpeed;
        copy.distanceToNextPoint = 0;
        copy.editable = this.editable;
        copy.isTarget = this.isTarget;
        copy.isWaypoint = this.isWaypoint;
        copy.desc = this.desc;
        return copy;
    }
}
