package pwcg.campaign;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import pwcg.campaign.battle.CampaignBattleRecord;
import pwcg.campaign.crewmember.SerialNumber;

@Getter
@Setter
public class CampaignData
{
	private Date date = null;
	private String name = "";
    @Setter(lombok.AccessLevel.NONE)
    private boolean isCoop = false;
    private CampaignMode campaignMode = CampaignMode.CAMPAIGN_MODE_NONE;
    private SerialNumber serialNumber = new SerialNumber();
    private int referencePlayerSerialNumber = 0;
    private List<CampaignBattleRecord> recentBattleRecords = new ArrayList<>();
}
