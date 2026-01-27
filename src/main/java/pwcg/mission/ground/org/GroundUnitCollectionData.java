package pwcg.mission.ground.org;

import java.util.List;

import lombok.Getter;

import pwcg.mission.mcu.Coalition;
import pwcg.mission.target.TargetType;

@Getter
public class GroundUnitCollectionData
{
    private GroundUnitCollectionType groundUnitCollectionType;
    private String name;
    private List<Coalition> triggerCoalitions;
    private TargetType targetType;

    public GroundUnitCollectionData(GroundUnitCollectionType groundUnitCollectionType, String name, TargetType targetType, List<Coalition> triggerCoalitions)
    {
        this.groundUnitCollectionType = groundUnitCollectionType;
        this.name = name;
        this.targetType = targetType;
        this.triggerCoalitions = triggerCoalitions;
    }
}
