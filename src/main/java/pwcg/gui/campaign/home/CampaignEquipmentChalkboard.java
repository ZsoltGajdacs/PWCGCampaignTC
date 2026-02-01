package pwcg.gui.campaign.home;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import pwcg.campaign.ArmedService;
import pwcg.campaign.Campaign;
import pwcg.campaign.company.Company;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.crewmember.CrewMember;
import pwcg.campaign.resupply.depot.EquipmentDepot;
import pwcg.campaign.tank.EquippedTank;
import pwcg.campaign.tank.TankSorter;
import pwcg.core.config.InternationalizationManager;
import pwcg.core.exception.PWCGException;
import pwcg.gui.ScreenIdentifier;
import pwcg.gui.UiImageResolver;
import pwcg.gui.colors.ColorMap;
import pwcg.gui.dialogs.PWCGMonitorFonts;
import pwcg.gui.utils.ImageResizingPanel;
import pwcg.gui.utils.PWCGLabelFactory;
import pwcg.gui.utils.PwcgBorderFactory;

public class CampaignEquipmentChalkboard extends ImageResizingPanel
{
    private static final long serialVersionUID = 1L;
    
    private Campaign campaign;
    
    public CampaignEquipmentChalkboard(Campaign campaign)
    {
        super("");
        this.setLayout(new BorderLayout());
        this.setOpaque(false);

        this.campaign = campaign;
    }
    
    public void makePanels() throws PWCGException
    {
        String imagePath = UiImageResolver.getImage(ScreenIdentifier.CampaignEquipmentChalkboard);
        this.setImageFromName(imagePath);
        this.setBorder(PwcgBorderFactory.createCampaignHomeChalkboardBoxBorder());

        CrewMember referencePlayer = campaign.findReferencePlayer();
        Map<Integer, EquippedTank> tanksForCompany = campaign.getEquipmentManager().getEquipmentForCompany(referencePlayer.getCompanyId()).getActiveEquippedTanks();

        JPanel equipmentPanel = createEquipmentListPanel(campaign, tanksForCompany, referencePlayer.getCompanyId());
        this.add(equipmentPanel, BorderLayout.CENTER);
    }

    private JPanel createEquipmentListPanel(Campaign campaign, Map<Integer, EquippedTank> tanksForCompany, int companyId) throws PWCGException
    {
        List<EquippedTank> sortedTanksOnInventory = TankSorter.sortEquippedTanksByGoodness(new ArrayList<EquippedTank>(tanksForCompany.values()));

        Font font = PWCGMonitorFonts.getChalkboardFont();

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.ipadx = 3;
        constraints.ipady = 3;
        GridBagLayout equipmentLayout = new GridBagLayout();

        JPanel equipmentChalkboardPanel = new JPanel();
        equipmentChalkboardPanel.setOpaque(false);
        equipmentChalkboardPanel.setLayout(equipmentLayout);

        constraints.weightx = 0.15;
        constraints.gridx = 0;
        constraints.gridy = 0;
        equipmentChalkboardPanel.add(PWCGLabelFactory.makeDummyLabel(), constraints);

        String tankTypeLabelText = InternationalizationManager.getTranslation("Tank Type");
        JLabel tankTypeLabel = PWCGLabelFactory.makeTransparentLabel(tankTypeLabelText, ColorMap.CHALK_FOREGROUND, font, SwingConstants.LEFT);
        constraints.weightx = 0.15;
        constraints.gridx = 1;
        constraints.gridy = 0;
        equipmentChalkboardPanel.add(tankTypeLabel, constraints);

        String lSerialNumberText = InternationalizationManager.getTranslation("Serial Number");
        JLabel lSerialNumber = PWCGLabelFactory.makeTransparentLabel(lSerialNumberText, ColorMap.CHALK_FOREGROUND, font, SwingConstants.RIGHT);
        constraints.weightx = 0.1;
        constraints.gridx = 2;
        constraints.gridy = 0;
        equipmentChalkboardPanel.add(lSerialNumber, constraints);

        String lTankIdCodeText = InternationalizationManager.getTranslation("ID Code");
        JLabel lTankIdCode = PWCGLabelFactory.makeTransparentLabel(lTankIdCodeText, ColorMap.CHALK_FOREGROUND, font, SwingConstants.RIGHT);
        constraints.weightx = 0.1;
        constraints.gridx = 3;
        constraints.gridy = 0;
        equipmentChalkboardPanel.add(lTankIdCode, constraints);

        constraints.gridx = 4;
        constraints.gridy = 0;
        equipmentChalkboardPanel.add(PWCGLabelFactory.makeDummyLabel(), constraints);

        int i = 1;
        for (EquippedTank tank : sortedTanksOnInventory)
        {
            constraints.weightx = 0.15;
            constraints.gridx = 0;
            constraints.gridy = i;
            equipmentChalkboardPanel.add(PWCGLabelFactory.makeDummyLabel(), constraints);

            JLabel tankNameLabel = PWCGLabelFactory.makeTransparentLabel(tank.getDisplayName(), ColorMap.CHALK_FOREGROUND, font, SwingConstants.LEFT);
            constraints.weightx = 0.15;
            constraints.gridx = 1;
            constraints.gridy = i;
            equipmentChalkboardPanel.add(tankNameLabel, constraints);

            JLabel tankSerialNumberLabel = PWCGLabelFactory.makeTransparentLabel("" + tank.getSerialNumber(), ColorMap.CHALK_FOREGROUND, font, SwingConstants.RIGHT);
            constraints.weightx = 0.1;
            constraints.gridx = 2;
            constraints.gridy = i;
            equipmentChalkboardPanel.add(tankSerialNumberLabel, constraints);

            constraints.gridx = 4;
            constraints.gridy = i;
            equipmentChalkboardPanel.add(PWCGLabelFactory.makeDummyLabel(), constraints);

            ++i;
        }

        addEquipmentSummary(equipmentChalkboardPanel, constraints, font, tanksForCompany.size(), companyId, i);

        return equipmentChalkboardPanel;
    }

