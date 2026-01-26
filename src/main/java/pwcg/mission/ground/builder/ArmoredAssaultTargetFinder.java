package pwcg.mission.ground.builder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import pwcg.campaign.api.Side;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.utils.MathUtils;
import pwcg.mission.Mission;
import pwcg.mission.ground.org.GroundUnitCollection;
import pwcg.mission.ground.org.GroundUnitElement;
import pwcg.mission.ground.org.GroundUnitType;
import pwcg.mission.ground.org.IGroundUnit;

public class ArmoredAssaultTargetFinder
{
    private GroundUnitCollection assaultFixedUnitCollection;
    private GroundUnitType groundUnitType;

    public ArmoredAssaultTargetFinder(Mission mission, GroundUnitCollection assaultFixedUnitCollection, GroundUnitType groundUnitType)
    {
        this.groundUnitType = groundUnitType;
        this.assaultFixedUnitCollection = assaultFixedUnitCollection;
    }

    public Coordinate findTargetForTankPlatoon(Coordinate startPosition, Side side) throws PWCGException
    {
        List<IGroundUnit> targetUnitsForSide = assaultFixedUnitCollection.getGroundUnitsByTypeAndSide(groundUnitType, side);
        IGroundUnit targetUnit = findUnitCloseToPosition(targetUnitsForSide, startPosition);
        return targetUnit.getPosition().copy();
    }

    public List<Coordinate> findUnitsCloseToPosition(Coordinate unitPosition, Side side) throws PWCGException
    {
        List<Coordinate> viableTargetUnits = new ArrayList<>();

        List<IGroundUnit> targetUnitsForSide = assaultFixedUnitCollection.getGroundUnitsByTypeAndSide(groundUnitType, side);
        for (IGroundUnit targetUnit : targetUnitsForSide)
        {
            for (GroundUnitElement targetUnitElement : targetUnit.getGroundElements())
            {
                double distanceToPlatoon = MathUtils.calcDist(targetUnitElement.getVehicleStartLocation(), unitPosition);
                if (distanceToPlatoon < 8000)
                {
                    viableTargetUnits.add(targetUnitElement.getVehicleStartLocation());
                }
            }
        }

        return viableTargetUnits;
    }

    private IGroundUnit findUnitCloseToPosition(List<IGroundUnit> targetUnitsForSide, Coordinate objectivePosition) throws PWCGException
    {
        if (targetUnitsForSide == null || targetUnitsForSide.isEmpty())
        {
            throw new PWCGException("No target units available for assault");
        }

        List<IGroundUnit> viableTargetUnits = new ArrayList<>();
        double closeUnitDistance = 4000.0;
        double maxDistance = 50000.0;
        while (viableTargetUnits.isEmpty() && closeUnitDistance < maxDistance)
        {
            for (IGroundUnit targetUnit : targetUnitsForSide)
            {
                double distanceToPlatoon = MathUtils.calcDist(targetUnit.getPosition(), objectivePosition);
                if (distanceToPlatoon < closeUnitDistance)
                {
                    viableTargetUnits.add(targetUnit);
                }
            }

            closeUnitDistance += 1000;
        }

        if (viableTargetUnits.isEmpty())
        {
            throw new PWCGException("No viable target units found within maximum distance");
        }

        Collections.shuffle(viableTargetUnits);
        return viableTargetUnits.get(0);
    }
}
