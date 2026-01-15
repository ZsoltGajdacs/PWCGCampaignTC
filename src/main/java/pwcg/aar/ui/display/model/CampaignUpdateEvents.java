package pwcg.aar.ui.display.model;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import pwcg.aar.ui.events.model.CrewMemberStatusEvent;
import pwcg.aar.ui.events.model.TankStatusEvent;
import pwcg.aar.ui.events.model.VictoryEvent;

@Getter
public class CampaignUpdateEvents
{
    private List<CrewMemberStatusEvent> crewMembersLost = new ArrayList<>();
    private List<TankStatusEvent> tanksLost = new ArrayList<>();
    private List<VictoryEvent> victories = new ArrayList<>();


    public void addCrewMemberLost(CrewMemberStatusEvent crewMemberLostEvent)
    {
        crewMembersLost.add(crewMemberLostEvent);
    }

    public void addVictory(VictoryEvent victory)
    {
        victories.add(victory);
    }

    public void addPlaneLost(TankStatusEvent planeLostEvent)
    {
        tanksLost.add(planeLostEvent);        
    }

}
