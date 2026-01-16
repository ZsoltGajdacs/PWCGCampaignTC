package pwcg.gui.utils;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Rectangle;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import pwcg.gui.dialogs.PWCGMonitorSupport;
import pwcg.gui.utils.PWCGScrollPane;

public class PWCGFrame
{	
    private static final Dimension LOCKED_UI_SIZE = new Dimension(1920, 1080);
    private static PWCGFrame pwcgFrame = null;
    
    private JFrame frame = new JFrame();
    private JPanel base = new JPanel();
	
	public static PWCGFrame getInstance()
	{
		if (pwcgFrame == null)
		{
		    pwcgFrame = new PWCGFrame();
		}
		
		return pwcgFrame;
	}
	
	private PWCGFrame()
	{
		super();
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		Dimension screenSize = PWCGMonitorSupport.getPWCGMonitorSize();
		frame.setSize(screenSize);
		frame.setState(JFrame.MAXIMIZED_BOTH);		
		
        base.setLayout(new BorderLayout());
        base.setBackground(Color.DARK_GRAY);
        base.setPreferredSize(LOCKED_UI_SIZE);

        JPanel centeredPanel = new JPanel(new GridBagLayout());
        centeredPanel.setBackground(Color.DARK_GRAY);

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.fill = GridBagConstraints.NONE;
        constraints.anchor = GridBagConstraints.CENTER;
        centeredPanel.add(base, constraints);

        JScrollPane scrollPane = new PWCGScrollPane(centeredPanel);
        scrollPane.getViewport().setBackground(Color.DARK_GRAY);
		
        frame.setVisible(false);
        frame.add(scrollPane);
  	}

	public void setPanel(JPanel newPanel)
	{
        base.removeAll();
	    base.add(newPanel, BorderLayout.CENTER);
	    base.revalidate();
	    base.repaint();
	    
	    if (!frame.isVisible())
	    {
	        frame.setVisible(true);
	    }
	}

    public Rectangle getBounds()
    {
        return frame.getBounds();
    }
}
