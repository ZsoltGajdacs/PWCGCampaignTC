package pwcg.aar.ui.events.model;

import java.util.Date;

import lombok.Getter;

@Getter
public class AircraftRetiredEvent  extends AAREvent
{
    private String aircraft = "";

    public AircraftRetiredEvent(String aircraft, Date date, boolean isNewsWorthy)
    {
        super(date, isNewsWorthy);
        this.aircraft = aircraft;
    }
}
