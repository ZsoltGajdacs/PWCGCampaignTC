package pwcg.gui.campaign.activity;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import pwcg.campaign.Campaign;
import pwcg.campaign.api.ICountry;
import pwcg.campaign.company.Company;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.crewmember.CrewMember;
import pwcg.campaign.factory.CountryFactory;
import pwcg.campaign.tank.Equipment;
import pwcg.campaign.tank.EquippedTank;
import pwcg.campaign.tank.ITankTypeFactory;
import pwcg.campaign.tank.PwcgRoleCategory;
import pwcg.campaign.tank.TankTypeInformation;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.PWCGLogger;
import pwcg.gui.CampaignGuiContextManager;
import pwcg.gui.ScreenIdentifier;
import pwcg.gui.UiImageResolver;
import pwcg.gui.dialogs.ErrorDialog;
import pwcg.gui.dialogs.PWCGMonitorFonts;
import pwcg.gui.utils.ImageResizingPanel;
import pwcg.gui.utils.ImageResizingPanelBuilder;
import pwcg.gui.utils.ImageToDisplaySizer;
import pwcg.gui.utils.PWCGButtonFactory;
import pwcg.gui.utils.PWCGLabelFactory;
import pwcg.gui.utils.PwcgBorderFactory;
import pwcg.gui.utils.SpacerPanelFactory;

public class EquipmentRequestScreen extends ImageResizingPanel implements ActionListener
{
    private static final long serialVersionUID = 1L;

    private Campaign campaign = null;
    private Map<Integer, JCheckBox> tankRetireChecklist = new TreeMap<>();
    private Map<Integer, JCheckBox> tankChangeChecklist = new TreeMap<>();
    private JComboBox<String> replacementTankTypeSelector;

    public EquipmentRequestScreen(Campaign campaign)
    {
        super("");
        this.setLayout(new GridBagLayout());
        this.setOpaque(false);

        this.campaign = campaign;
    }

    public void makePanels() throws PWCGException
    {
        String imagePath = UiImageResolver.getImage(ScreenIdentifier.CampaignLeaveScreen);
        this.setImageFromName(imagePath);

        GridBagConstraints constraints = initializeGridbagConstraints();

        constraints.weightx = 0.1;
        constraints.gridx = 0;
        constraints.gridy = 0;
        this.add(makeNavPanel(), constraints);

        constraints.weightx = 0.1;
        constraints.gridx = 1;
        constraints.gridy = 0;
        this.add(makeCenterPanel(), constraints);
        
        constraints.weightx = 0.5;
        constraints.gridx = 2;
        constraints.gridy = 0;
        this.add(SpacerPanelFactory.makeDocumentSpacerPanel(1400), constraints);

    }

    private JPanel makeNavPanel() throws PWCGException
    {
        JPanel navPanel = new JPanel(new BorderLayout());
        navPanel.setOpaque(false);

        JPanel equipmentManagementButtonPanel = new JPanel(new GridLayout(0, 1));
        equipmentManagementButtonPanel.setOpaque(false);

        JButton acceptButton = PWCGButtonFactory.makeTranslucentMenuButton("Finished", "Finished", "Finished with equipment management", this);
        equipmentManagementButtonPanel.add(acceptButton);

        navPanel.add(equipmentManagementButtonPanel, BorderLayout.NORTH);

        return navPanel;
    }

    private JPanel makeCenterPanel() throws PWCGException
    {
        String imagePath = UiImageResolver.getImage(ScreenIdentifier.Document);
        ImageResizingPanel equipmentManagementPanel = ImageResizingPanelBuilder.makeImageResizingPanel(imagePath);
        equipmentManagementPanel.setBorder(PwcgBorderFactory.createStandardDocumentBorder());

        JPanel equipmentSelectionPanel = makeEquipmentSelectionPanel();
        equipmentManagementPanel.add(equipmentSelectionPanel, BorderLayout.NORTH);

        JPanel equipmentReplaceConfirmationPanel = makeEquipmentSelectionConfirmationPanel();
        equipmentManagementPanel.add(equipmentReplaceConfirmationPanel, BorderLayout.CENTER);

        ImageToDisplaySizer.setDocumentSize(equipmentManagementPanel);

        return equipmentManagementPanel;
    }


    private JPanel makeEquipmentSelectionPanel() throws PWCGException
    {
        JPanel equipmentSelectionPanel = new JPanel();
        equipmentSelectionPanel.setLayout(new BorderLayout());
        equipmentSelectionPanel.setOpaque(false);
        
        JPanel equipmentRetirementSelectionPanel = makeEquipmentRetirementSelectionPanel();
        equipmentSelectionPanel.add(equipmentRetirementSelectionPanel, BorderLayout.NORTH);

        JPanel equipmentChangeSelectionPanel = makeEquipmentChangeSelectionPanel();
        equipmentSelectionPanel.add(equipmentChangeSelectionPanel, BorderLayout.SOUTH);
        
        return equipmentSelectionPanel;
    }
    
