package pwcg.campaign.group.airfield;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AirfieldDescriptorSet
{
    private String locationSetName = "";
    private List<AirfieldDescriptor> locations = new ArrayList<>();
}
