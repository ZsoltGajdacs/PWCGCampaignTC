package pwcg.gui.rofmap.brief;

import java.util.List;

import javax.swing.JComponent;
import javax.swing.JOptionPane;

import pwcg.campaign.tank.EquippedTank;
import pwcg.core.exception.PWCGException;
import pwcg.gui.rofmap.brief.model.BriefingPlayerPlatoon;

public class BriefingTankPicker
{
    private BriefingPlayerPlatoon missionEditHandler;
    private JComponent parent;
    
    public BriefingTankPicker(BriefingPlayerPlatoon missionEditHandler, JComponent parent)
    {
        this.missionEditHandler = missionEditHandler;
        this.parent = parent;
    }

    public Integer pickTank(Integer crewMemberSerialNumber) throws PWCGException
    {
        List<EquippedTank> companyTanks = missionEditHandler.getSortedUnassignedTanks();
        Object[] possibilities = new Object[companyTanks.size()];
        for (int i = 0; i < companyTanks.size(); ++i)
        {
            EquippedTank tank = companyTanks.get(i);
            PickerEntry entry = new PickerEntry();
            entry.description = tank.getDisplayName();
            entry.tank = tank;
            possibilities[i] = entry;
        }

        PickerEntry pickedTank = (PickerEntry)JOptionPane.showInputDialog(
                parent,
                "Select Tank",
                "Select Tank",
                JOptionPane.PLAIN_MESSAGE,
                null,
                possibilities,
                null);

        if (pickedTank != null)
            return pickedTank.tank.getSerialNumber();

        return null;
    }

    private static class PickerEntry
    {
        public String description;
        public EquippedTank tank;

        public String toString()
        {
            return description;
        }
    }
}
