package pwcg.campaign.crewmember;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;

/**
 * Victory is was is stored in the campaign file.
 * A victory object can be created from a MissionResultVictory (from the logs)
 * or from straight forward IO of a victory object (See VictoryIO)
 *
 * @author Patrick Wilson
 *
 */
@Getter
@Setter
public class Victory implements Comparable<Victory>
{
    public static final int AIRCRAFT = 1;
    public static final int VEHICLE = 2;
    public static final int UNSPECIFIED_VICTORY = 3;

    private Date date = null;
    private String location = "";

    private VictoryEntity victim = new VictoryEntity();
    private VictoryEntity victor = new VictoryEntity();

    @Override
    public int compareTo(Victory otherVictory)
    {
        if (this.date.before(otherVictory.date))
        {
            return -1;
        }
        else if (this.date.after(otherVictory.date))
        {
            return 1;
        }
        return 0;
    }
}
