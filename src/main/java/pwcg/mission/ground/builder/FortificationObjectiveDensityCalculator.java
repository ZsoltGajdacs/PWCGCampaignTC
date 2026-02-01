package pwcg.mission.ground.builder;

import java.util.List;

import pwcg.campaign.Campaign;
import pwcg.campaign.api.Side;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.group.Block;
import pwcg.campaign.group.Bridge;
import pwcg.campaign.group.BridgeFinder;
import pwcg.campaign.group.RailroadStationFinder;
import pwcg.campaign.group.TownFinder;
import pwcg.campaign.group.airfield.Airfield;
import pwcg.campaign.group.AirfieldManager;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.location.PWCGLocation;
import pwcg.mission.Mission;
import pwcg.mission.MissionObjective;

public class FortificationObjectiveDensityCalculator
{
    private static final int DENSITY_RADIUS = 30000;
    private static final int SMALL_MAX = 3;
    private static final int MEDIUM_MAX = 7;

    public static FortificationSize determineFortificationSize(Mission mission) throws PWCGException
    {
        int density = calculateObjectiveDensity(mission);
        if (density <= SMALL_MAX)
        {
            return FortificationSize.SMALL;
        }
        else if (density <= MEDIUM_MAX)
        {
            return FortificationSize.MEDIUM;
        }

        return FortificationSize.LARGE;
    }

    public static int calculateObjectiveDensity(Mission mission) throws PWCGException
    {
        MissionObjective objective = mission.getObjective();
        if (objective == null)
        {
            return 0;
        }

        Coordinate referenceCoordinate = objective.getPosition();
        Side defendingSide = objective.getDefendingCountry() != null ? objective.getDefendingCountry().getSide() : Side.ALLIED;
        Campaign campaign = mission.getCampaign();

        int density = 0;
        density += countRailroadObjectives(campaign, defendingSide, referenceCoordinate);
        density += countAirfieldObjectives(campaign, defendingSide, referenceCoordinate);
        density += countTownObjectives(campaign, defendingSide, referenceCoordinate);
        density += countBridgeObjectives(campaign, defendingSide, referenceCoordinate);

        return density;
    }

    private static int countRailroadObjectives(Campaign campaign, Side defendingSide, Coordinate referenceCoordinate) throws PWCGException
    {
        int count = 0;
        RailroadStationFinder railroadFinder = PWCGContext.getInstance().getCurrentMap().getGroupManager().getRailroadStationFinder();
        List<Block> railBlocks = railroadFinder.getTrainPositionWithinRadiusBySide(defendingSide, campaign.getDate(), referenceCoordinate, DENSITY_RADIUS);
        for (Block block : railBlocks)
        {
            if (isCloseToFront(campaign, block.getPosition(), defendingSide))
            {
                ++count;
            }
        }
        return count;
    }

    private static int countAirfieldObjectives(Campaign campaign, Side defendingSide, Coordinate referenceCoordinate) throws PWCGException
    {
        int count = 0;
        AirfieldManager airfieldManager = PWCGContext.getInstance().getCurrentMap().getAirfieldManager();
        List<Airfield> airfields = airfieldManager.getAirfieldsWithinRadiusBySide(defendingSide, campaign.getDate(), referenceCoordinate, DENSITY_RADIUS);
        for (Airfield airfield : airfields)
        {
            if (isCloseToFront(campaign, airfield.getPosition(), defendingSide))
            {
                ++count;
            }
        }
        return count;
    }

    private static int countTownObjectives(Campaign campaign, Side defendingSide, Coordinate referenceCoordinate) throws PWCGException
    {
        int count = 0;
        TownFinder townFinder = PWCGContext.getInstance().getCurrentMap().getGroupManager().getTownFinder();
        List<PWCGLocation> towns = townFinder.findTownsForSideWithinRadius(defendingSide, campaign.getDate(), referenceCoordinate, DENSITY_RADIUS);
        for (PWCGLocation town : towns)
        {
            if (isCloseToFront(campaign, town.getPosition(), defendingSide))
            {
                ++count;
            }
        }
        return count;
    }

    private static int countBridgeObjectives(Campaign campaign, Side defendingSide, Coordinate referenceCoordinate) throws PWCGException
    {
        int count = 0;
        BridgeFinder bridgeFinder = PWCGContext.getInstance().getCurrentMap().getGroupManager().getBridgeFinder();
        List<Bridge> bridges = bridgeFinder.findBridgesForSideWithinRadius(defendingSide, campaign.getDate(), referenceCoordinate, DENSITY_RADIUS);
        for (Bridge bridge : bridges)
        {
            if (isCloseToFront(campaign, bridge.getPosition(), defendingSide))
            {
                ++count;
            }
        }
        return count;
    }

    private static boolean isCloseToFront(Campaign campaign, Coordinate position, Side defendingSide) throws PWCGException
    {
        double distance = campaign.getFrontLinesForCampaign(campaign.getDate()).findClosestFriendlyPositionDistance(position, defendingSide);
        return distance < 20000;
    }
}
