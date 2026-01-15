package pwcg.mission.flight;

import lombok.Getter;
import pwcg.campaign.api.ICountry;
import pwcg.campaign.plane.PlaneType;
import pwcg.core.location.Coordinate;
import pwcg.mission.Mission;

@Getter
public class FlightBuildInformation
{
    private static int FLIGHT_COUNT = 1;
    
    private Mission mission;
    private ICountry country;
    private FlightTypes flightType;
    private PlaneType planeType;
    private String flightName;
    private Coordinate homePosition;
    
    public FlightBuildInformation(Mission mission, ICountry country, FlightTypes flightType, PlaneType planeType, Coordinate homePosition)
    {
        this.mission = mission;
        this.country = country;
        this.flightType = flightType;
        this.planeType = planeType;
        this.flightName = planeType.getType() + "_" + FLIGHT_COUNT;
        this.homePosition = homePosition;
    }
}
