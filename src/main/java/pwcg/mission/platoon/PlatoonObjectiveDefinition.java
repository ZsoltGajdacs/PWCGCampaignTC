package pwcg.mission.platoon;

import java.util.List;

import lombok.Getter;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.utils.MathUtils;

@Getter
public class PlatoonObjectiveDefinition
{
    private PlatoonMissionType missionType;
    private Coordinate startPosition;
    private List<Coordinate> objectivePositions;
    
    public PlatoonObjectiveDefinition(PlatoonMissionType missionType, Coordinate startPosition, List<Coordinate> objectivePositions)
    {
        this.missionType = missionType;
        this.startPosition = startPosition;
        this.objectivePositions = objectivePositions;
    }

    public Coordinate getStartPosition()
    {
        return startPosition.copy();
    }

    public double calcAngleToObjective() throws PWCGException
    {
        return MathUtils.calcAngle(startPosition, objectivePositions.get(objectivePositions.size()-1));
    }
}
