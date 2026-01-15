package pwcg.aar.ui.display.model;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import pwcg.aar.ui.events.model.VictoryEvent;

@Getter
public class VictoryEvents
{
    private List<VictoryEvent> outOfMissionVictoryEvents = new ArrayList<>();

    public void addVictory(VictoryEvent victory)
    {
        outOfMissionVictoryEvents.add(victory);
        
    }

    public void merge(VictoryEvents victoryEventsToMerge)
    {
        outOfMissionVictoryEvents.addAll(victoryEventsToMerge.getOutOfMissionVictoryEvents());
    }
}
