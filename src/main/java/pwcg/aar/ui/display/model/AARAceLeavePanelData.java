package pwcg.aar.ui.display.model;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import pwcg.aar.ui.events.model.AceLeaveEvent;

@Getter
public class AARAceLeavePanelData
{
    private List<AceLeaveEvent> acesOnLeaveDuringElapsedTime = new ArrayList<>();

    public void addAceOnLeaveDuringElapsedTime(AceLeaveEvent aceOnLeave)
    {
        this.acesOnLeaveDuringElapsedTime.add(aceOnLeave);
    }

}
