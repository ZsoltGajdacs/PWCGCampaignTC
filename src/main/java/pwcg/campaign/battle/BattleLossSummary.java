package pwcg.campaign.battle;

import lombok.Getter;
import lombok.Setter;
import pwcg.campaign.tank.PwcgRoleCategory;

@Getter
@Setter
public class BattleLossSummary
{
    private int tanks;
    private int planes;
    private int other;

    public BattleLossSummary()
    {
    }

    public void addLossForRoleCategory(PwcgRoleCategory roleCategory)
    {
        if (roleCategory == null)
        {
            other++;
            return;
        }

        switch (roleCategory)
        {
            case FIGHTER:
            case ATTACK:
            case BOMBER:
            case TRANSPORT:
                planes++;
                break;
            case MAIN_BATTLE_TANK:
            case TANK_DESTROYER:
            case SELF_PROPELLED_GUN:
            case SELF_PROPELLED_AAA:
            case ARMORED_CAR:
                tanks++;
                break;
            default:
                other++;
                break;
        }
    }

    public int getTotal()
    {
        return tanks + planes + other;
    }
}
