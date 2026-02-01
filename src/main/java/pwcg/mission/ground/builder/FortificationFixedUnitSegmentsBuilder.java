package pwcg.mission.ground.builder;

import java.util.ArrayList;
import java.util.List;

import pwcg.core.exception.PWCGException;
import pwcg.mission.Mission;
import pwcg.mission.ground.org.GroundUnitCollection;
import pwcg.mission.ground.org.GroundUnitCollectionData;
import pwcg.mission.ground.org.GroundUnitCollectionType;
import pwcg.mission.ground.org.IGroundUnit;
import pwcg.mission.mcu.Coalition;
import pwcg.mission.target.FrontSegmentDefinition;
import pwcg.mission.target.FrontSegmentDefinitionGenerator;
import pwcg.mission.target.TargetType;

public class FortificationFixedUnitSegmentsBuilder
{
    public static GroundUnitCollection generateFortifications(Mission mission, FortificationSize fortificationSize) throws PWCGException
    {
        GroundUnitCollectionData groundUnitCollectionData = new GroundUnitCollectionData(
                GroundUnitCollectionType.INFANTRY_GROUND_UNIT_COLLECTION, "Fortification", TargetType.TARGET_INFANTRY, Coalition.getCoalitions());

        GroundUnitCollection battleUnitCollection = new GroundUnitCollection(mission.getCampaign(), "Fortification Fixed Units", groundUnitCollectionData);

        int numFixedSegments = FrontFixedUnitSegmentsBuilder.calcNumFixedSegments(mission);

        List<FrontSegmentDefinition> segmentDefinitions = new ArrayList<>();
        for (int i = 0; i < numFixedSegments; ++i)
        {
            FrontSegmentDefinitionGenerator assaultDefinitionGenerator = new FrontSegmentDefinitionGenerator(mission, i);
            FrontSegmentDefinition frontSegmentDefinition = assaultDefinitionGenerator.generateBattleDefinition();
            segmentDefinitions.add(frontSegmentDefinition);
        }

        for (int i = 0; i < segmentDefinitions.size(); ++i)
        {
            int mgCount = getCountForSegment(fortificationSize.getMachineGuns(), numFixedSegments, i);
            int aaCount = getCountForSegment(fortificationSize.getAaGuns(), numFixedSegments, i);
            int atCount = getCountForSegment(fortificationSize.getAntiTankGuns(), numFixedSegments, i);
            int artilleryCount = getCountForSegment(fortificationSize.getArtillery(), numFixedSegments, i);

            List<IGroundUnit> primaryAssaultSegmentGroundUnits = new ArrayList<>();
            GroundUnitCollection fixedBattleSegmentUnits = buildFixedUnits(
                    mission,
                    segmentDefinitions.get(i),
                    fortificationSize,
                    mgCount,
                    aaCount,
                    atCount,
                    artilleryCount,
                    primaryAssaultSegmentGroundUnits);
            battleUnitCollection.merge(fixedBattleSegmentUnits);
        }

        battleUnitCollection.finishGroundUnitCollection();

        return battleUnitCollection;
    }

    private static int getCountForSegment(int totalCount, int segments, int index)
    {
        if (segments <= 0)
        {
            return totalCount;
        }

        int base = totalCount / segments;
        int remainder = totalCount % segments;
        return base + (index < remainder ? 1 : 0);
    }

    private static GroundUnitCollection buildFixedUnits(
            Mission mission,
            FrontSegmentDefinition segmentDefinition,
            FortificationSize fortificationSize,
            int machineGuns,
            int aaGuns,
            int antiTankGuns,
            int artillery,
            List<IGroundUnit> primaryAssaultSegmentGroundUnits) throws PWCGException
    {
        FortificationFixedUnitBuilder fortificationUnitBuilder = new FortificationFixedUnitBuilder(
                mission,
                segmentDefinition,
                fortificationSize,
                machineGuns,
                aaGuns,
                antiTankGuns,
                artillery);
        GroundUnitCollection fortificationUnits = fortificationUnitBuilder.generateFortificationUnits();
        primaryAssaultSegmentGroundUnits.add(fortificationUnits.getPrimaryGroundUnit());
        mission.registerAssault(segmentDefinition);
        return fortificationUnits;
    }
}
