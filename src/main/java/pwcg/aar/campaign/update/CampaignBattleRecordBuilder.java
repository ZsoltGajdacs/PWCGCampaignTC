package pwcg.aar.campaign.update;

import java.util.ArrayList;
import java.util.List;

import pwcg.aar.data.AARContext;
import pwcg.aar.data.AAREquipmentLosses;
import pwcg.aar.inmission.phase2.logeval.missionresultentity.LogTank;
import pwcg.aar.inmission.phase2.logeval.missionresultentity.LogVictory;
import pwcg.campaign.Campaign;
import pwcg.campaign.api.Side;
import pwcg.campaign.battle.BattleLossSummary;
import pwcg.campaign.battle.CampaignBattleRecord;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.campaign.context.PWCGContext;

public class CampaignBattleRecordBuilder
{
    public CampaignBattleRecord build(Campaign campaign, AARContext aarContext) throws PWCGException
    {
        if (campaign == null || aarContext == null)
        {
            return null;
        }

        AAREquipmentLosses equipmentLosses = aarContext.getEquipmentLosses();
        List<LogVictory> victories = aarContext.getMissionEvaluationData().getVictoryResults();
        if ((equipmentLosses == null || equipmentLosses.getTanksDestroyed().isEmpty()) && (victories == null || victories.isEmpty()))
        {
            return null;
        }

        CampaignBattleRecord record = new CampaignBattleRecord();
        record.setDate(campaign.getDate());
        record.setLocation(determineBattleCenter(campaign, victories));

        BattleLossSummary alliedLosses = new BattleLossSummary();
        BattleLossSummary axisLosses = new BattleLossSummary();

        if (equipmentLosses != null)
        {
            for (LogTank lostTank : equipmentLosses.getTanksDestroyed().values())
            {
                if (lostTank.getCountry() == null)
                {
                    continue;
                }
                Side side = lostTank.getCountry().getSide();
                if (side == Side.ALLIED)
                {
                    alliedLosses.addLossForRoleCategory(lostTank.getRoleCategory());
                }
                else if (side == Side.AXIS)
                {
                    axisLosses.addLossForRoleCategory(lostTank.getRoleCategory());
                }
            }
        }

        record.setAlliedLosses(alliedLosses);
        record.setAxisLosses(axisLosses);
        record.setWinningSide(determineWinningSide(campaign, alliedLosses, axisLosses));

        return record;
    }

    private Coordinate determineBattleCenter(Campaign campaign, List<LogVictory> victories) throws PWCGException
    {
        List<Coordinate> locations = new ArrayList<>();
        if (victories != null)
        {
            for (LogVictory victory : victories)
            {
                if (victory.getLocation() != null)
                {
                    locations.add(victory.getLocation());
                }
            }
        }

        if (locations.isEmpty())
        {
            return PWCGContext.getInstance().getCurrentMap().getMapCenter();
        }

        double xSum = 0.0;
        double zSum = 0.0;
        for (Coordinate coordinate : locations)
        {
            xSum += coordinate.getXPos();
            zSum += coordinate.getZPos();
        }

        double xAvg = xSum / locations.size();
        double zAvg = zSum / locations.size();
        return new Coordinate(xAvg, 0.0, zAvg);
    }

    private Side determineWinningSide(Campaign campaign, BattleLossSummary alliedLosses, BattleLossSummary axisLosses) throws PWCGException
    {
        Side playerSide = campaign.findReferencePlayer().determineCountry(campaign.getDate()).getSide();
        BattleLossSummary friendlyLosses = playerSide == Side.ALLIED ? alliedLosses : axisLosses;
        BattleLossSummary enemyLosses = playerSide == Side.ALLIED ? axisLosses : alliedLosses;

        if (enemyLosses.getTotal() >= friendlyLosses.getTotal())
        {
            return playerSide;
        }

        return playerSide.getOppositeSide();
    }
}
