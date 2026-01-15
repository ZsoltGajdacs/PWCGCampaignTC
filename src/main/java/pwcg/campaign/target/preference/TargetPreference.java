package pwcg.campaign.target.preference;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;
import pwcg.campaign.api.Side;
import pwcg.mission.target.TargetType;

@Getter
@Setter
public class TargetPreference
{
    private Date startDate;
    private Date endDate;
    private Side targetSide;
    private TargetType targetType;
    private int oddsOfUse;
}
