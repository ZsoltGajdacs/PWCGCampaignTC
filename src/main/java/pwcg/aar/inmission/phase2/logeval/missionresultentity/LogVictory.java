package pwcg.aar.inmission.phase2.logeval.missionresultentity;

import lombok.Getter;
import lombok.Setter;
import pwcg.aar.inmission.phase2.logeval.AARDamageStatus;
import pwcg.core.location.Coordinate;

@Getter
@Setter
public class LogVictory extends LogBase
{
    private LogAIEntity victor = new LogUnknown();
    private LogAIEntity victim = new LogUnknown();
    private AARDamageStatus damageInformation;
    private Coordinate location;
    private boolean confirmed = false;
    
    public LogVictory(int sequenceNumber)
    {
        super(sequenceNumber);
    }

    public boolean didCrewMemberDamageTank(String victorId)
    {
        if(damageInformation == null)
        {
            return false;
        }
        return damageInformation.didCrewMemberDamagePlane(victorId);
    }
}
