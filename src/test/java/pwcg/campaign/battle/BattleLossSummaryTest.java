package pwcg.campaign.battle;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import pwcg.campaign.tank.PwcgRoleCategory;

public class BattleLossSummaryTest
{
    @Test
    public void shouldMapRoleCategoriesToLossBuckets()
    {
        BattleLossSummary summary = new BattleLossSummary();

        summary.addLossForRoleCategory(PwcgRoleCategory.FIGHTER);
        summary.addLossForRoleCategory(PwcgRoleCategory.MAIN_BATTLE_TANK);
        summary.addLossForRoleCategory(PwcgRoleCategory.GROUND_UNIT);

        Assertions.assertEquals(1, summary.getPlanes());
        Assertions.assertEquals(1, summary.getTanks());
        Assertions.assertEquals(1, summary.getOther());
        Assertions.assertEquals(3, summary.getTotal());
    }
}
