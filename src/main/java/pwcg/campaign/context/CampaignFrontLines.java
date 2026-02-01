package pwcg.campaign.context;

import java.util.ArrayList;
import java.util.List;

import pwcg.campaign.api.Side;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.location.LocationSet;
import pwcg.core.location.PWCGLocation;
import pwcg.core.utils.MathUtils;

public class CampaignFrontLines
{
    private FrontMapIdentifier mapIdentifier;
    private List<FrontLinePoint> frontLinesAllied = new ArrayList<>();
    private List<FrontLinePoint> frontLinesAxis = new ArrayList<>();
    private FrontLinesForMap frontLinesForMap;

    public CampaignFrontLines(FrontMapIdentifier mapIdentifier)
    {
        this.mapIdentifier = mapIdentifier;
    }

    public static CampaignFrontLines fromBaseline(FrontLinesForMap baseline, FrontMapIdentifier mapIdentifier) throws PWCGException
    {
        CampaignFrontLines frontLines = new CampaignFrontLines(mapIdentifier);
        frontLines.frontLinesAllied = copyFrontLines(baseline.getFrontLines(Side.ALLIED));
        frontLines.frontLinesAxis = copyFrontLines(baseline.getFrontLines(Side.AXIS));
        return frontLines;
    }

    public static CampaignFrontLines fromData(CampaignFrontLinesData data) throws PWCGException
    {
        if (data == null)
        {
            return null;
        }

        FrontMapIdentifier mapIdentifier = FrontMapIdentifier.valueOf(data.getMapIdentifier());
        CampaignFrontLines frontLines = new CampaignFrontLines(mapIdentifier);
        frontLines.loadFromLocationSet(data.getFrontLines());
        return frontLines;
    }

    public CampaignFrontLinesData toData()
    {
        CampaignFrontLinesData data = new CampaignFrontLinesData();
        data.setMapIdentifier(mapIdentifier.name());
        data.setFrontLines(buildLocationSet());
        return data;
    }

    public FrontMapIdentifier getMapIdentifier()
    {
        return mapIdentifier;
    }

    public boolean isForMap(FrontMapIdentifier mapIdentifier)
    {
        return this.mapIdentifier == mapIdentifier;
    }

    public FrontLinesForMap getFrontLinesForMap() throws PWCGException
    {
        if (frontLinesForMap == null)
        {
            frontLinesForMap = new FrontLinesForMap(mapIdentifier.getMapName());
            frontLinesForMap.setFrontLinesAllied(frontLinesAllied);
            frontLinesForMap.setFrontLinesAxis(frontLinesAxis);
        }
        return frontLinesForMap;
    }

    public void advanceFrontLines(Side advancingSide, double advanceMeters) throws PWCGException
    {
        if (advanceMeters <= 0 || frontLinesAllied.isEmpty() || frontLinesAxis.isEmpty())
        {
            return;
        }

        List<FrontLinePoint> advancingFront = getFrontLinesForSide(advancingSide);
        List<FrontLinePoint> retreatingFront = getFrontLinesForSide(advancingSide.getOppositeSide());

        shiftFrontLines(advancingFront, retreatingFront, advanceMeters, true);
        shiftFrontLines(retreatingFront, advancingFront, advanceMeters, false);
    }

    private void shiftFrontLines(List<FrontLinePoint> frontLinesToShift, List<FrontLinePoint> referenceLines, double advanceMeters, boolean towardReference) throws PWCGException
    {
        for (FrontLinePoint point : frontLinesToShift)
        {
            FrontLinePoint closestReference = findClosestPoint(referenceLines, point.getPosition());
            if (closestReference == null)
            {
                continue;
            }

            double angleToReference = MathUtils.calcAngle(point.getPosition(), closestReference.getPosition());
            if (!towardReference)
            {
                angleToReference = MathUtils.adjustAngle(angleToReference, 180.0);
            }

            Coordinate newPosition = MathUtils.calcNextCoord(point.getPosition(), angleToReference, advanceMeters);
            point.setPosition(newPosition);
        }
    }

    private FrontLinePoint findClosestPoint(List<FrontLinePoint> frontLinePoints, Coordinate reference) throws PWCGException
    {
        FrontLinePoint closestPoint = null;
        double closestDistance = Double.MAX_VALUE;

        for (FrontLinePoint frontLinePoint : frontLinePoints)
        {
            double distance = MathUtils.calcDist(reference, frontLinePoint.getPosition());
            if (distance < closestDistance)
            {
                closestDistance = distance;
                closestPoint = frontLinePoint;
            }
        }

        return closestPoint;
    }

    private List<FrontLinePoint> getFrontLinesForSide(Side side)
    {
        if (side == Side.ALLIED)
        {
            return frontLinesAllied;
        }

        return frontLinesAxis;
    }

    private void loadFromLocationSet(LocationSet locationSet) throws PWCGException
    {
        frontLinesAllied.clear();
        frontLinesAxis.clear();

        if (locationSet == null)
        {
            return;
        }

        for (PWCGLocation location : locationSet.getLocations())
        {
            FrontLinePoint frontLinePoint = new FrontLinePoint();
            frontLinePoint.setLocation(location);
            if (frontLinePoint.getSide() == Side.ALLIED)
            {
                frontLinesAllied.add(frontLinePoint);
            }
            else if (frontLinePoint.getSide() == Side.AXIS)
            {
                frontLinesAxis.add(frontLinePoint);
            }
        }
    }

    private LocationSet buildLocationSet()
    {
        LocationSet locationSet = new LocationSet("FrontLines");
        addFrontLinesToLocationSet(locationSet, frontLinesAllied);
        addFrontLinesToLocationSet(locationSet, frontLinesAxis);
        return locationSet;
    }

    private void addFrontLinesToLocationSet(LocationSet locationSet, List<FrontLinePoint> frontLines)
    {
        for (FrontLinePoint frontLinePoint : frontLines)
        {
            PWCGLocation location = new PWCGLocation();
            location.setName(frontLinePoint.getName());
            location.setPosition(frontLinePoint.getPosition());
            location.setOrientation(frontLinePoint.getOrientation());
            locationSet.addLocation(location);
        }
    }

    private static List<FrontLinePoint> copyFrontLines(List<FrontLinePoint> source) throws PWCGException
    {
        List<FrontLinePoint> copy = new ArrayList<>();
        for (FrontLinePoint frontLinePoint : source)
        {
            FrontLinePoint copiedPoint = new FrontLinePoint();
            copiedPoint.setName(frontLinePoint.getName());
            copiedPoint.setPosition(frontLinePoint.getPosition());
            copiedPoint.setOrientation(frontLinePoint.getOrientation());
            copy.add(copiedPoint);
        }
        return copy;
    }
}