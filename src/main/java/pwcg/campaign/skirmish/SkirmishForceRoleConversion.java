package pwcg.campaign.skirmish;

import lombok.Getter;
import pwcg.campaign.api.Side;
import pwcg.campaign.tank.PwcgRole;

@Getter
public class SkirmishForceRoleConversion
{
    Side side;
    PwcgRole fromRole;
    PwcgRole toRole;
}
