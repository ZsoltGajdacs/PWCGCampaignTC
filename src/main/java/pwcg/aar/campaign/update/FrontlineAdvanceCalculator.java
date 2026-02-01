package pwcg.aar.campaign.update;

import java.util.Collection;

import pwcg.aar.data.AARContext;
import pwcg.aar.inmission.phase2.logeval.AARMissionEvaluationData;
import pwcg.aar.inmission.phase2.logeval.missionresultentity.LogAIEntity;
import pwcg.aar.inmission.phase2.logeval.missionresultentity.LogTank;
import pwcg.aar.inmission.phase2.logeval.missionresultentity.LogVictory;
import pwcg.campaign.api.Side;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.tank.PwcgRoleCategory;
import pwcg.core.exception.PWCGException;
import pwcg.mission.ground.vehicle.VehicleClass;
import pwcg.mission.ground.vehicle.VehicleDefinition;

public class FrontlineAdvanceCalculator
{
    public FrontlineAdvanceResult calculate(AARContext aarContext) throws PWCGException
    {
        if (aarContext == null || aarContext.getMissionEvaluationData() == null)
        {
            return FrontlineAdvanceResult.noAdvance();
        }

        AARMissionEvaluationData evaluationData = aarContext.getMissionEvaluationData();
        FrontlineCombatTotals totals = new FrontlineCombatTotals();

        addForcesFromEntities(evaluationData.getPlaneAiEntities().values(), totals);
        addForcesFromEntities(evaluationData.getGroundAiEntities().values(), totals);

        for (LogVictory victory : evaluationData.getVictoryResults())
        {
            LogAIEntity victor = victory.getVictor();
            LogAIEntity victim = victory.getVictim();
            if (victor == null || victim == null || victor.getCountry() == null || victim.getCountry() == null)
            {
                continue;
            }

            Side victorSide = victor.getCountry().getSide();
            Side victimSide = victim.getCountry().getSide();
            if (victorSide == null || victimSide == null || victorSide == victimSide)
            {
                continue;
            }

            int weight = getWeightForEntity(victim);
            totals.addKills(victorSide, weight);
            totals.addLosses(victimSide, weight);
        }

        return decideAdvance(totals);
    }

    private void addForcesFromEntities(Collection<? extends LogAIEntity> entities, FrontlineCombatTotals totals) throws PWCGException
    {
        for (LogAIEntity entity : entities)
        {
            if (entity == null || entity.getCountry() == null)
            {
                continue;
            }
            Side side = entity.getCountry().getSide();
            if (side == null)
            {
                continue;
            }

            int weight = getWeightForEntity(entity);
            totals.addForces(side, weight);
        }
    }

    private FrontlineAdvanceResult decideAdvance(FrontlineCombatTotals totals)
    {
        double alliedRatio = totals.getRatioForSide(Side.ALLIED);
        double axisRatio = totals.getRatioForSide(Side.AXIS);

        Side winnerSide = null;
        double winnerRatio = 0.0;
        double winnerKills = 0.0;

        if (alliedRatio >= FrontlineAdvancePolicy.ADVANCE_RATIO || axisRatio >= FrontlineAdvancePolicy.ADVANCE_RATIO)
        {
            if (alliedRatio > axisRatio)
            {
                winnerSide = Side.ALLIED;
                winnerRatio = alliedRatio;
                winnerKills = totals.getKillsForSide(Side.ALLIED);
            }
            else if (axisRatio > alliedRatio)
            {
                winnerSide = Side.AXIS;
                winnerRatio = axisRatio;
                winnerKills = totals.getKillsForSide(Side.AXIS);
            }
            else
            {
                double alliedKills = totals.getKillsForSide(Side.ALLIED);
                double axisKills = totals.getKillsForSide(Side.AXIS);
                winnerSide = (alliedKills >= axisKills) ? Side.ALLIED : Side.AXIS;
                winnerRatio = alliedRatio;
                winnerKills = Math.max(alliedKills, axisKills);
            }
        }

        if (winnerSide == null)
        {
            return FrontlineAdvanceResult.noAdvance();
        }

        double enemyForces = totals.getForcesForSide(winnerSide.getOppositeSide());
        double wipeRatio = (enemyForces > 0.0) ? (winnerKills / enemyForces) : 0.0;

        double advanceDistance = 0.0;
        if (winnerRatio >= FrontlineAdvancePolicy.BREAKTHROUGH_RATIO && wipeRatio >= FrontlineAdvancePolicy.BREAKTHROUGH_WIPE_THRESHOLD)
        {
            advanceDistance = FrontlineAdvancePolicy.BREAKTHROUGH_DISTANCE_METERS;
        }
        else if (winnerRatio >= FrontlineAdvancePolicy.ADVANCE_RATIO)
        {
            advanceDistance = FrontlineAdvancePolicy.ADVANCE_DISTANCE_METERS;
        }

        return new FrontlineAdvanceResult(winnerSide, advanceDistance, winnerRatio, wipeRatio);
    }

