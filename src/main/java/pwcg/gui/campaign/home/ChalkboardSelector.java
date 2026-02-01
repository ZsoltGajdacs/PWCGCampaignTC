package pwcg.gui.campaign.home;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import pwcg.campaign.Campaign;
import pwcg.campaign.crewmember.CrewMember;
import pwcg.campaign.crewmember.CrewMembers;
import pwcg.campaign.personnel.CompanyPersonnel;
import pwcg.campaign.personnel.CrewMemberFilter;
import pwcg.core.exception.PWCGException;
import pwcg.core.exception.PWCGUserException;
import pwcg.core.utils.PWCGLogger;
import pwcg.gui.campaign.home.TopAcesListBuilder.TopAcesListType;
import pwcg.gui.colors.ColorMap;
import pwcg.gui.dialogs.ErrorDialog;
import pwcg.gui.dialogs.PWCGMonitorFonts;
import pwcg.gui.utils.PWCGLabelFactory;
import pwcg.gui.utils.ToolTipManager;

public class ChalkboardSelector extends JPanel implements ActionListener
{
    private static final long serialVersionUID = 1L;

    private Campaign campaign;
    private CampaignHomeScreen campaignHome;
    private Map<String, JButton> buttonsByAction = new HashMap<>();
    private JButton activeButton;

    public ChalkboardSelector(CampaignHomeScreen campaignHome)
    {
        this.campaignHome = campaignHome;
        this.campaign = campaignHome.getCampaign();
        
        setLayout(new BorderLayout());
        setOpaque(false);
    }

    public void createSelectorPanel() throws PWCGException
    {
        JPanel selectorPanel = new JPanel(new GridLayout(0, 3));
        selectorPanel.setOpaque(false);

        JButton crewMembersButton = makeButton("CrewMembers", "CampCrewMembers", "Show company crewMember chalk board");
        selectorPanel.add(crewMembersButton);

        JButton playerCrewMembersButton = makeButton("Player CrewMembers", "CampPlayerCrewMembers", "Show player crewMembers chalk board");
        selectorPanel.add(playerCrewMembersButton);

        JButton topAcesButton = makeButton("Top Aces: All", "CampTopAces", "Show top aces chalk board");
        selectorPanel.add(topAcesButton);

        JButton equipmentButton = makeButton("Equipment", "Equipment", "Show equipment chalk board");
        selectorPanel.add(equipmentButton);

        selectorPanel.add(PWCGLabelFactory.makeDummyLabel());

        JButton topAcesForServiceButton = makeButton("Top Aces: Service", "CampTopAcesService", "Show top aces chalk board for your service");
        selectorPanel.add(topAcesForServiceButton);

        selectorPanel.add(PWCGLabelFactory.makeDummyLabel());
        selectorPanel.add(PWCGLabelFactory.makeDummyLabel());

        JButton topAcesNoHistoricalButton = makeButton("Top Aces: Exclude Historical", "CampTopAcesNoHistorical", "Show top aces chalk board with no historical aces");
        selectorPanel.add(topAcesNoHistoricalButton); 
        
        this.add(selectorPanel, BorderLayout.CENTER);
        setActiveButton(crewMembersButton);
    }

    private JButton makeButton(String buttonText, String action, String toolTiptext) throws PWCGException 
    {
        Color fgColor = ColorMap.CHALK_FOREGROUND;

        Font font = PWCGMonitorFonts.getPrimaryFont();

        JButton button = new JButton(buttonText);
        button.setActionCommand(action);
        button.setHorizontalAlignment(SwingConstants.LEFT );
        button.setBorder(BorderFactory.createEmptyBorder(1, 4, 1, 4));
        button.setFocusPainted(false);
        button.addActionListener(this);
        button.setContentAreaFilled(false);
        button.setOpaque(false);
        button.setForeground(fgColor);
        button.setFont(font);
        
        ToolTipManager.setToolTip(button, toolTiptext);

        buttonsByAction.put(action, button);

        return button;
    }

    private void setActiveButton(String action)
    {
        JButton button = buttonsByAction.get(action);
        if (button != null)
        {
            setActiveButton(button);
        }
    }

    private void setActiveButton(JButton button)
    {
        if (activeButton != null)
        {
            applyActiveStyle(activeButton, false);
        }

        activeButton = button;

        if (activeButton != null)
        {
            applyActiveStyle(activeButton, true);
        }
    }

    private void applyActiveStyle(JButton button, boolean isActive)
    {
        if (isActive)
        {
            button.setOpaque(true);
            button.setContentAreaFilled(true);
            button.setBackground(ColorMap.CHALK_BACKGROUND);
            button.setBorder(BorderFactory.createLineBorder(ColorMap.CHALK_FOREGROUND));
        }
        else
        {
            button.setOpaque(false);
            button.setContentAreaFilled(false);
            button.setBorder(BorderFactory.createEmptyBorder(1, 4, 1, 4));
        }
    }

