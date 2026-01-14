package pwcg.gui.maingui;

import java.awt.Color;
import java.awt.Insets;

import javax.swing.UIManager;

import com.formdev.flatlaf.FlatLightLaf;

import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.utils.TestDriver;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.PWCGPath;
import pwcg.core.utils.PWCGLogger;
import pwcg.gui.colors.ColorMap;
import pwcg.gui.dialogs.ErrorDialog;

public class PwcgMain
{
	public static void main(String[] args) 
	{
        PWCGPath.applyRootFromArgs(args);
        PwcgMain pwcg = new PwcgMain();
        pwcg.startPWCGTC();
	}

	public PwcgMain() 
	{
	}
	

	private void startPWCGTC()
	{
        try
        {
            validatetestDriverNotEnabled();            
            setProduct();
            initializePWCGStaticData();
            setupUIManager();
            
            PwcgMainScreen campaignMainScreen = new PwcgMainScreen();
            campaignMainScreen.makePanels();
        }
        catch (Exception e)
        {
            PWCGLogger.logException(e);
        }
	}

    private void validatetestDriverNotEnabled()
    {
        TestDriver testDriver = TestDriver.getInstance();
        if (testDriver.isEnabled())
        {
            ErrorDialog.userError("PWCG test driver is enabled - PWCG will not function normally");
        }
    }

    private void setProduct() throws PWCGException
    {
        
    }

    private void initializePWCGStaticData()
    {
        PWCGContext.getInstance();
    }

    private void setupUIManager() throws PWCGException
    {
        try
        {
            // Initialize FlatLaf Look and Feel
            FlatLightLaf.setup();
        }
        catch (Exception e)
        {
            PWCGLogger.logException(e);
            // Fall back to system look and feel if FlatLaf fails
            try
            {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            }
            catch (Exception ex)
            {
                PWCGLogger.logException(ex);
            }
        }
        
        // Apply custom UI settings that work with FlatLaf
        Color tabSelectedColor = ColorMap.PAPER_BACKGROUND;
        UIManager.put("TabbedPane.selected", tabSelectedColor);
        UIManager.put("TabbedPane.contentOpaque", false);
        
        Insets insets = UIManager.getInsets("TabbedPane.contentBorderInsets");
        if (insets != null)
        {
            insets.top = -1;
            UIManager.put("TabbedPane.contentBorderInsets", insets);
        }
        
        UIManager.put("OptionPane.background", ColorMap.NEWSPAPER_BACKGROUND);
        UIManager.getLookAndFeelDefaults().put("Panel.background", ColorMap.NEWSPAPER_BACKGROUND);
    }
}
