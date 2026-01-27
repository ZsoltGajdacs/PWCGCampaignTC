package pwcg.mission.ground.building;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class BuildingSearchCriteria
{
    private String searchValue;
    private BuildingSearchMethod searchMethod;
}
