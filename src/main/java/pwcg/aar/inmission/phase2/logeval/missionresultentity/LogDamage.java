package pwcg.aar.inmission.phase2.logeval.missionresultentity;

import lombok.Getter;
import lombok.Setter;
import pwcg.core.location.Coordinate;

@Getter
@Setter
public class LogDamage extends LogBase
{
    private LogAIEntity victor = new LogUnknown();
    private LogAIEntity victim = new LogUnknown();
    private Coordinate location;
    private double damageLevel = 0.0;

    public LogDamage(int sequenceNumber)
    {
        super(sequenceNumber);
    }
}
