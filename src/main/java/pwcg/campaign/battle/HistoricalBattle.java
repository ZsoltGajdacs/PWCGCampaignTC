package pwcg.campaign.battle;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;
import pwcg.campaign.context.Country;
import pwcg.campaign.context.FrontMapIdentifier;
import pwcg.core.location.Coordinate;
import pwcg.core.utils.PWCGLogger;
import pwcg.core.utils.PWCGLogger.LogLevel;

@Getter
@Setter
public class HistoricalBattle
{
    private String name;
	private Coordinate neCorner;
	private Coordinate swCorner;
    private Country aggressorcountry;
    private Country defendercountry;
    private Date startDate;
    private Date stopDate;
    private FrontMapIdentifier map;

	public HistoricalBattle()
	{
	}
    
    public void dump()
    {
        PWCGLogger.log(LogLevel.DEBUG, name);
        PWCGLogger.log(LogLevel.DEBUG, startDate.toString());
        PWCGLogger.log(LogLevel.DEBUG, stopDate.toString());
        PWCGLogger.log(LogLevel.DEBUG, swCorner.toString());
        PWCGLogger.log(LogLevel.DEBUG, neCorner.toString());
        PWCGLogger.log(LogLevel.DEBUG, "" + map);
    }
}
