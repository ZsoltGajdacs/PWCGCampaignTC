package pwcg.mission.ground.unittypes.staticobject;

import java.util.Arrays;
import java.util.List;

import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.mission.ground.GroundUnitInformation;
import pwcg.mission.ground.org.GroundUnit;
import pwcg.mission.ground.vehicle.VehicleClass;

public class GroundStaticSceneryUnit extends GroundUnit
{
    public GroundStaticSceneryUnit(GroundUnitInformation groundUnitInformation)
    {
        super(VehicleClass.StaticScenery, groundUnitInformation);
    }

    @Override
    public void createGroundUnit() throws PWCGException
    {
        super.createSpawnTimer();
        Coordinate position = groundUnitInformation.getPosition().copy();
        List<Coordinate> positions = Arrays.asList(position);
        super.createVehicles(positions);
        super.linkElements();
    }
}
