package pwcg.campaign.resupply.equipment;

import java.util.ArrayList;
import java.util.List;

import pwcg.campaign.Campaign;
import pwcg.campaign.company.Company;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.resupply.depot.EquipmentReplacementUtils;
import pwcg.campaign.tank.Equipment;
import pwcg.campaign.tank.EquippedTank;
import pwcg.campaign.tank.TankArchType;
import pwcg.campaign.tank.TankEquipmentFactory;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.PWCGLogger;
import pwcg.core.utils.PWCGLogger.LogLevel;
import pwcg.core.utils.RandomNumberGenerator;

public class WithdrawnEquipmentReplacer
{
    private Campaign campaign;
    private Equipment equipment;
    private Company company;
    
    public WithdrawnEquipmentReplacer(Campaign campaign, Equipment equipment, Company company)
    {
        this.campaign = campaign;
        this.equipment = equipment;
        this.company = company;
    }
    
    public int replaceWithdrawnEquipment() throws PWCGException
    {
        int tanksRemoved = removeWithdrawnTanks();
        int tanksAdded = replaceWithNewTanks(tanksRemoved);
        return tanksAdded;
    }

    private int removeWithdrawnTanks()
    {
        int tanksRemoved = 0;
        for (EquippedTank tank: equipment.getActiveEquippedTanks().values())
        {
            if (isWithdrawnTank(tank))
            {
                equipment.deactivateEquippedTankFromCompany(tank.getSerialNumber(), campaign.getDate());
                ++tanksRemoved;
            }
        }

        return tanksRemoved;
    }

    private boolean isWithdrawnTank(EquippedTank tank)
    {
        if (campaign.getDate().before(tank.getWithdrawal()))
        {
            return false;
        }

        if (tank.isEquipmentRequest())
        {
            return false;
        }

        return true;
    }

    private int replaceWithNewTanks(int tanksRemoved) throws PWCGException
    {
        int numberOfTanksToAdd = calculateTanksNeeded(tanksRemoved);
        for (int i = 0; i < numberOfTanksToAdd; ++i)
        {
            String tankTypeName = determineTankType();
            if (!tankTypeName.isEmpty())
            {
                addTankToCompany(tankTypeName);
            }
        }

        return numberOfTanksToAdd;
    }

    private int calculateTanksNeeded(int tanksRemoved)
    {
        int minNeeded = Company.MIN_REEQUIPMENT_SIZE - equipment.getActiveEquippedTanks().size();
        int numNeeded = tanksRemoved;
        if (minNeeded > tanksRemoved)
        {
            numNeeded = minNeeded;
        }

        return numNeeded;
    }

    private String determineTankType() throws PWCGException
    {
        String tankArchTypeName = chooseArchTypeForCompany();
        if (!tankArchTypeName.isEmpty())
        {
            TankArchType tankArchType = PWCGContext.getInstance().getPlayerTankTypeFactory().getTankArchType(tankArchTypeName);
            String tankTypeName = EquipmentReplacementUtils.getTypeForReplacement(campaign.getDate(), tankArchType);
            return tankTypeName;
        }
        else
        {
            return "";
        }
    }

    private String chooseArchTypeForCompany() throws PWCGException
    {
        List<String> archTypes = determineAvailableArchTypes();
        if (archTypes.size() > 0)
        {
            int index = RandomNumberGenerator.getRandom(archTypes.size());
            return archTypes.get(index);
        }
        else
        {
            return "";
        }
    }

    private List<String> determineAvailableArchTypes() throws PWCGException
    {
        List<String> availableArchTypes = new ArrayList<>();
        for (String tankArchTypeName : equipment.getArchTypes())
        {
            TankArchType tankArchType = PWCGContext.getInstance().getPlayerTankTypeFactory().getTankArchType(tankArchTypeName);
            String tankTypeName = EquipmentReplacementUtils.getTypeForReplacement(campaign.getDate(), tankArchType);
            if (tankTypeName != null && !tankTypeName.isEmpty())
            {
                availableArchTypes.add(tankArchTypeName);
            }
            else
            {
                PWCGLogger.log(LogLevel.DEBUG, "");
            }
        }
        return availableArchTypes;
    }

    private void addTankToCompany(String tankTypeName) throws PWCGException
    {
        EquippedTank equippedTank = TankEquipmentFactory.makeTankForCompany(campaign, tankTypeName, company);
        equipment.addEquippedTankToCompany(campaign, company.getCompanyId(), equippedTank);
    }
}
