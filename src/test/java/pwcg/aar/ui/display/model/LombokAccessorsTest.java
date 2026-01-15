package pwcg.aar.ui.display.model;

import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import pwcg.aar.ui.events.model.TankStatusEvent;
import pwcg.aar.ui.events.model.VictoryEvent;
import pwcg.mission.data.MissionHeader;

class LombokAccessorsTest
{
    @Test
    void campaignUpdateEventsTracksTanksLost()
    {
        CampaignUpdateEvents events = new CampaignUpdateEvents();
        TankStatusEvent tankStatusEvent = Mockito.mock(TankStatusEvent.class);

        events.addPlaneLost(tankStatusEvent);

        Assertions.assertEquals(List.of(tankStatusEvent), events.getTanksLost());
    }

    @Test
    void victoryEventsExposeOutOfMissionEvents()
    {
        VictoryEvents events = new VictoryEvents();
        VictoryEvent victoryEvent = Mockito.mock(VictoryEvent.class);

        events.addVictory(victoryEvent);

        Assertions.assertEquals(List.of(victoryEvent), events.getOutOfMissionVictoryEvents());
    }

    @Test
    void combatReportPanelDataUsesMissionHeaderAccessors()
    {
        AARCombatReportPanelData panelData = new AARCombatReportPanelData();
        MissionHeader missionHeader = Mockito.mock(MissionHeader.class);

        panelData.setMissionHeader(missionHeader);

        Assertions.assertSame(missionHeader, panelData.getMissionHeader());
    }
}
