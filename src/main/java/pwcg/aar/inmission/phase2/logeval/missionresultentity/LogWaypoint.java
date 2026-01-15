package pwcg.aar.inmission.phase2.logeval.missionresultentity;

import lombok.Getter;
import lombok.Setter;
import pwcg.core.location.Coordinate;

@Getter
@Setter
public class LogWaypoint extends LogBase
{
	protected Coordinate location;

    public LogWaypoint(int sequenceNumber)
    {
        super(sequenceNumber);
    }
}
