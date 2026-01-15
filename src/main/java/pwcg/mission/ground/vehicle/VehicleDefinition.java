package pwcg.mission.ground.vehicle;

import java.util.Date;
import java.util.List;

import lombok.Getter;
import pwcg.campaign.context.Country;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.tank.TankTypeInformation;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;
import pwcg.core.utils.IWeight;

@Getter
public class VehicleDefinition implements IWeight
{
    public final static String UNKNOWN_VEHICLE_NAME = "Unknown Vehicle";

    private String scriptDir;
    private String modelDir;
    private String vehicleName;
    private String vehicleType;
    private String displayName;
    private List<Country> countries;
    private Date startDate;
    private Date endDate;
    private int rarityWeight;
    private VehicleClass vehicleClass;
    private String associatedBlock;
    private int vehicleLength;
    private String archType = "";

    @Override
    public int getWeight()
    {
        return rarityWeight;
    }

    public boolean shouldUse(VehicleRequestDefinition requestDefinition) throws PWCGException
    {
        if (vehicleClass != requestDefinition.getVehicleClass())
        {
            return false;
        }

        if (!DateUtils.isDateInRange(requestDefinition.getDate(), startDate, endDate))
        {
            return false;
        }

        if (requestDefinition.getVehicleClass() == VehicleClass.Drifter)
        {
            if (vehicleLength > 100)
            {
                return false;
            }
        }

        for (Country vehicleCountry : countries)
        {
            if (vehicleCountry == requestDefinition.getCountry())
            {
                return true;
            }
        }
        return false;
    }

    public boolean isPlayerDrivable() throws PWCGException
    {
        TankTypeInformation tankDefinition = PWCGContext.getInstance().getFullTankTypeFactory().createTankTypeByAnyName(vehicleType);
        if (tankDefinition != null)
        {
            return tankDefinition.isPlayer();
        }
        return false;
    }
}
