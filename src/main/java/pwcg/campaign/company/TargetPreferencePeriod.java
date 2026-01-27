package pwcg.campaign.company;

import java.util.Date;

import lombok.Getter;

import pwcg.mission.target.TargetType;

@Getter
public class TargetPreferencePeriod
{
    private Date startDate;
    private Date endDate;
    private TargetType targetType;
    private Integer targetPreferenceOdds;
}
