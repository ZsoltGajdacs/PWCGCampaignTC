package pwcg.campaign.skin;

import java.io.File;
import java.util.Date;

import lombok.Getter;
import lombok.Setter;
import pwcg.campaign.context.PWCGDirectorySimulatorManager;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;
import pwcg.core.utils.PWCGLogger;

@Getter
@Setter
public class Skin implements Cloneable
{
    public static final String PRODUCT_SKIN_DIR = PWCGDirectorySimulatorManager.getInstance().getSkinsDir();
    
	private String skinName = "";
	private String planeType = "";
	private String[] archTypes = {};
	private Date startDate = DateUtils.getBeginningOfGame();
	private Date endDate = DateUtils.getEndOfWar();
    private int squadId = PERSONAL_SKIN;
    private String country = "";
    private String category = "";
    private boolean definedInGame = false;
    private boolean winter = false;
    private boolean useTacticalCodes = false;
    private TacticalCodeColor tacticalCodeColor = TacticalCodeColor.BLACK;
	
    public static int FACTORY_GENERIC = -2;
    public static int PERSONAL_SKIN = -1;
	
	public Skin() throws PWCGException 
	{
	}
	
	public Skin copy ()
    {
        boolean exists = false;

        String skinNameForLookup = skinName;
        if (!skinNameForLookup.contains(".dds"))
        {
            skinNameForLookup = skinNameForLookup + ".dds";
        }

        String filename = directory + planeType + File.separator + skinNameForLookup;
        File file = new File(filename);
        if (file.exists())
        {
            exists = true;
        }
        else
        {
            exists = false;
        }

        return exists;
    }

	public String getPlane() {
		return planeType;
	}

	public void setPlane(String plane) {
		this.planeType = plane;
	}
}
