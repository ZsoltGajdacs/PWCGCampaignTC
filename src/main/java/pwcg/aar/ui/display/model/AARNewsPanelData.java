package pwcg.aar.ui.display.model;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import pwcg.aar.ui.events.model.AceKilledEvent;
import pwcg.campaign.newspapers.Newspaper;

@Getter
@Setter
public class AARNewsPanelData
{
    private List<AceKilledEvent> acesKilledDuringElapsedTime = new ArrayList<>();
    private List<Newspaper> newspaperEventsDuringElapsedTime = new ArrayList<>();

    public void merge(AARNewsPanelData newsPanelData)
    {
        acesKilledDuringElapsedTime.addAll(newsPanelData.getAcesKilledDuringElapsedTime());
        newspaperEventsDuringElapsedTime.addAll(newsPanelData.getNewspaperEventsDuringElapsedTime());
    }
}
