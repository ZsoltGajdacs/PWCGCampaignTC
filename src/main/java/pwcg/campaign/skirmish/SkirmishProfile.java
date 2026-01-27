package pwcg.campaign.skirmish;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SkirmishProfile
{
    private SkirmishProfileType profileType;
    private List<SkirmishProfileElement> skirmishProfileElements = new ArrayList<>();
}
