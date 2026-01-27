package pwcg.campaign.group.airfield;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import pwcg.core.location.PWCGLocation;

@Getter
@Setter
public class AirfieldDescriptor extends PWCGLocation
{
    private List<Runway> runways = new ArrayList<>();
}
