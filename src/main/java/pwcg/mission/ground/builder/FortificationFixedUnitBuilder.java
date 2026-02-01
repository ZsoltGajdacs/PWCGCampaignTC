package pwcg.mission.ground.builder;

import java.util.Arrays;

import pwcg.campaign.context.PWCGContext;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.utils.MathUtils;
import pwcg.mission.Mission;
import pwcg.mission.ground.GroundUnitInformation;
import pwcg.mission.ground.GroundUnitInformationFactory;
import pwcg.mission.ground.org.GroundUnitCollection;
import pwcg.mission.ground.org.GroundUnitCollectionData;
import pwcg.mission.ground.org.GroundUnitCollectionType;
import pwcg.mission.ground.org.IGroundUnit;
import pwcg.mission.ground.unittypes.infantry.AssaultGroundUnitFactory;
import pwcg.mission.ground.unittypes.staticobject.GroundStaticSceneryUnit;
import pwcg.mission.mcu.Coalition;
import pwcg.mission.target.FrontSegmentDefinition;
import pwcg.mission.target.FrontSegmentDefinitionGenerator;
import pwcg.mission.target.TargetType;

public class FortificationFixedUnitBuilder
{
    private static final double FORTIFICATION_SPREAD = 150.0;
    private static final double FORTIFICATION_DEPTH = 100.0;
    private static final double FORTIFICATION_ARTILLERY_DEPTH = 1200.0;

    private static final String STATIC_MG_POSITION = "mg_position";
    private static final String STATIC_ARTILLERY_POSITION_SMALL = "art_position_small";
    private static final String STATIC_ARTILLERY_POSITION_MEDIUM = "art_position_medium";
    private static final String STATIC_ARTILLERY_POSITION_LARGE = "art_position_big";

    private final Mission mission;
    private final FrontSegmentDefinition segmentDefinition;
    private final FortificationSize fortificationSize;
    private final int machineGuns;
    private final int aaGuns;
    private final int antiTankGuns;
    private final int artillery;
    private final AssaultGroundUnitFactory assaultFactory = new AssaultGroundUnitFactory();

    private final GroundUnitCollection battleSegmentUnitCollection;

    public FortificationFixedUnitBuilder(
            Mission mission,
            FrontSegmentDefinition segmentDefinition,
            FortificationSize fortificationSize,
            int machineGuns,
            int aaGuns,
            int antiTankGuns,
            int artillery)
    {
        this.mission = mission;
        this.segmentDefinition = segmentDefinition;
        this.fortificationSize = fortificationSize;
        this.machineGuns = machineGuns;
        this.aaGuns = aaGuns;
        this.antiTankGuns = antiTankGuns;
        this.artillery = artillery;

        GroundUnitCollectionData groundUnitCollectionData = new GroundUnitCollectionData(
                GroundUnitCollectionType.INFANTRY_GROUND_UNIT_COLLECTION,
                "Fortification Segment",
                TargetType.TARGET_INFANTRY,
                Coalition.getCoalitions());

        this.battleSegmentUnitCollection = new GroundUnitCollection(mission.getCampaign(), "Fortification Segment", groundUnitCollectionData);
    }

    public GroundUnitCollection generateFortificationUnits() throws PWCGException
    {
        createAssaultUnits();
        createFortifiedDefenders();
        return battleSegmentUnitCollection;
    }

    private void createAssaultUnits() throws PWCGException
    {
        assaultingMachineGun();
        assaultingATGunScreen();
        assaultingArtillery();
        assaultingAAAMachineGun();
        assaultingAAAArty();
    }

    private void assaultingMachineGun() throws PWCGException
    {
        Coordinate machineGunStartPosition = MathUtils.calcNextCoord(
                segmentDefinition.getDefensePosition(),
                segmentDefinition.getTowardsAttackerOrientation().getyOri(),
                FrontSegmentDefinitionGenerator.DISTANCE_BETWEEN_COMBATANTS);

        GroundUnitInformation groundUnitInformation = buildAssaultGroundUnitInformation(machineGunStartPosition, TargetType.TARGET_INFANTRY);
        IGroundUnit assaultingMachineGunUnit = assaultFactory.createMachineGunUnit(groundUnitInformation);
        battleSegmentUnitCollection.addGroundUnit(assaultingMachineGunUnit);
    }

