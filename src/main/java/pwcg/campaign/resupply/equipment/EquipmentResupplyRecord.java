package pwcg.campaign.resupply.equipment;

import lombok.Getter;
import pwcg.campaign.tank.EquippedTank;

@Getter
public class EquipmentResupplyRecord
{
    private EquippedTank equippedTank;
    private int transferTo;

    public EquipmentResupplyRecord(EquippedTank equippedTank, int transferTo)
    {
        this.equippedTank  = equippedTank;
        this.transferTo  = transferTo;
    }
    
    public EquippedTank getEquippedPlane()
    {
        return equippedTank;
    }
}
