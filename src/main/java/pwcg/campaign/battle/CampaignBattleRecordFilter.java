package pwcg.campaign.battle;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;

public class CampaignBattleRecordFilter
{
    public static List<CampaignBattleRecord> filterRecentBattles(List<CampaignBattleRecord> records, Date campaignDate, int daysBack) throws PWCGException
    {
        List<CampaignBattleRecord> filtered = new ArrayList<>();
        if (records == null || campaignDate == null)
        {
            return filtered;
        }

        Date startDate = DateUtils.removeTimeDays(campaignDate, daysBack);
        for (CampaignBattleRecord record : records)
        {
            if (record != null && record.getDate() != null)
            {
                if (DateUtils.isDateInRange(record.getDate(), startDate, campaignDate))
                {
                    filtered.add(record);
                }
            }
        }

        return filtered;
    }
}