    private void assaultingATGunScreen() throws PWCGException
    {
        Coordinate antiTankAssaultPosition = MathUtils.calcNextCoord(
                segmentDefinition.getDefensePosition(),
                segmentDefinition.getTowardsAttackerOrientation().getyOri(),
                FrontSegmentDefinitionGenerator.DISTANCE_BETWEEN_COMBATANTS + FrontSegmentDefinitionGenerator.DISTANCE_FOR_AT_GUNS);

        GroundUnitInformation groundUnitInformation = buildAssaultGroundUnitInformation(antiTankAssaultPosition, TargetType.TARGET_ANTI_TANK);
        IGroundUnit assaultAntiTankUnit = assaultFactory.createAntiTankGunUnit(groundUnitInformation);
        battleSegmentUnitCollection.addGroundUnit(assaultAntiTankUnit);
    }

    private void assaultingArtillery() throws PWCGException
    {
        Coordinate artilleryAssaultPosition = MathUtils.calcNextCoord(
                segmentDefinition.getDefensePosition(),
                segmentDefinition.getTowardsAttackerOrientation().getyOri(),
                FrontSegmentDefinitionGenerator.DISTANCE_BETWEEN_COMBATANTS + FrontSegmentDefinitionGenerator.DISTANCE_FOR_ARTILLERY);

        GroundUnitInformation groundUnitInformation = buildAssaultGroundUnitInformation(artilleryAssaultPosition, TargetType.TARGET_ARTILLERY);
        IGroundUnit assaultArtilleryUnit = assaultFactory.createAssaultArtilleryUnit(groundUnitInformation);
        battleSegmentUnitCollection.addGroundUnit(assaultArtilleryUnit);
    }

    private void assaultingAAAMachineGun() throws PWCGException
    {
        Coordinate aaaMgAssaultPosition = MathUtils.calcNextCoord(
                segmentDefinition.getDefensePosition(),
                segmentDefinition.getTowardsAttackerOrientation().getyOri(),
                FrontSegmentDefinitionGenerator.DISTANCE_BETWEEN_COMBATANTS + FrontSegmentDefinitionGenerator.DISTANCE_FOR_AT_GUNS);

        GroundUnitInformation groundUnitInformation = buildAssaultGroundUnitInformation(aaaMgAssaultPosition, TargetType.TARGET_AAA);
        IGroundUnit assaultAAMachineGunUnit = assaultFactory.createAAMachineGunUnitUnit(groundUnitInformation);
        battleSegmentUnitCollection.addGroundUnit(assaultAAMachineGunUnit);
    }

    private void assaultingAAAArty() throws PWCGException
    {
        Coordinate aaaArtyAssaultPosition = MathUtils.calcNextCoord(
                segmentDefinition.getDefensePosition(),
                segmentDefinition.getTowardsAttackerOrientation().getyOri(),
                FrontSegmentDefinitionGenerator.DISTANCE_BETWEEN_COMBATANTS + FrontSegmentDefinitionGenerator.DISTANCE_FOR_AAA_ARTILLERY);

        GroundUnitInformation groundUnitInformation = buildAssaultGroundUnitInformation(aaaArtyAssaultPosition, TargetType.TARGET_AAA);
        IGroundUnit assaultAAArtilleryUnit = assaultFactory.createAAArtilleryUnitUnit(groundUnitInformation);
        battleSegmentUnitCollection.addGroundUnit(assaultAAArtilleryUnit);
    }

    private void createFortifiedDefenders() throws PWCGException
    {
        buildFortifiedUnits(machineGuns, FortificationUnitType.MACHINE_GUN);
        buildFortifiedUnits(antiTankGuns, FortificationUnitType.ANTI_TANK);
        buildFortifiedUnits(aaGuns, FortificationUnitType.ANTI_AIR);
        buildFortifiedUnits(artillery, FortificationUnitType.ARTILLERY);
    }

    private void buildFortifiedUnits(int count, FortificationUnitType unitType) throws PWCGException
    {
        if (count <= 0)
        {
            return;
        }

        for (int i = 0; i < count; ++i)
        {
            Coordinate unitPosition = buildFortificationPosition(unitType, i, count);
            IGroundUnit unit = createDefensiveUnit(unitType, unitPosition);
            battleSegmentUnitCollection.addGroundUnit(unit);
            addStaticObjectForUnit(unitType, unitPosition);
        }
    }

