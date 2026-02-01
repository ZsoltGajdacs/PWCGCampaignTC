package pwcg.campaign.battle;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;
import pwcg.campaign.api.Side;
import pwcg.core.location.Coordinate;

@Getter
@Setter
public class CampaignBattleRecord
{
    private Date date;
    private Coordinate location = new Coordinate();
    private Side winningSide = Side.NEUTRAL;
    private BattleLossSummary alliedLosses = new BattleLossSummary();
    private BattleLossSummary axisLosses = new BattleLossSummary();

    public CampaignBattleRecord()
    {
    }
}
