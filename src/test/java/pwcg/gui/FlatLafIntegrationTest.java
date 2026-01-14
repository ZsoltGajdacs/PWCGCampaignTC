package pwcg.gui;

import static org.junit.jupiter.api.Assertions.*;

import javax.swing.UIManager;

import org.junit.jupiter.api.Test;

import com.formdev.flatlaf.FlatLightLaf;

public class FlatLafIntegrationTest 
{
    @Test
    public void testFlatLafCanBeInitialized() 
    {
        // Test that FlatLaf can be set up without errors
        boolean result = FlatLightLaf.setup();
        assertTrue(result, "FlatLaf should be successfully initialized");
    }
    
    @Test
    public void testFlatLafIsAvailable()
    {
        // Test that FlatLaf can be instantiated
        try
        {
            FlatLightLaf laf = new FlatLightLaf();
            assertNotNull(laf, "FlatLightLaf instance should be created successfully");
        }
        catch (Exception e)
        {
            fail("FlatLightLaf should be instantiable: " + e.getMessage());
        }
    }
    
    @Test
    public void testLookAndFeelAfterSetup()
    {
        // Initialize FlatLaf
        FlatLightLaf.setup();
        
        // Verify that the Look and Feel is an instance of FlatLightLaf
        assertTrue(UIManager.getLookAndFeel() instanceof FlatLightLaf,
                   "Look and Feel should be FlatLightLaf, but was: " + 
                   UIManager.getLookAndFeel().getClass().getName());
    }
}
