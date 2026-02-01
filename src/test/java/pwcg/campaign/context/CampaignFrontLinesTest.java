package pwcg.campaign.context;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import pwcg.campaign.api.Side;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;

public class CampaignFrontLinesTest
{
    @Test
    public void shouldCopyBaselineFrontLines() throws PWCGException
    {
        FrontLinesForMap baseline = buildBaselineFrontLines();
        CampaignFrontLines campaignFrontLines = CampaignFrontLines.fromBaseline(baseline, FrontMapIdentifier.MOSCOW_MAP);

        FrontLinePoint baselineAllied = baseline.getFrontLines(Side.ALLIED).get(0);
        Coordinate baselineOriginal = baselineAllied.getPosition();

        baselineAllied.setPosition(new Coordinate(baselineOriginal.getXPos() + 500.0, 0.0, baselineOriginal.getZPos()));

        FrontLinePoint campaignAllied = campaignFrontLines.getFrontLinesForMap().getFrontLines(Side.ALLIED).get(0);
        Assertions.assertEquals(baselineOriginal.getXPos(), campaignAllied.getPosition().getXPos(), 0.01);
        Assertions.assertEquals(baselineOriginal.getZPos(), campaignAllied.getPosition().getZPos(), 0.01);
    }

    @Test
    public void shouldAdvanceFrontLinesInExpectedDirection() throws PWCGException
    {
        CampaignFrontLines campaignFrontLines = CampaignFrontLines.fromBaseline(buildBaselineFrontLines(), FrontMapIdentifier.MOSCOW_MAP);
        FrontLinePoint alliedBefore = campaignFrontLines.getFrontLinesForMap().getFrontLines(Side.ALLIED).get(0);
        FrontLinePoint axisBefore = campaignFrontLines.getFrontLinesForMap().getFrontLines(Side.AXIS).get(0);

        double alliedX = alliedBefore.getPosition().getXPos();
        double axisX = axisBefore.getPosition().getXPos();

        campaignFrontLines.advanceFrontLines(Side.ALLIED, 1000.0);

        FrontLinePoint alliedAfter = campaignFrontLines.getFrontLinesForMap().getFrontLines(Side.ALLIED).get(0);
        FrontLinePoint axisAfter = campaignFrontLines.getFrontLinesForMap().getFrontLines(Side.AXIS).get(0);

        Assertions.assertTrue(alliedAfter.getPosition().getXPos() > alliedX);
        Assertions.assertTrue(axisAfter.getPosition().getXPos() > axisX);
    }

    @Test
    public void shouldRoundTripFrontLinesData() throws PWCGException
    {
        CampaignFrontLines campaignFrontLines = CampaignFrontLines.fromBaseline(buildBaselineFrontLines(), FrontMapIdentifier.MOSCOW_MAP);
        CampaignFrontLinesData data = campaignFrontLines.toData();

        CampaignFrontLines rehydrated = CampaignFrontLines.fromData(data);
        Assertions.assertEquals(FrontMapIdentifier.MOSCOW_MAP, rehydrated.getMapIdentifier());

        FrontLinesForMap rehydratedMap = rehydrated.getFrontLinesForMap();
        Assertions.assertEquals(2, rehydratedMap.getFrontLines(Side.ALLIED).size());
        Assertions.assertEquals(2, rehydratedMap.getFrontLines(Side.AXIS).size());
    }

    private FrontLinesForMap buildBaselineFrontLines() throws PWCGException
    {
        FrontLinesForMap baseline = new FrontLinesForMap(FrontMapIdentifier.MOSCOW_MAP.getMapName());
        baseline.setFrontLinesAllied(makeFrontLinePoints(Side.ALLIED, 0.0));
        baseline.setFrontLinesAxis(makeFrontLinePoints(Side.AXIS, 10000.0));
        return baseline;
    }

    private List<FrontLinePoint> makeFrontLinePoints(Side side, double xPos) throws PWCGException
    {
        List<FrontLinePoint> points = new ArrayList<>();
        points.add(makeFrontLinePoint(side, xPos, 0.0));
        points.add(makeFrontLinePoint(side, xPos, 5000.0));
        return points;
    }

    private FrontLinePoint makeFrontLinePoint(Side side, double xPos, double zPos) throws PWCGException
    {
        FrontLinePoint point = new FrontLinePoint();
        point.setName(side == Side.ALLIED ? FrontLinePoint.ALLIED_FRONT_LINE : FrontLinePoint.AXIS_FRONT_LINE);
        point.setPosition(new Coordinate(xPos, 0.0, zPos));
        return point;
    }
}