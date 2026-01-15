package pwcg.aar.ui.events.model;

import java.util.Date;

import lombok.Getter;

@Getter
public class AircraftIntroducedEvent extends AAREvent
{
    private String aircraft = "";

    public AircraftIntroducedEvent(String aircraftType, Date date, boolean isNewsWorthy)
    {
        super(date, isNewsWorthy);
        this.aircraft = aircraftType;
    }
}