    private void addEquipmentSummary(JPanel panel, GridBagConstraints constraints, Font font, int companyEquipmentCount, int companyId, int startRow) throws PWCGException
    {
        int depotCount = getDepotCountForCompany(companyId);

        int row = startRow + 1;

        constraints.weightx = 0.15;
        constraints.gridx = 0;
        constraints.gridy = row;
        panel.add(PWCGLabelFactory.makeDummyLabel(), constraints);

        String summaryText = InternationalizationManager.getTranslation("Company Equipment");
        summaryText += ": " + companyEquipmentCount;
        JLabel companyCountLabel = PWCGLabelFactory.makeTransparentLabel(summaryText, ColorMap.CHALK_FOREGROUND, font, SwingConstants.LEFT);
        constraints.weightx = 0.35;
        constraints.gridx = 1;
        constraints.gridy = row;
        constraints.gridwidth = 2;
        panel.add(companyCountLabel, constraints);

        row++;
        constraints.gridwidth = 1;

        constraints.weightx = 0.15;
        constraints.gridx = 0;
        constraints.gridy = row;
        panel.add(PWCGLabelFactory.makeDummyLabel(), constraints);

        String depotText = InternationalizationManager.getTranslation("Depot Reserves");
        depotText += ": " + depotCount;
        JLabel depotCountLabel = PWCGLabelFactory.makeTransparentLabel(depotText, ColorMap.CHALK_FOREGROUND, font, SwingConstants.LEFT);
        constraints.weightx = 0.35;
        constraints.gridx = 1;
        constraints.gridy = row;
        constraints.gridwidth = 2;
        panel.add(depotCountLabel, constraints);

        row++;
        constraints.gridwidth = 1;

        constraints.weightx = 0.15;
        constraints.gridx = 0;
        constraints.gridy = row;
        panel.add(PWCGLabelFactory.makeDummyLabel(), constraints);

        String noteText = InternationalizationManager.getTranslation("(Depot is for replacements after losses)");
        JLabel noteLabel = PWCGLabelFactory.makeTransparentLabel(noteText, ColorMap.CHALK_FOREGROUND, font, SwingConstants.LEFT);
        constraints.weightx = 0.35;
        constraints.gridx = 1;
        constraints.gridy = row;
        constraints.gridwidth = 2;
        panel.add(noteLabel, constraints);
    }

    private int getDepotCountForCompany(int companyId) throws PWCGException
    {
        Company company = PWCGContext.getInstance().getCompanyManager().getCompany(companyId);
        ArmedService service = company.determineServiceForCompany(campaign.getDate());
        EquipmentDepot depot = campaign.getEquipmentManager().getEquipmentDepotForService(service.getServiceId());
        return depot.getDepotSize();
    }
}
