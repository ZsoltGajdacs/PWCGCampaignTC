package pwcg.gui.maingui.campaigngenerate;

import java.util.Date;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import pwcg.campaign.ArmedService;
import pwcg.campaign.Campaign;
import pwcg.campaign.CampaignMode;
import pwcg.campaign.api.IRankHelper;
import pwcg.campaign.context.FrontMapIdentifier;
import pwcg.campaign.crewmember.CrewMember;
import pwcg.campaign.factory.RankFactory;
import pwcg.campaign.tank.PwcgRole;
import pwcg.coop.CoopUserManager;
import pwcg.coop.model.CoopUser;
import pwcg.core.exception.PWCGException;

@Getter
@Setter
public class CampaignGeneratorDO
{
    private ArmedService service = null;
    private FrontMapIdentifier frontMap = null;
    private String campaignName = "";
    private String playerCrewMemberName = "";
    private String coopUser = "";
    private String region = "";
    private String rankName = null;
    private String squadName = "";
    private Date startDate = null;
    private PwcgRole role = PwcgRole.ROLE_FIGHTER;
    private CampaignMode campaignMode = CampaignMode.CAMPAIGN_MODE_NONE;

    public boolean isDataSetValid()
    {
        if (campaignName.isEmpty())
        {
            return false;
        }
        
        if (playerCrewMemberName.isEmpty())
        {
            return false;
        }
        
        if (rankName.isEmpty())
        {
            return false;
        }
        
        if (squadName.isEmpty())
        {
            return false;
        }
        
        if (startDate == null)
        {
            return false;
        }
        
        if (service == null)
        {
            return false;
        }

        return true;
    }

    public void initializeForService()
    {
        if (service != null)
        {
            role = PwcgRole.ROLE_FIGHTER;
            
            IRankHelper ranks = RankFactory.createRankHelper();
            List<String> rankList = ranks.getRanksByService(service);
            
            rankName = rankList.get(rankList.size()-1);
    
            startDate = service.getServiceStartDate();
        }
    }

    public boolean isCommandRank()
    {
        if (rankName != null && service != null)
        {
            IRankHelper rank = RankFactory.createRankHelper();
            return rank.isCommandRank(rankName, service);
        }
        
        return false;
    }
    
    public void createCoopUserAndPersona(Campaign campaign, CrewMember player) throws PWCGException
    {
        if (getCampaignMode() != CampaignMode.CAMPAIGN_MODE_SINGLE)
        {
            CoopUser coopUser = CoopUserManager.getIntance().getCoopUser(getCoopUser());
            if (coopUser == null)
            {
                coopUser = CoopUserManager.getIntance().buildCoopUser(getCoopUser());
            }
            
            CoopUserManager.getIntance().createCoopPersona(campaign.getName(), player, getCoopUser());
        }
    }

    public ArmedService getService()
    {
        return service;
    }

    public void setService(ArmedService service)
    {
        this.service = service;
        
        initializeForService();
    }

    public String getRank()
    {
        return rankName;
    }

    public void setRank(String rank)
    {
        this.rankName = rank;
    }
}
