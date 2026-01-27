package pwcg.campaign.group.airfield.hotspot;

import lombok.Getter;
import lombok.Setter;
import pwcg.core.location.Coordinate;
import pwcg.core.location.Orientation;

@Getter
@Setter
public class HotSpot
{
    private Coordinate position = new Coordinate();
	private Orientation orientation = new Orientation();
	private HotSpotType hotSpotType = HotSpotType.HOTSPOT_UNUSED;
}
