package pwcg.mission.ground.builder;

import pwcg.campaign.battle.AmphibiousAssault;
import pwcg.campaign.context.PWCGContext;
import pwcg.core.exception.PWCGException;
import pwcg.mission.Mission;
import pwcg.mission.ground.builder.amphibious.AmphibiousAssaultBuilder;
import pwcg.mission.ground.builder.amphibious.AmphibiousPlatoonBuilder;
import pwcg.mission.options.BattleMissionType;

public class MissionBattleBuilderFactory
{   
    public static IBattleBuilder getBattleBuilder(Mission mission) throws PWCGException
    {
        AmphibiousAssault amphibiousAssault = PWCGContext.getInstance().getCurrentMap().getAmphibiousAssaultManager().getActiveAmphibiousAssault(mission);
        if (amphibiousAssault != null)
        {
            return new AmphibiousAssaultBuilder(mission, amphibiousAssault);
        }

        BattleMissionType battleMissionType = mission.getMissionOptions().getBattleMissionType();
        if (battleMissionType != null
            && (battleMissionType == BattleMissionType.ATTACK_FORTIFICATION || battleMissionType == BattleMissionType.DEFEND_FORTIFICATION))
        {
            return new FortificationBattleBuilder(mission);
        }
        
        return new MissionBattleBuilder(mission);
    }

    public static IPlatoonBuilder getPlatoonBuilder(Mission mission) throws PWCGException
    {
        AmphibiousAssault amphibiousAssault = PWCGContext.getInstance().getCurrentMap().getAmphibiousAssaultManager().getActiveAmphibiousAssault(mission);
        if (amphibiousAssault != null)
        {
            return new AmphibiousPlatoonBuilder(mission);
        }
        
        return new MissionPlatoonBuilder(mission);
    }
}
