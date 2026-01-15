package pwcg.mission.options;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MapSeasonalParameters
{
    public enum Season
    {
        WINTER,
        SPRING,
        SUMMER,
        AUTUMN
    }

    public static String WINTER_STRING = "winter";
    public static String SPRING_STRING = "spring";
    public static String SUMMER_STRING = "summer";
    public static String AUTUMN_STRING = "autumn";  // Autumn uses the summer prefix

    public static String WINTER_ABREV = "wi";
    public static String SPRING_ABREV = "sp";
    public static String SUMMER_ABREV = "su";
    public static String AUTUMN_ABREV = "au"; // Autumn uses the summer prefix

    private String heightMap = "Oops";
    private String textureMap = "Oops";
    private String forrestMap = "Oops";
    private String guiMap = "Oops";
    private String season = "Oops";
    private String seasonAbbreviation = "Oops";
}
