package pwcg.aar.campaign.update;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import pwcg.aar.data.AARContext;
import pwcg.aar.inmission.phase2.logeval.AARMissionEvaluationData;
import pwcg.aar.inmission.phase2.logeval.missionresultentity.LogAIEntity;
import pwcg.aar.inmission.phase2.logeval.missionresultentity.LogNonPlayerVehicle;
import pwcg.aar.inmission.phase2.logeval.missionresultentity.LogTank;
import pwcg.aar.inmission.phase2.logeval.missionresultentity.LogVictory;
import pwcg.campaign.Campaign;
import pwcg.campaign.api.Side;
import pwcg.campaign.factory.CountryFactory;
import pwcg.campaign.tank.PwcgRoleCategory;
import pwcg.core.exception.PWCGException;
import pwcg.testutils.CampaignCache;
import pwcg.testutils.CompanyTestProfile;

public class FrontlineAdvanceCalculatorTest
{
    private Campaign campaign;

    @BeforeEach
    public void setup() throws PWCGException
    {
        campaign = CampaignCache.makeCampaign(CompanyTestProfile.THIRD_DIVISION_PROFILE);
    }

    @Test
    public void shouldAdvanceWithWeightedRatio() throws PWCGException
    {
        AARMissionEvaluationData evaluationData = new AARMissionEvaluationData();
        Map<String, LogTank> tanks = new HashMap<>();
        Map<String, LogNonPlayerVehicle> groundUnits = new HashMap<>();

        LogTank alliedTank = makeTank("alliedTank", Side.ALLIED);
        LogNonPlayerVehicle alliedPlane = makePlane("alliedPlane", Side.ALLIED);
        LogNonPlayerVehicle alliedOther = makeOther("alliedOther", Side.ALLIED);
        LogTank axisTank1 = makeTank("axisTank1", Side.AXIS);
        LogTank axisTank2 = makeTank("axisTank2", Side.AXIS);
        LogTank axisTank3 = makeTank("axisTank3", Side.AXIS);
        LogTank axisTank4 = makeTank("axisTank4", Side.AXIS);
        LogTank axisTank5 = makeTank("axisTank5", Side.AXIS);

        tanks.put(alliedTank.getId(), alliedTank);
        groundUnits.put(alliedPlane.getId(), alliedPlane);
        groundUnits.put(alliedOther.getId(), alliedOther);
        tanks.put(axisTank1.getId(), axisTank1);
        tanks.put(axisTank2.getId(), axisTank2);
        tanks.put(axisTank3.getId(), axisTank3);
        tanks.put(axisTank4.getId(), axisTank4);
        tanks.put(axisTank5.getId(), axisTank5);

        evaluationData.setPlaneAiEntities(tanks);
        evaluationData.setGroundAiEntities(groundUnits);
        evaluationData.setVictoryResults(buildVictories(
            victory(1, alliedTank, axisTank1),
            victory(2, alliedPlane, axisTank2),
            victory(3, axisTank3, alliedTank)));

        AARContext aarContext = new AARContext(campaign);
        aarContext.setMissionEvaluationData(evaluationData);

        FrontlineAdvanceCalculator calculator = new FrontlineAdvanceCalculator();
        FrontlineAdvanceResult result = calculator.calculate(aarContext);

        Assertions.assertTrue(result.shouldAdvance());
        Assertions.assertEquals(Side.ALLIED, result.getAdvancingSide());
        Assertions.assertEquals(FrontlineAdvancePolicy.ADVANCE_DISTANCE_METERS, result.getAdvanceDistanceMeters(), 0.01);
        Assertions.assertEquals(2.0, result.getKillLossRatio(), 0.01);
    }

