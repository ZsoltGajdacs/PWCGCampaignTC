package pwcg.mission.ground;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import pwcg.campaign.api.ICountry;
import pwcg.campaign.context.Country;
import pwcg.campaign.factory.CountryFactory;
import pwcg.core.location.Coordinate;
import pwcg.core.location.Orientation;
import pwcg.mission.target.TargetType;

@Getter
@Setter
public class GroundUnitInformation
{
    private ICountry country = CountryFactory.makeCountryByCountry(Country.NEUTRAL);
    private String name = "";
    private Date date;
    private Coordinate position = new Coordinate();
    private List<Coordinate> destinations = new ArrayList<>();
	private Orientation orientation = new Orientation();
    private TargetType targetType = TargetType.TARGET_NONE;
    private String requestedUnitType = "";

    public Coordinate getDestination()
    {
        if (!destinations.isEmpty())
        {
            return destinations.get(destinations.size()-1);
        }
        return null;
    }

    public void addDestination(Coordinate destination)
    {
        this.destinations.add(destination);
    }
}
