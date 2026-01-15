package pwcg.aar.ui.display.model;

import java.util.HashMap;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;
import pwcg.aar.ui.events.model.CrewMemberStatusEvent;

@Getter
@Setter
public class AARCrewMemberLossPanelData
{
    private Map<Integer, CrewMemberStatusEvent> squadMembersLost = new HashMap<>();
    

    public void merge(AARCrewMemberLossPanelData eventData)
    {
        for (CrewMemberStatusEvent event : eventData.getSquadMembersLost().values())
        {
            squadMembersLost.put(event.getCrewMemberSerialNumber(), event);
        }
    }
}