    @Test
    public void shouldBreakThroughWithWipeThreshold() throws PWCGException
    {
        AARMissionEvaluationData evaluationData = new AARMissionEvaluationData();
        Map<String, LogTank> tanks = new HashMap<>();
        Map<String, LogNonPlayerVehicle> groundUnits = new HashMap<>();

        List<LogVictory> victories = new ArrayList<>();
        LogTank axisTank1 = makeTank("axisTank1", Side.AXIS);
        LogTank axisTank2 = makeTank("axisTank2", Side.AXIS);
        LogTank axisTank3 = makeTank("axisTank3", Side.AXIS);
        LogTank axisTank4 = makeTank("axisTank4", Side.AXIS);
        tanks.put(axisTank1.getId(), axisTank1);
        tanks.put(axisTank2.getId(), axisTank2);
        tanks.put(axisTank3.getId(), axisTank3);
        tanks.put(axisTank4.getId(), axisTank4);

        LogTank alliedVictor1 = makeTank("alliedVictor1", Side.ALLIED);
        LogTank alliedVictor2 = makeTank("alliedVictor2", Side.ALLIED);
        LogTank alliedVictor3 = makeTank("alliedVictor3", Side.ALLIED);
        LogTank alliedVictim = makeTank("alliedVictim", Side.ALLIED);
        tanks.put(alliedVictor1.getId(), alliedVictor1);
        tanks.put(alliedVictor2.getId(), alliedVictor2);
        tanks.put(alliedVictor3.getId(), alliedVictor3);
        tanks.put(alliedVictim.getId(), alliedVictim);

        victories.add(victory(10, alliedVictor1, axisTank1));
        victories.add(victory(11, alliedVictor2, axisTank2));
        victories.add(victory(12, alliedVictor3, axisTank3));
        victories.add(victory(20, axisTank4, alliedVictim));

        evaluationData.setPlaneAiEntities(tanks);
        evaluationData.setGroundAiEntities(groundUnits);
        evaluationData.setVictoryResults(victories);

        AARContext aarContext = new AARContext(campaign);
        aarContext.setMissionEvaluationData(evaluationData);

        FrontlineAdvanceCalculator calculator = new FrontlineAdvanceCalculator();
        FrontlineAdvanceResult result = calculator.calculate(aarContext);

        Assertions.assertTrue(result.shouldAdvance());
        Assertions.assertEquals(Side.ALLIED, result.getAdvancingSide());
        Assertions.assertEquals(FrontlineAdvancePolicy.BREAKTHROUGH_DISTANCE_METERS, result.getAdvanceDistanceMeters(), 0.01);
        Assertions.assertTrue(result.getKillLossRatio() >= FrontlineAdvancePolicy.BREAKTHROUGH_RATIO);
        Assertions.assertTrue(result.getWipeRatio() >= FrontlineAdvancePolicy.BREAKTHROUGH_WIPE_THRESHOLD);
    }

