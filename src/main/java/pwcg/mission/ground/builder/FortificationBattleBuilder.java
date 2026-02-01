package pwcg.mission.ground.builder;

import pwcg.core.exception.PWCGException;
import pwcg.mission.Mission;
import pwcg.mission.ground.org.GroundUnitCollection;

public class FortificationBattleBuilder implements IBattleBuilder
{
    private Mission mission = null;

    public FortificationBattleBuilder(Mission mission)
    {
        this.mission = mission;
    }

    @Override
    public GroundUnitCollection generateBattle() throws PWCGException
    {
        FortificationSize fortificationSize = FortificationObjectiveDensityCalculator.determineFortificationSize(mission);
        GroundUnitCollection fortificationUnitCollection = FortificationFixedUnitSegmentsBuilder.generateFortifications(mission, fortificationSize);
        return fortificationUnitCollection;
    }
}
