package pwcg.gui.rofmap.campaignmap;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import pwcg.campaign.Campaign;
import pwcg.campaign.context.PWCGContext;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.PWCGLogger;
import pwcg.gui.CampaignGuiContextManager;
import pwcg.gui.colors.ColorMap;
import pwcg.gui.dialogs.ErrorDialog;
import pwcg.gui.rofmap.MapGUI;
import pwcg.gui.rofmap.MapScroll;
import pwcg.gui.utils.PWCGButtonFactory;
import pwcg.gui.utils.PWCGLabelFactory;

public class CampaignBattleMapGUI extends MapGUI implements ActionListener
{
    private static final long serialVersionUID = 1L;

    private Campaign campaign;

    public CampaignBattleMapGUI(Campaign campaign) throws PWCGException
    {
        super(campaign.getDate());
        this.campaign = campaign;
        PWCGContext.getInstance().setCampaign(campaign);
    }

    public void makePanels()
    {
        try
        {
            Color bg = ColorMap.MAP_BACKGROUND;
            setSize(200, 200);
            setOpaque(false);
            setBackground(bg);

            this.add(BorderLayout.WEST, makeNavigationPanel());
            this.add(BorderLayout.CENTER, createMapPanel());

            centerMapAt(null);
        }
        catch (Exception e)
        {
            PWCGLogger.logException(e);
            ErrorDialog.internalError(e.getMessage());
        }
    }

    private JPanel createMapPanel() throws PWCGException
    {
        JPanel mapCenterPanel = new JPanel(new BorderLayout());

        CampaignBattleMapPanel mapPanel = new CampaignBattleMapPanel(this, campaign);
        mapScroll = new MapScroll(mapPanel);
        mapPanel.setData();

        mapCenterPanel.add(mapScroll.getMapScrollPane(), BorderLayout.CENTER);
        return mapCenterPanel;
    }

    private JPanel makeNavigationPanel() throws PWCGException
    {
        JPanel navPanel = new JPanel(new BorderLayout());
        navPanel.setOpaque(false);

        JPanel buttonPanel = new JPanel(new GridLayout(0, 1));
        buttonPanel.setOpaque(false);

        JButton finishedButton = PWCGButtonFactory.makeTranslucentMenuButton("Finished", "Finished", "Return to campaign home", this);
        buttonPanel.add(finishedButton);

        JLabel spacer = PWCGLabelFactory.makeMenuLabelLarge("");
        buttonPanel.add(spacer);

        navPanel.add(buttonPanel, BorderLayout.NORTH);
        return navPanel;
    }

    @Override
    public void actionPerformed(ActionEvent ae)
    {
        try
        {
            String action = ae.getActionCommand();
            if (action.equalsIgnoreCase("Finished"))
            {
                CampaignGuiContextManager.getInstance().backToCampaignHome();
            }
        }
        catch (Exception e)
        {
            PWCGLogger.logException(e);
            ErrorDialog.internalError(e.getMessage());
        }
    }
}
