package pwcg.campaign.company;

import java.util.Date;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CompanyRolePeriod
{
    private Date startDate;
    private List<CompanyRoleWeight> weightedRoles;
}