    private int getWeightForEntity(LogAIEntity entity) throws PWCGException
    {
        FrontlineUnitCategory category = categorizeEntity(entity);
        switch (category)
        {
            case TANK:
                return FrontlineAdvancePolicy.TANK_WEIGHT;
            case PLANE:
                return FrontlineAdvancePolicy.PLANE_WEIGHT;
            case OTHER:
            default:
                return FrontlineAdvancePolicy.OTHER_WEIGHT;
        }
    }

    private FrontlineUnitCategory categorizeEntity(LogAIEntity entity) throws PWCGException
    {
        if (entity instanceof LogTank)
        {
            return FrontlineUnitCategory.TANK;
        }

        if (isAirRoleCategory(entity.getRoleCategory()))
        {
            return FrontlineUnitCategory.PLANE;
        }

        VehicleDefinition vehicleDefinition = PWCGContext.getInstance().getVehicleDefinitionManager().getVehicleDefinition(entity.getVehicleType());
        if (vehicleDefinition != null && vehicleDefinition.getVehicleClass() == VehicleClass.Tank)
        {
            return FrontlineUnitCategory.TANK;
        }

        return FrontlineUnitCategory.OTHER;
    }

    private boolean isAirRoleCategory(PwcgRoleCategory roleCategory)
    {
        if (roleCategory == null)
        {
            return false;
        }

        return roleCategory == PwcgRoleCategory.FIGHTER
                || roleCategory == PwcgRoleCategory.ATTACK
                || roleCategory == PwcgRoleCategory.BOMBER
                || roleCategory == PwcgRoleCategory.TRANSPORT;
    }

    private enum FrontlineUnitCategory
    {
        TANK,
        PLANE,
        OTHER
    }

    private static class FrontlineCombatTotals
    {
        private double alliedKills = 0.0;
        private double axisKills = 0.0;
        private double alliedLosses = 0.0;
        private double axisLosses = 0.0;
        private double alliedForces = 0.0;
        private double axisForces = 0.0;

        public void addKills(Side side, double value)
        {
            if (side == Side.ALLIED)
            {
                alliedKills += value;
            }
            else if (side == Side.AXIS)
            {
                axisKills += value;
            }
        }

        public void addLosses(Side side, double value)
        {
            if (side == Side.ALLIED)
            {
                alliedLosses += value;
            }
            else if (side == Side.AXIS)
            {
                axisLosses += value;
            }
        }

        public void addForces(Side side, double value)
        {
            if (side == Side.ALLIED)
            {
                alliedForces += value;
            }
            else if (side == Side.AXIS)
            {
                axisForces += value;
            }
        }

        public double getKillsForSide(Side side)
        {
            return (side == Side.ALLIED) ? alliedKills : axisKills;
        }

        public double getLossesForSide(Side side)
        {
            return (side == Side.ALLIED) ? alliedLosses : axisLosses;
        }

        public double getForcesForSide(Side side)
        {
            return (side == Side.ALLIED) ? alliedForces : axisForces;
        }

        public double getRatioForSide(Side side)
        {
            double kills = getKillsForSide(side);
            double losses = getLossesForSide(side);
            if (losses <= 0.0)
            {
                return kills > 0.0 ? Double.POSITIVE_INFINITY : 0.0;
            }
            return kills / losses;
        }
    }
}