    private JPanel makeEquipmentRetirementSelectionPanel() throws PWCGException
    {
        JPanel equipmentRetirementSelectionPanel = new JPanel();
        equipmentRetirementSelectionPanel.setOpaque(false);
        equipmentRetirementSelectionPanel.setLayout(new GridLayout(0, 1));
        equipmentRetirementSelectionPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        CrewMember referencePlayer = campaign.getReferencePlayer();
        Equipment equipment = campaign.getEquipmentManager().getEquipmentForCompany(referencePlayer.getCompanyId());

        JLabel titleLabel = PWCGLabelFactory.makePaperLabelLarge("Retire Equipment Requests (Previously Ordered Equipment)");
        equipmentRetirementSelectionPanel.add(titleLabel);

        for (int serialNumber : equipment.getActiveEquippedTanks().keySet())
        {
            EquippedTank tank = equipment.getEquippedTank(serialNumber);
            if (!tank.isEquipmentRequest())
            {
                continue;
            }

            JCheckBox tankCheckBox = makeCheckBox(tank.getDisplayName());
            tankRetireChecklist.put(tank.getSerialNumber(), tankCheckBox);
            equipmentRetirementSelectionPanel.add(tankCheckBox);
        }
        
        for (int i = 0; i < 2; ++i)
        {
            equipmentRetirementSelectionPanel.add(PWCGLabelFactory.makeDummyLabel());
        }

        return equipmentRetirementSelectionPanel;
    }

    private JPanel makeEquipmentChangeSelectionPanel() throws PWCGException
    {
        JPanel equipmentChangeSelectionPanel = new JPanel();
        equipmentChangeSelectionPanel.setOpaque(false);
        equipmentChangeSelectionPanel.setLayout(new BorderLayout());
        equipmentChangeSelectionPanel.setOpaque(false);

        JPanel equipmentManagementSelectionGrid = makeAircraftSelectionGrid();
        JPanel replacementAircraftSelectionPanel = makeReplacementAircraftSelectionPanel();

        equipmentChangeSelectionPanel.add(equipmentManagementSelectionGrid, BorderLayout.NORTH);
        equipmentChangeSelectionPanel.add(replacementAircraftSelectionPanel, BorderLayout.SOUTH);

        return equipmentChangeSelectionPanel;
    }

    private JPanel makeAircraftSelectionGrid() throws PWCGException
    {
        JPanel equipmentChangeSelectionGrid = new JPanel();
        equipmentChangeSelectionGrid.setOpaque(false);
        equipmentChangeSelectionGrid.setLayout(new GridLayout(0, 1));
        equipmentChangeSelectionGrid.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        CrewMember referencePlayer = campaign.getReferencePlayer();
        Equipment equipment = campaign.getEquipmentManager().getEquipmentForCompany(referencePlayer.getCompanyId());

        JLabel titleLabel = PWCGLabelFactory.makePaperLabelLarge("Request Type Change for Company Equipment");
        equipmentChangeSelectionGrid.add(titleLabel);

        for (int serialNumber : equipment.getActiveEquippedTanks().keySet())
        {
            EquippedTank tank = equipment.getEquippedTank(serialNumber);
            if (tank.isEquipmentRequest())
            {
                continue;
            }

            JCheckBox tankCheckBox = makeCheckBox(tank.getDisplayName());
            tankChangeChecklist.put(tank.getSerialNumber(), tankCheckBox);
            equipmentChangeSelectionGrid.add(tankCheckBox);
        }
        
        for (int i = 0; i < 1; ++i)
        {
            equipmentChangeSelectionGrid.add(PWCGLabelFactory.makeDummyLabel());
        }

        JPanel equipmentChangePanel = new JPanel(new BorderLayout());
        equipmentChangePanel.setOpaque(false);
        equipmentChangePanel.add(equipmentChangeSelectionGrid, BorderLayout.NORTH);

        return equipmentChangePanel;
    }

