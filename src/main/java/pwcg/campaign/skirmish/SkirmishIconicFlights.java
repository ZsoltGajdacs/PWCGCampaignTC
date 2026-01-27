package pwcg.campaign.skirmish;

import lombok.Getter;
import pwcg.campaign.api.Side;
import pwcg.mission.flight.FlightTypes;

@Getter
public class SkirmishIconicFlights
{
    private FlightTypes flightType;
    private Side side;
    private int maxForcedFlightTypes;
}
