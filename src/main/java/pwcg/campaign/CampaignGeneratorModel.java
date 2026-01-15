package pwcg.campaign;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;
import pwcg.campaign.company.Company;
import pwcg.campaign.company.CompanyManager;
import pwcg.campaign.context.PWCGContext;
import pwcg.core.exception.PWCGException;
import pwcg.core.exception.PWCGUserException;
import pwcg.core.utils.DateUtils;

@Getter
@Setter
public class CampaignGeneratorModel
{
    private ArmedService service;
    private String campaignName;
    private String playerName;
    private String userName;
    private String playerRank;
    private String companyName;
    private Date campaignDate;
    private String playerRegion;
    private CampaignMode campaignMode = CampaignMode.CAMPAIGN_MODE_NONE;

    public Company getCampaignCompany() throws PWCGException
    {
    	CompanyManager companyManager = PWCGContext.getInstance().getCompanyManager();
        Company playerCompany = companyManager.getCompanyByName(companyName, campaignDate);
        return playerCompany;
    }
    

    public void validateCampaignInputs() throws PWCGException
    {
        if (getCampaignDate() == null)
        {
            throw new PWCGUserException ("Invalid date - no campaign start date provided");
        }


        Date earliest = DateUtils.getBeginningOfGame();
        Date latest = DateUtils.getEndOfWar();

        if (getCampaignDate().before(earliest) || getCampaignDate().after(latest))
        {
            throw new PWCGUserException ("Invalid date - must be between start and end of war");
        }

        if (getCampaignName() == null || getCampaignName().length() == 0)
        {
            throw new PWCGUserException ("Invalid name - no campaign name provided");
        }

        if (getPlayerName() == null || getPlayerName().length() == 0)
        {
            throw new PWCGUserException ("Invalid name - no crewMember name provided");
        }

        if (getPlayerRank() == null || getPlayerRank().length() == 0)
        {
            throw new PWCGUserException ("Invalid rank - no rank provided");
        }

        if (getCompanyName() == null || getCompanyName().length() == 0)
        {
            throw new PWCGUserException ("Invalid squaron - no company provided");
        }
        
        if (getPlayerRegion() == null)
        {
            setPlayerRegion("");
        }
    }

}
