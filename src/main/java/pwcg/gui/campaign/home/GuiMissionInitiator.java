package pwcg.gui.campaign.home;

import java.util.Map;

import pwcg.campaign.Campaign;
import pwcg.campaign.tank.PwcgRole;
import pwcg.core.exception.PWCGException;
import pwcg.core.exception.PWCGUserException;
import pwcg.core.utils.DateUtils;
import pwcg.mission.Mission;
import pwcg.mission.MissionGenerator;
import pwcg.mission.MissionHumanParticipants;
import pwcg.mission.options.BattleMissionType;

public class GuiMissionInitiator 
{
	private Campaign campaign;
    private MissionHumanParticipants participatingPlayers;

	public GuiMissionInitiator(Campaign campaign, MissionHumanParticipants participatingPlayers)
	{
		this.campaign = campaign;
		this.participatingPlayers = participatingPlayers;
	}

    public Mission makeMission(Map<Integer, PwcgRole> companyRoleOverride) throws PWCGException 
    {
        return makeMission(companyRoleOverride, null);
    }

    public Mission makeMission(Map<Integer, PwcgRole> companyRoleOverride, BattleMissionType battleMissionType) throws PWCGException 
    {
        Mission mission = null;

        if (!(campaign.getDate().before(DateUtils.getEndOfWar())))
        {
            throw new PWCGUserException ("The war is over.  Go home.");
        }
        else
        {
            if (campaign.getCurrentMission() == null)
            {
                MissionGenerator missionGenerator = new MissionGenerator(campaign);
                mission = missionGenerator.makeMission(participatingPlayers, companyRoleOverride, battleMissionType);                    
            }
            else
            {
                mission = campaign.getCurrentMission();
            }
        }

        return mission;
    }
}