    private IGroundUnit createDefensiveUnit(FortificationUnitType unitType, Coordinate unitPosition) throws PWCGException
    {
        TargetType targetType = unitType.getTargetType();
        GroundUnitInformation groundUnitInformation = buildDefenseGroundUnitInformation(unitPosition, targetType);
        switch (unitType)
        {
            case MACHINE_GUN:
                return assaultFactory.createMachineGunUnit(groundUnitInformation);
            case ANTI_TANK:
                return assaultFactory.createAntiTankGunUnit(groundUnitInformation);
            case ANTI_AIR:
                return assaultFactory.createAAArtilleryUnitUnit(groundUnitInformation);
            case ARTILLERY:
            default:
                return assaultFactory.createAssaultArtilleryUnit(groundUnitInformation);
        }
    }

    private Coordinate buildFortificationPosition(FortificationUnitType unitType, int index, int totalCount) throws PWCGException
    {
        double baseDistance = FORTIFICATION_DEPTH;
        if (unitType == FortificationUnitType.ARTILLERY)
        {
            baseDistance = FORTIFICATION_ARTILLERY_DEPTH;
        }

        double orientation = segmentDefinition.getTowardsAttackerOrientation().getyOri();
        Coordinate basePosition = MathUtils.calcNextCoord(segmentDefinition.getDefensePosition(), orientation, baseDistance);

        double lateralOffset = 0.0;
        if (totalCount > 1)
        {
            double centerIndex = (totalCount - 1) / 2.0;
            lateralOffset = (index - centerIndex) * FORTIFICATION_SPREAD;
        }

        double lateralAngle = MathUtils.adjustAngle(orientation, lateralOffset >= 0 ? 90 : -90);
        return MathUtils.calcNextCoord(basePosition, lateralAngle, Math.abs(lateralOffset));
    }

    private void addStaticObjectForUnit(FortificationUnitType unitType, Coordinate unitPosition) throws PWCGException
    {
        String staticObjectType = getStaticObjectType(unitType);
        if (staticObjectType == null)
        {
            return;
        }

        if (PWCGContext.getInstance().getVehicleDefinitionManager().getVehicleDefinition(staticObjectType) == null)
        {
            return;
        }

        GroundUnitInformation staticUnitInformation = buildDefenseGroundUnitInformation(unitPosition, TargetType.TARGET_NONE);
        staticUnitInformation.setRequestedUnitType(staticObjectType);
        GroundStaticSceneryUnit staticUnit = new GroundStaticSceneryUnit(staticUnitInformation);
        staticUnit.createGroundUnit();
        battleSegmentUnitCollection.addGroundUnit(staticUnit);
    }

    private String getStaticObjectType(FortificationUnitType unitType)
    {
        switch (unitType)
        {
            case MACHINE_GUN:
                return STATIC_MG_POSITION;
            case ARTILLERY:
                return getArtilleryPositionType();
            case ANTI_TANK:
            case ANTI_AIR:
            default:
                return getArtilleryPositionType();
        }
    }

    private String getArtilleryPositionType()
    {
        switch (fortificationSize)
        {
            case MEDIUM:
                return STATIC_ARTILLERY_POSITION_MEDIUM;
            case LARGE:
                return STATIC_ARTILLERY_POSITION_LARGE;
            case SMALL:
            default:
                return STATIC_ARTILLERY_POSITION_SMALL;
        }
    }

    private GroundUnitInformation buildAssaultGroundUnitInformation(Coordinate unitPosition, TargetType targetType) throws PWCGException
    {
        return GroundUnitInformationFactory.buildGroundUnitInformation(
                mission.getCampaign(),
                segmentDefinition.getAssaultingCountry(),
                targetType,
                unitPosition,
                Arrays.asList(segmentDefinition.getDefensePosition()),
                segmentDefinition.getTowardsDefenderOrientation());
    }

    private GroundUnitInformation buildDefenseGroundUnitInformation(Coordinate unitPosition, TargetType targetType) throws PWCGException
    {
        return GroundUnitInformationFactory.buildGroundUnitInformation(
                mission.getCampaign(),
                segmentDefinition.getDefendingCountry(),
                targetType,
                unitPosition,
                Arrays.asList(segmentDefinition.getAssaultPosition()),
                segmentDefinition.getTowardsAttackerOrientation());
    }

    private enum FortificationUnitType
    {
        MACHINE_GUN(TargetType.TARGET_INFANTRY),
        ANTI_TANK(TargetType.TARGET_ANTI_TANK),
        ANTI_AIR(TargetType.TARGET_AAA),
        ARTILLERY(TargetType.TARGET_ARTILLERY);

        private final TargetType targetType;

        FortificationUnitType(TargetType targetType)
        {
            this.targetType = targetType;
        }

        public TargetType getTargetType()
        {
            return targetType;
        }
    }
}
