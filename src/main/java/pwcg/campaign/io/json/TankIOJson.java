package pwcg.campaign.io.json;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.tank.TankTypeInformation;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.FileUtils;

public class TankIOJson 
{
	public static Map<String, TankTypeInformation> readJson() throws PWCGException
	{
	    Map<String, TankTypeInformation> tankMap = new TreeMap<>();

		List<File> jsonFiles = FileUtils.getFilesWithFilter(PWCGContext.getInstance().getDirectoryManager().getPwcgTankInfoDir(), ".json");

		for (File jsonFile : jsonFiles)
		{
			JsonObjectReader<TankTypeInformation> jsoReader = new JsonObjectReader<>(TankTypeInformation.class);
			TankTypeInformation tankType = jsoReader.readJsonFile(PWCGContext.getInstance().getDirectoryManager().getPwcgTankInfoDir(), jsonFile.getName());
			tankMap.put(tankType.getType(), tankType);
		}

		return tankMap;
	}
}
