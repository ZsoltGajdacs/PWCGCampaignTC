package pwcg.campaign.company;

import lombok.Getter;
import lombok.Setter;

import pwcg.campaign.tank.PwcgRole;

@Getter
@Setter
public class CompanyRoleWeight
{
    private PwcgRole role;
    private int weight;
}
