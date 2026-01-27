package pwcg.aar.tabulate.combatreport;

import lombok.Getter;
import lombok.Setter;
import pwcg.aar.ui.display.model.AARCombatReportMapData;
import pwcg.aar.ui.display.model.AARCombatReportPanelData;

@Getter
@Setter
public class UICombatReportData
{
    private int companyId;
    private AARCombatReportPanelData combatReportPanelData;
    private AARCombatReportMapData combatReportMapData;

    public UICombatReportData(int companyId)
    {
        this.companyId = companyId;
        this.combatReportPanelData = new AARCombatReportPanelData();
        this.combatReportMapData = new AARCombatReportMapData();
    }
}
