package pwcg.aar.data;

import lombok.Getter;
import lombok.Setter;
import pwcg.aar.outofmission.phase3.resupply.AARResupplyData;

@Getter
@Setter
public class AARContextDailyData
{
    private AARPersonnelLosses personnelLosses = new AARPersonnelLosses();
    private AAREquipmentLosses equipmentLosses = new AAREquipmentLosses();

    private AARPersonnelAwards personnelAwards = new AARPersonnelAwards();
    private AARPersonnelAcheivements personnelAcheivements = new AARPersonnelAcheivements();
    private AARResupplyData resupplyData = new AARResupplyData();
}