    private JPanel makeReplacementAircraftSelectionPanel() throws PWCGException
    {
        ITankTypeFactory tankTypeFactory = PWCGContext.getInstance().getPlayerTankTypeFactory();
        CrewMember referencePlayer = campaign.getReferencePlayer();
        ICountry country = CountryFactory.makeCountryByCountry(referencePlayer.getCountry());
        Company company = PWCGContext.getInstance().getCompanyManager().getCompany(referencePlayer.getCompanyId());
        PwcgRoleCategory roleCategory = company.getCompanyRoles().selectCompanyPrimaryRoleCategory(campaign.getDate());
        List<TankTypeInformation> availableTankTypes = tankTypeFactory.getAvailablePlayerTankTypes(country, roleCategory, campaign.getDate());        

        replacementTankTypeSelector = new JComboBox<String>();
        replacementTankTypeSelector.setOpaque(false);
        if (!availableTankTypes.isEmpty())
        {
            for (TankTypeInformation tankType : availableTankTypes)
            {
                replacementTankTypeSelector.addItem(tankType.getDisplayName());
            }
            replacementTankTypeSelector.setSelectedIndex(availableTankTypes.size()-1);
        }


        JLabel titleLabel = PWCGLabelFactory.makePaperLabelLarge("Select Tank Type To Convert To");

        JPanel replacementDropDownGrid = new JPanel(new GridLayout(0,1));
        replacementDropDownGrid.setOpaque(false);

        replacementDropDownGrid.add(titleLabel);
        replacementDropDownGrid.add(replacementTankTypeSelector);
                
        for (int i = 0; i < 2; ++i)
        {
            replacementDropDownGrid.add(PWCGLabelFactory.makeDummyLabel());
        }

        JPanel replacementDropDownPanel = new JPanel(new BorderLayout());
        replacementDropDownPanel.setOpaque(false);
        replacementDropDownPanel.add(replacementDropDownGrid, BorderLayout.NORTH);
        return replacementDropDownPanel;
    }

    private JCheckBox makeCheckBox(String buttonText) throws PWCGException 
    {
        Color fgColor = Color.BLACK;
        Font font = PWCGMonitorFonts.getPrimaryFont();
        JCheckBox checkBox = PWCGButtonFactory.makeCheckBox(buttonText, "", font, fgColor, null); 
        return checkBox;
    }

    private JPanel makeEquipmentSelectionConfirmationPanel() throws PWCGException
    {
        JPanel equipmentChaneConfirmationPanel = new JPanel();
        equipmentChaneConfirmationPanel.setOpaque(false);
        equipmentChaneConfirmationPanel.setLayout(new GridLayout(0, 3));

        equipmentChaneConfirmationPanel.add(PWCGLabelFactory.makeDummyLabel());

        JButton finishedButton = PWCGButtonFactory.makePaperButtonWithBorder("Process Equipment Requests", "ChangeEquipment", "Show change equipment screen",  this);
        equipmentChaneConfirmationPanel.add(finishedButton);

        equipmentChaneConfirmationPanel.add(PWCGLabelFactory.makeDummyLabel());

        return equipmentChaneConfirmationPanel;
    }

    private GridBagConstraints initializeGridbagConstraints()
    {
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.ipadx = 3;
        constraints.ipady = 3;
        constraints.anchor = GridBagConstraints.NORTHWEST;
        Insets margins = new Insets(0, 50, 50, 0);
        constraints.insets = margins;
        return constraints;
    }


    public void actionPerformed(ActionEvent ae)
    {
        try
        {
            String action = ae.getActionCommand();
            
            if (action.equalsIgnoreCase("Finished"))
            {
                campaign.write();                
                CampaignGuiContextManager.getInstance().popFromContextStack();
            }
            else if (action.equalsIgnoreCase("ChangeEquipment"))
            {
                processEquipmentRequests();
            }
        }
        catch (Exception e)
        {
            PWCGLogger.logException(e);
            ErrorDialog.internalError(e.getMessage());
        }
    }

    private void processEquipmentRequests() throws PWCGException
    {
        changeRequestedTanks();
        retireRequestedTanks();
        campaign.write();
        updateEquipmentChangeUI();
    }

    private void changeRequestedTanks() throws PWCGException
    {
        List<Integer> serialNumbersOfChangedTanks = new ArrayList<>();
        for (int serialNumber : tankChangeChecklist.keySet())
        {
            JCheckBox checkBox = tankChangeChecklist.get(serialNumber);
            if (checkBox.isSelected())
            {
                serialNumbersOfChangedTanks.add(serialNumber);
            }
        }

        if (!serialNumbersOfChangedTanks.isEmpty())
        {
            String tankTypeToChangeTo = (String) replacementTankTypeSelector.getSelectedItem();

            CrewMember referencePlayer = campaign.getReferencePlayer();
            Company company = PWCGContext.getInstance().getCompanyManager().getCompany(referencePlayer.getCompanyId());

            campaign.getEquipmentManager().actOnEquipmentRequest(company, serialNumbersOfChangedTanks, tankTypeToChangeTo);
        }
    }

    private void retireRequestedTanks() throws PWCGException
    {
        for (int serialNumber : tankRetireChecklist.keySet())
        {
            JCheckBox checkBox = tankRetireChecklist.get(serialNumber);
            if (!checkBox.isSelected())
            {
                continue;
            }

            campaign.getEquipmentManager().destroyTank(serialNumber, campaign.getDate());
        }
    }

    private void updateEquipmentChangeUI() throws PWCGException
    {
        tankRetireChecklist.clear();
        tankChangeChecklist.clear();
        replacementTankTypeSelector = null;

        this.removeAll();
        makePanels();
        
        this.setVisible(false);
        this.setVisible(true);
    }
}
