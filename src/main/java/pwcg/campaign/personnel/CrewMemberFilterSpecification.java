package pwcg.campaign.personnel;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CrewMemberFilterSpecification
{
    public static final int NO_Company_FILTER = 0;

    private Date date;
    private boolean includeInactive = false;
    private boolean includeActive = true;
    private boolean includeAces = true;
    private boolean includeAI = true;
    private boolean includePlayer = true;
    private boolean includeWounded = true;
    private int specifyCompany = NO_Company_FILTER;

    public static int getNoCompanyFilter()
    {
        return NO_Company_FILTER;
    }
}