    @Test
    public void shouldAllowAdvanceWithoutBreakthroughWipe() throws PWCGException
    {
        AARMissionEvaluationData evaluationData = new AARMissionEvaluationData();
        Map<String, LogTank> tanks = new HashMap<>();

        List<LogVictory> victories = new ArrayList<>();
        LogTank axisTank1 = makeTank("axisTank1", Side.AXIS);
        LogTank axisTank2 = makeTank("axisTank2", Side.AXIS);
        LogTank axisTank3 = makeTank("axisTank3", Side.AXIS);
        LogTank axisTank4 = makeTank("axisTank4", Side.AXIS);
        tanks.put(axisTank1.getId(), axisTank1);
        tanks.put(axisTank2.getId(), axisTank2);
        tanks.put(axisTank3.getId(), axisTank3);
        tanks.put(axisTank4.getId(), axisTank4);

        LogTank alliedVictor1 = makeTank("alliedVictor1", Side.ALLIED);
        LogTank alliedVictor2 = makeTank("alliedVictor2", Side.ALLIED);
        LogTank alliedVictim = makeTank("alliedVictim", Side.ALLIED);
        tanks.put(alliedVictor1.getId(), alliedVictor1);
        tanks.put(alliedVictor2.getId(), alliedVictor2);
        tanks.put(alliedVictim.getId(), alliedVictim);

        victories.add(victory(100, alliedVictor1, axisTank1));
        victories.add(victory(101, alliedVictor2, axisTank2));
        victories.add(victory(110, axisTank3, alliedVictim));

        evaluationData.setPlaneAiEntities(tanks);
        evaluationData.setVictoryResults(victories);

        AARContext aarContext = new AARContext(campaign);
        aarContext.setMissionEvaluationData(evaluationData);

        FrontlineAdvanceCalculator calculator = new FrontlineAdvanceCalculator();
        FrontlineAdvanceResult result = calculator.calculate(aarContext);

        Assertions.assertTrue(result.shouldAdvance());
        Assertions.assertEquals(FrontlineAdvancePolicy.ADVANCE_DISTANCE_METERS, result.getAdvanceDistanceMeters(), 0.01);
        Assertions.assertTrue(result.getKillLossRatio() >= FrontlineAdvancePolicy.ADVANCE_RATIO);
        Assertions.assertTrue(result.getWipeRatio() < FrontlineAdvancePolicy.BREAKTHROUGH_WIPE_THRESHOLD);
    }

    @Test
    public void shouldNotAdvanceBelowRatio() throws PWCGException
    {
        AARMissionEvaluationData evaluationData = new AARMissionEvaluationData();
        Map<String, LogTank> tanks = new HashMap<>();

        LogTank alliedTank = makeTank("alliedTank", Side.ALLIED);
        LogTank axisTank = makeTank("axisTank", Side.AXIS);
        tanks.put(alliedTank.getId(), alliedTank);
        tanks.put(axisTank.getId(), axisTank);

        evaluationData.setPlaneAiEntities(tanks);
        evaluationData.setVictoryResults(buildVictories(
                victory(200, alliedTank, axisTank),
                victory(201, axisTank, alliedTank)));

        AARContext aarContext = new AARContext(campaign);
        aarContext.setMissionEvaluationData(evaluationData);

        FrontlineAdvanceCalculator calculator = new FrontlineAdvanceCalculator();
        FrontlineAdvanceResult result = calculator.calculate(aarContext);

        Assertions.assertFalse(result.shouldAdvance());
        Assertions.assertEquals(0.0, result.getAdvanceDistanceMeters(), 0.01);
    }

    private LogVictory victory(int sequence, LogAIEntity victor, LogAIEntity victim)
    {
        LogVictory victory = new LogVictory(sequence);
        victory.setVictor(victor);
        victory.setVictim(victim);
        return victory;
    }

    private List<LogVictory> buildVictories(LogVictory... victories)
    {
        List<LogVictory> results = new ArrayList<>();
        for (LogVictory victory : victories)
        {
            results.add(victory);
        }
        return results;
    }

    private LogTank makeTank(String id, Side side) throws PWCGException
    {
        LogTank tank = new LogTank(0);
        tank.setId(id);
        tank.setCountry(CountryFactory.makeMapReferenceCountry(side));
        tank.setRoleCategory(PwcgRoleCategory.MAIN_BATTLE_TANK);
        return tank;
    }

    private LogNonPlayerVehicle makePlane(String id, Side side)
    {
        LogNonPlayerVehicle plane = new LogNonPlayerVehicle(0);
        plane.setId(id);
        plane.setCountry(CountryFactory.makeMapReferenceCountry(side));
        plane.setRoleCategory(PwcgRoleCategory.FIGHTER);
        return plane;
    }

    private LogNonPlayerVehicle makeOther(String id, Side side)
    {
        LogNonPlayerVehicle other = new LogNonPlayerVehicle(0);
        other.setId(id);
        other.setCountry(CountryFactory.makeMapReferenceCountry(side));
        other.setRoleCategory(PwcgRoleCategory.GROUND_UNIT);
        return other;
    }
}