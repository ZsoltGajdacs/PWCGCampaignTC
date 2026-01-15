package pwcg.mission.ground.vehicle;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import pwcg.campaign.context.Country;
import pwcg.core.utils.DateUtils;

@AllArgsConstructor
@Getter
public class VehicleRequestDefinition
{
    private Country country;
    private Date date;
    private VehicleClass vehicleClass;
    
    public String toString()
    {
        return "Country = " + country + "     Date = " + DateUtils.getDateStringYYYYMMDD(date) + "     Class = " + vehicleClass;
    }
}
