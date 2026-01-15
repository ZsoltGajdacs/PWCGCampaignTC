package pwcg.mission.data;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MissionHeader
{
    private String missionFileName = "";
    private String company = "";
    private String date = "";
    private String time = "";
    private String vehicleType = "";
    private String mapName;
    private String base = "";
    private String duty = "";
}