    @Override
    public void actionPerformed(ActionEvent ae)
    {
        try
        {
            String action = ae.getActionCommand();

            setActiveButton(action);

            if (action.equalsIgnoreCase("CampCrewMembers"))
            {
                createPlayerCompanyContext();
            }            
            else if (action.equalsIgnoreCase("CampPlayerCrewMembers"))
            {
                createPlayerCrewMembersContext();
            }
            else if (action.equalsIgnoreCase("CampTopAcesService"))
            {
                createTopAceContext(TopAcesListType.TOP_ACES_SERVICE);
            }
            else if (action.equalsIgnoreCase("CampTopAcesNoHistorical"))
            {
                createTopAceContext(TopAcesListType.TOP_ACES_NO_HISTORICAL);
            }
            else if (action.equalsIgnoreCase("CampTopAces"))
            {
                createTopAceContext(TopAcesListType.TOP_ACES_ALL);
            }
            else if (action.equalsIgnoreCase("Equipment"))
            {
                createEquipmentContext();
            }
        }
        catch (PWCGUserException ue)
        {
            campaign.setCurrentMission(null);
            PWCGLogger.logException(ue);
            ErrorDialog.userError(ue.getMessage());
        }
        catch (Exception e)
        {
            campaign.setCurrentMission(null);
            PWCGLogger.logException(e);
            ErrorDialog.internalError(e.getMessage());
        }
        catch (Throwable t)
        {
            campaign.setCurrentMission(null);
            PWCGLogger.logException(t);
            ErrorDialog.internalError(t.getMessage());
        }
    }

    public void createEquipmentContext() throws PWCGException 
    {       
        CampaignEquipmentChalkBoardPanelSet equipmentChalkboardDisplay = new CampaignEquipmentChalkBoardPanelSet(campaignHome.getChalkboardSelector());
        equipmentChalkboardDisplay.makeEquipmentPanel(campaignHome.getCampaign());
        
        List<CrewMember> crewMembers = makeCrewMemberList();
        CrewMember referencePlayer = campaign.findReferencePlayer();
        JPanel companyPanel = CampaignHomeRightPanelFactory.makeCampaignHomeCompanyRightPanel(campaignHome.getCampaign(), campaignHome, crewMembers, referencePlayer.getCompanyId());

        campaignHome.createNewContext(equipmentChalkboardDisplay, companyPanel);
    }    

    public void createPlayerCompanyContext() throws PWCGException 
    {
        List<CrewMember> crewMembers = makeCrewMemberList();
        JPanel chalkboardPanel =  CampaignHomeCenterPanelFactory.makeCampaignHomeCenterPanel(campaignHome, crewMembers);
        
        CrewMember referencePlayer = campaign.findReferencePlayer();
        JPanel companyPanel = CampaignHomeRightPanelFactory.makeCampaignHomeCompanyRightPanel(campaignHome.getCampaign(), campaignHome, crewMembers, referencePlayer.getCompanyId());

        campaignHome.createNewContext(chalkboardPanel, companyPanel);
    }

    public void createPlayerCrewMembersContext() throws PWCGException 
    {
        List<CrewMember> playerCrewMembers = campaign.getPersonnelManager().getAllPlayers().getCrewMemberList();
        JPanel chalkboardPanel =  CampaignHomeCenterPanelFactory.makeCampaignHomeCenterPanel(campaignHome, playerCrewMembers);
        
        JPanel playerCrewMemberPanel = CampaignHomeRightPanelFactory.makeCampaignHomeAcesRightPanel(campaignHome, playerCrewMembers);

        campaignHome.createNewContext(chalkboardPanel, playerCrewMemberPanel);
    }
    
    private List<CrewMember> makeCrewMemberList() throws PWCGException 
    {
        CrewMember referencePlayer = campaign.findReferencePlayer();
        CompanyPersonnel companyPersonnel = campaign.getPersonnelManager().getCompanyPersonnel(referencePlayer.getCompanyId());
        CrewMembers crewMembers = CrewMemberFilter.filterActiveAIAndPlayerAndAces(companyPersonnel.getCrewMembersWithAces().getCrewMemberCollection(), campaign.getDate());
        return crewMembers.sortCrewMembers(campaign.getDate());
    }

    private void createTopAceContext(TopAcesListType topAcesListType) throws PWCGException 
    {
        TopAcesListBuilder topAcesListBuilder = new TopAcesListBuilder(campaign);
        List<CrewMember> acesToDisplay = topAcesListBuilder.getTopTenAces(topAcesListType);
        
        CampaignHomeTopAcesCenterPanel topAceListChalkboard = new CampaignHomeTopAcesCenterPanel(campaignHome);
        topAceListChalkboard.makePanel(acesToDisplay);
        
        JPanel topAcesListPanel = CampaignHomeRightPanelFactory.makeCampaignHomeAcesRightPanel(campaignHome, acesToDisplay);

        campaignHome.createNewContext(topAceListChalkboard, topAcesListPanel);
    }
}
