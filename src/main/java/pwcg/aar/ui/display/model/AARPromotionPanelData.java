package pwcg.aar.ui.display.model;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import pwcg.aar.ui.events.model.PromotionEvent;

@Getter
@Setter
public class AARPromotionPanelData
{
    private List<PromotionEvent> promotionEventsDuringElapsedTime = new ArrayList<>();

    public void merge(AARPromotionPanelData promotionPanelData)
    {
        promotionEventsDuringElapsedTime.addAll(promotionPanelData.getPromotionEventsDuringElapsedTime());        
    }
}
