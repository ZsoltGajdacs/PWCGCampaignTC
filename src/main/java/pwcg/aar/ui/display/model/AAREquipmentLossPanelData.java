package pwcg.aar.ui.display.model;

import java.util.HashMap;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;
import pwcg.aar.ui.events.model.TankStatusEvent;

@Getter
@Setter
public class AAREquipmentLossPanelData
{
    private Map<Integer, TankStatusEvent> equipmentLost = new HashMap<>();

    public void merge(AAREquipmentLossPanelData equipmentLossPanelData)
    {
        equipmentLost.putAll(equipmentLossPanelData.getEquipmentLost());
    }

}
