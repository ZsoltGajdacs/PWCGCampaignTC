package pwcg.campaign.tank;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import pwcg.campaign.Campaign;
import pwcg.campaign.company.Company;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;

public class Equipment
{
    private Map<Integer, EquippedTank> equippedTanks = new HashMap<>();
    
    public EquippedTank getEquippedTank(int tankSerialNumber)
    {
        return equippedTanks.get(tankSerialNumber);
    }

    public Map<Integer, EquippedTank> getAvailableDepotTanks()
    {
        Map<Integer, EquippedTank> availableDepotTanks = new HashMap<>();
        for (EquippedTank equippedTank : equippedTanks.values())
        {
            if (equippedTank.getTankStatus() == TankStatus.STATUS_DEPOT)
            {
                availableDepotTanks.put(equippedTank.getSerialNumber(), equippedTank);
            }
        }
        return availableDepotTanks;
    }

    public Map<Integer, EquippedTank> getActiveEquippedTanks()
    {
        Map<Integer, EquippedTank> activeEquippedTanks = new TreeMap<>();
        for (EquippedTank equippedTank : equippedTanks.values())
        {
            if (equippedTank.getTankStatus() == TankStatus.STATUS_DEPLOYED && equippedTank.getDateRemovedFromService() == null)
            {
                activeEquippedTanks.put(equippedTank.getSerialNumber(), equippedTank);
            }
        }
        return activeEquippedTanks;
    }

    public Map<Integer, EquippedTank> getRecentlyInactiveEquippedTanks(Date date) throws PWCGException
    {
        Map<Integer, EquippedTank> recentlyInactiveTanks = new HashMap<>();
        for (EquippedTank equippedTank : equippedTanks.values())
        {
            if (equippedTank.getDateRemovedFromService() != null)
            {
                if (equippedTank.getDateRemovedFromService().after(DateUtils.removeTimeDays(date, 7)))
                {
                    recentlyInactiveTanks.put(equippedTank.getSerialNumber(), equippedTank);
                }
            }
        }
        return recentlyInactiveTanks;
    }

    public boolean isCompanyEquipmentViable()
    {
        if (getActiveEquippedTanks().size() > (Company.COMPANY_STAFF_SIZE / 2))
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    public void addEquippedTankToCompany(Campaign campaign, int companyId, EquippedTank equippedTank) throws PWCGException
    {
        equippedTanks.put(equippedTank.getSerialNumber(), equippedTank);
    }

    public void addTankToDepot(EquippedTank equippedTank) throws PWCGException
    {
        equippedTanks.put(equippedTank.getSerialNumber(), equippedTank);
    }

    public EquippedTank removeBestEquippedFromDepot(List<String> activeArchTypes)
    {
        EquippedTank selectedTank = null;
        for (EquippedTank equippedTank : getTanksForArchTypes(activeArchTypes))
        {
            if (selectedTank == null || selectedTank.getGoodness() < equippedTank.getGoodness())
            {
                selectedTank = equippedTank;
            }
        }
        if (selectedTank != null)
        {
            return removeEquippedTank(selectedTank.getSerialNumber());
        }
        else
        {
            return null;
        }
    }

    public List<EquippedTank> getTanksForArchTypes(List<String> activeArchTypes)
    {
        List<EquippedTank> tanksForArchType = new ArrayList<>();
        for (EquippedTank equippedTank : equippedTanks.values())
        {
            for (String archTypeName : activeArchTypes)
            {
                if (equippedTank.getArchType().equals(archTypeName))
                {
                    tanksForArchType.add(equippedTank);
                }
            }
        }
        return tanksForArchType;
    }

    public List<String> getArchTypes()
    {
        Map<String, String> archTypeMap = new HashMap<>();
        for (EquippedTank equippedTank : equippedTanks.values())
        {
            archTypeMap.put(equippedTank.getArchType(), equippedTank.getArchType());
        }
        return new ArrayList<String>(archTypeMap.values());
    }

    public Map<Integer, EquippedTank> getEquippedTanks()
    {
        return equippedTanks;
    }

    public EquippedTank deactivateEquippedTankFromCompany(Integer tankSerialNumber, Date date)
    {
        EquippedTank equippedTank = equippedTanks.get(tankSerialNumber);
        if (equippedTank != null)
        {
            equippedTank.setTankStatus(TankStatus.STATUS_REMOVED_FROM_SERVICE);
            equippedTank.setDateRemovedFromService(date);
        }
        return equippedTank;
    }

    public EquippedTank removeEquippedTank(Integer tankSerialNumber)
    {
        return equippedTanks.remove(tankSerialNumber);
    }
}
