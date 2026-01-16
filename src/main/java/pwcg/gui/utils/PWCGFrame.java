package pwcg.gui.utils;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
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
    private JScrollPane scrollPane;
	
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
        base.setMinimumSize(LOCKED_UI_SIZE);
        base.setMaximumSize(LOCKED_UI_SIZE);

        JPanel centeredPanel = new FixedCenteringPanel(base, LOCKED_UI_SIZE);
        centeredPanel.setBackground(Color.DARK_GRAY);

        scrollPane = new PWCGScrollPane(centeredPanel);
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

    private static class FixedCenteringPanel extends JPanel
    {
        private static final long serialVersionUID = 1L;
        private final Dimension lockedSize;

        public FixedCenteringPanel(JPanel contentPanel, Dimension targetSize)
        {
            super(new GridBagLayout());
            this.lockedSize = new Dimension(targetSize);

            GridBagConstraints constraints = new GridBagConstraints();
            constraints.gridx = 0;
            constraints.gridy = 0;
            constraints.weightx = 1.0;
            constraints.weighty = 1.0;
            constraints.anchor = GridBagConstraints.CENTER;
            add(contentPanel, constraints);
        }

        @Override
        public Dimension getPreferredSize()
        {
            int width = lockedSize.width;
            int height = lockedSize.height;
            Container parent = getParent();
            if (parent != null)
            {
                width = Math.max(width, parent.getWidth());
                height = Math.max(height, parent.getHeight());
            }
            return new Dimension(width, height);
        }
    }
}
