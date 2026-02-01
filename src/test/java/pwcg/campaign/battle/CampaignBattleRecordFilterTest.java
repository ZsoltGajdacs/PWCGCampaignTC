package pwcg.campaign.battle;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;

public class CampaignBattleRecordFilterTest
{
    @Test
    public void shouldFilterBattlesWithinSevenDays() throws PWCGException
    {
        Date campaignDate = DateUtils.getDateNoCheck("10/10/1941");
        Date withinRange = DateUtils.removeTimeDays(campaignDate, 5);
        Date outsideRange = DateUtils.removeTimeDays(campaignDate, 8);

        List<CampaignBattleRecord> records = new ArrayList<>();
        records.add(buildRecord(outsideRange));
        records.add(buildRecord(withinRange));
        records.add(buildRecord(campaignDate));

        List<CampaignBattleRecord> filtered = CampaignBattleRecordFilter.filterRecentBattles(records, campaignDate, 7);

        Assertions.assertEquals(2, filtered.size());
        Assertions.assertTrue(filtered.stream().anyMatch(record -> record.getDate().equals(withinRange)));
        Assertions.assertTrue(filtered.stream().anyMatch(record -> record.getDate().equals(campaignDate)));
    }

    private CampaignBattleRecord buildRecord(Date date)
    {
        CampaignBattleRecord record = new CampaignBattleRecord();
        record.setDate(date);
        return record;
    }
}
