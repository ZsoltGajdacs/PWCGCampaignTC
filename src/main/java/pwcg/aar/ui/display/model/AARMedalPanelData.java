package pwcg.aar.ui.display.model;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import pwcg.aar.ui.events.model.MedalEvent;

@Getter
@Setter
public class AARMedalPanelData
{
    private List<MedalEvent> medalsAwarded = new ArrayList<>();

    public void merge(AARMedalPanelData medalPanelData)
    {
        medalsAwarded.addAll(medalPanelData.getMedalsAwarded());
    }
}
