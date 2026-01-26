package pwcg.mission.ground.org;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pwcg.campaign.api.Side;
import pwcg.core.exception.PWCGException;

public class GroundUnitCollectionTargetFinder
{
    private GroundUnitCollection groundUnitCollection;
    private Map<GroundUnitType, List<IGroundUnit>> groundUnitsForSideByUnitType = new HashMap<>();

    public GroundUnitCollectionTargetFinder(GroundUnitCollection groundUnitCollection)
    {
        this.groundUnitCollection = groundUnitCollection;
    }

    public IGroundUnit findTargetUnit() throws PWCGException
    {
        buildGroundUnitMapForAllUnits();
        return findTargetPosition();
    }
    
    public IGroundUnit findTargetUnit(Side side) throws PWCGException
    {
        buildGroundUnitsForSide(side);
        return findTargetPosition();
    }

    private IGroundUnit findTargetPosition() throws PWCGException
    {
        if (groundUnitCollection.getGroundUnitCollectionType() == GroundUnitCollectionType.INFANTRY_GROUND_UNIT_COLLECTION)
        {
            return findInfantryTargetUnit();
        }
        else if (groundUnitCollection.getGroundUnitCollectionType() == GroundUnitCollectionType.BALLOON_GROUND_UNIT_COLLECTION)
        {
            return findBalloonTargetUnit();
        }
        else if (groundUnitCollection.getGroundUnitCollectionType() == GroundUnitCollectionType.TRANSPORT_GROUND_UNIT_COLLECTION)
        {
            return findTransportTargetUnit();
        }
        else if (groundUnitCollection.getGroundUnitCollectionType() == GroundUnitCollectionType.STATIC_GROUND_UNIT_COLLECTION)
        {
            return findStaticTargetUnit();
        }
        else 
        {
            throw new PWCGException("No target unit found for unknown ground collection type " + groundUnitCollection.getGroundUnitCollectionType());
        }
    }
    
    private void buildGroundUnitMapForAllUnits() throws PWCGException
    {
        for (IGroundUnit groundUnit : groundUnitCollection.getGroundUnits())
        {
            buildGroundUnitsForUnitSet(groundUnit);
        }
    }

    private void buildGroundUnitsForSide(Side side) throws PWCGException
    {
        List<IGroundUnit> groundUnitsForSide = groundUnitCollection.getGroundUnitsForSide(side);
        for (IGroundUnit groundUnit : groundUnitsForSide)
        {
            buildGroundUnitsForUnitSet(groundUnit);
        }
    }

    private void buildGroundUnitsForUnitSet(IGroundUnit groundUnit)
    {
        if (!groundUnitsForSideByUnitType.containsKey(groundUnit.getGroundUnitType()))
        {
            List<IGroundUnit> groundUnitsForType = new ArrayList<>();
            groundUnitsForSideByUnitType.put(groundUnit.getGroundUnitType(), groundUnitsForType);
        }
        List<IGroundUnit> groundUnitsForType = groundUnitsForSideByUnitType.get(groundUnit.getGroundUnitType());
        groundUnitsForType.add(groundUnit);
    }

    private IGroundUnit findInfantryTargetUnit() throws PWCGException
    {
        if (groundUnitsForSideByUnitType.containsKey(GroundUnitType.TANK_UNIT))
        {
            return getFirstUnitOfType(GroundUnitType.TANK_UNIT);
        }
        else if (groundUnitsForSideByUnitType.containsKey(GroundUnitType.INFANTRY_UNIT))
        {
            return getFirstUnitOfType(GroundUnitType.INFANTRY_UNIT);
        }
        else if (groundUnitsForSideByUnitType.containsKey(GroundUnitType.ARTILLERY_UNIT))
        {
            return getFirstUnitOfType(GroundUnitType.ARTILLERY_UNIT);
        }
        else if (groundUnitsForSideByUnitType.containsKey(GroundUnitType.AAA_UNIT))
        {
            return getFirstUnitOfType(GroundUnitType.AAA_UNIT);
        }
        else if (groundUnitsForSideByUnitType.containsKey(GroundUnitType.TRANSPORT_UNIT))
        {
            return getFirstUnitOfType(GroundUnitType.TRANSPORT_UNIT);
        }
        else
        {
            throw new PWCGException("No target unit found for infantry ground collection");
        }
    }

    private IGroundUnit findBalloonTargetUnit() throws PWCGException
    {
        if (groundUnitsForSideByUnitType.containsKey(GroundUnitType.BALLOON_UNIT))
        {
            return getFirstUnitOfType(GroundUnitType.BALLOON_UNIT);
        }
        else
        {
            throw new PWCGException("No target unit found for balloon ground collection");
        }
    }

    private IGroundUnit findTransportTargetUnit() throws PWCGException
    {
        if (groundUnitsForSideByUnitType.containsKey(GroundUnitType.TRANSPORT_UNIT))
        {
            return getFirstUnitOfType(GroundUnitType.TRANSPORT_UNIT);
        }
        else
        {
            throw new PWCGException("No target unit found for transport ground collection");
        }
    }

    private IGroundUnit findStaticTargetUnit() throws PWCGException
    {
        if (groundUnitsForSideByUnitType.containsKey(GroundUnitType.STATIC_UNIT))
        {
            return getFirstUnitOfType(GroundUnitType.STATIC_UNIT);
        }
        else
        {
            throw new PWCGException("No target unit found for static ground collection");
        }
    }

    private IGroundUnit getFirstUnitOfType(GroundUnitType unitType) throws PWCGException
    {
        List<IGroundUnit> units = groundUnitsForSideByUnitType.get(unitType);
        if (units == null || units.isEmpty())
        {
            throw new PWCGException("No units available for type: " + unitType);
        }
        return units.get(0);
    }
}

