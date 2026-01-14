package pwcg.campaign.io.json;

import java.io.File;

import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.target.preference.TargetPreferenceSet;
import pwcg.core.exception.PWCGException;

public class TargetPreferenceIOJson 
{
	public static TargetPreferenceSet readJson(String mapName) throws PWCGException, PWCGException
	{
		JsonObjectReader<TargetPreferenceSet> jsonReader = new JsonObjectReader<>(TargetPreferenceSet.class);
		TargetPreferenceSet targetPreferenceSet = jsonReader.readJsonFile(PWCGContext.getInstance().getDirectoryManager().getPwcgInputDir() + mapName + File.separator, "TargetPreferences.json");
		return targetPreferenceSet;
	}
}
