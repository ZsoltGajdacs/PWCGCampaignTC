package pwcg.campaign;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CompanyHistoryEntry
{
    public static final int NO_Company_SKILL_CHANGE = -1;
    private String date = "";
    private String squadName = "";
    private String armedServiceName = "";
    private int skill = NO_Company_SKILL_CHANGE;
    private String unitIdCode;
    private String subUnitIdCode;
}
