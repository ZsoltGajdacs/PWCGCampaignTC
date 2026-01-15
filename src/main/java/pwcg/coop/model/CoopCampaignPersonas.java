package pwcg.coop.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class CoopCampaignPersonas
{
    @Getter
    private String campaignName;
    private Map<Integer, String> campaignCrewMembers = new TreeMap<>();
    
    public CoopCampaignPersonas(String campaignName)
    {
        this.campaignName = campaignName;
    }

    public List<Integer> getSerialNumbers()
    {
        return new ArrayList<Integer>(campaignCrewMembers.keySet());
    }

    public void addPersona(int serialNumber, String crewMemberName)
    {
        if (!campaignCrewMembers.containsKey(serialNumber))
        {
            campaignCrewMembers.put(serialNumber, crewMemberName);
        }
    }

    public boolean hasPersona(int serialNumber)
    {
        if (campaignCrewMembers.containsKey(serialNumber))
        {
            return true;
        }
        return false;
    }

    public void remove(int serialNumber)
    {
        if (campaignCrewMembers.containsKey(serialNumber))
        {
            campaignCrewMembers.remove(serialNumber);
        }        
    }
}
