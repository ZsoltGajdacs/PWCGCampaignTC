package pwcg.gui.rofmap.campaignmap;

import java.awt.BasicStroke;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.geom.Line2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ToolTipManager;

import pwcg.campaign.Campaign;
import pwcg.campaign.api.Side;
import pwcg.campaign.battle.BattleLossSummary;
import pwcg.campaign.battle.CampaignBattleRecord;
import pwcg.campaign.battle.CampaignBattleRecordFilter;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.FrontLinePoint;
import pwcg.campaign.context.FrontLinesForMap;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.utils.PWCGLogger;
import pwcg.core.utils.MathUtils;
import pwcg.core.utils.PWCGPath;
import pwcg.gui.colors.ColorMap;
import pwcg.gui.image.ImageCache;
import pwcg.gui.rofmap.MapGUI;
import pwcg.gui.rofmap.MapPanelBase;

public class CampaignBattleMapPanel extends MapPanelBase
{
    private static final long serialVersionUID = 1L;

    private static final int RECENT_DAYS = 7;
    private static final int MARKER_RADIUS = 12;

    private Campaign campaign;
    private List<BattleMarker> battleMarkers = new ArrayList<>();

    public CampaignBattleMapPanel(MapGUI parent, Campaign campaign) throws PWCGException
    {
        super(parent);
        this.campaign = campaign;
        setToolTipText("");
        ToolTipManager.sharedInstance().registerComponent(this);
    }

    public void setData() throws PWCGException
    {
        setMapBackground(100);
        rebuildMarkers();
        repaint();
    }

    @Override
    public void increaseZoom()
    {
        super.increaseZoom();
        rebuildMarkersSafely();
    }

    @Override
    public void decreaseZoom()
    {
        super.decreaseZoom();
        rebuildMarkersSafely();
    }

    @Override
    public void paintComponent(Graphics g)
    {
        try
        {
            g.drawImage(image, 0, 0, null);
            paintCampaignFrontLines(g);
            drawBattleMarkers(g);
        }
        catch (Exception e)
        {
            PWCGLogger.logException(e);
        }
    }

    private void paintCampaignFrontLines(Graphics g) throws PWCGException
    {
        Graphics2D g2 = (Graphics2D) g;
        g2.setStroke(new BasicStroke(3));

        FrontLinesForMap frontLinesForMap = campaign.getFrontLinesForCampaign(parent.getMapDate());
        List<FrontLinePoint> frontLinesAllied = frontLinesForMap.getFrontLines(Side.ALLIED);
        Point prev = null;
        for (FrontLinePoint frontCoord : frontLinesAllied)
        {
            Point point = coordinateToPoint(frontCoord.getPosition());
            g2.setColor(ColorMap.RUSSIAN_RED);

            if (prev != null && shouldDrawLine(prev, point))
            {
                g2.draw(new Line2D.Float(prev.x, prev.y, point.x, point.y));
            }

            prev = point;
        }

        List<FrontLinePoint> frontLinesAxis = frontLinesForMap.getFrontLines(Side.AXIS);
        prev = null;
        for (FrontLinePoint frontCoord : frontLinesAxis)
        {
            Point point = coordinateToPoint(frontCoord.getPosition());
            g2.setColor(ColorMap.AXIS_BLACK);

            if (prev != null && shouldDrawLine(prev, point))
            {
                g2.draw(new Line2D.Float(prev.x, prev.y, point.x, point.y));
            }

            prev = point;
        }
    }

    private boolean shouldDrawLine(Point from, Point to) throws PWCGException
    {
        Coordinate coord1 = pointToCoordinate(from);
        Coordinate coord2 = pointToCoordinate(to);
        double distance = MathUtils.calcDist(coord1, coord2);
        return distance < 20000.0;
    }

    private void rebuildMarkers() throws PWCGException
    {
        battleMarkers.clear();
        List<CampaignBattleRecord> recentBattles = CampaignBattleRecordFilter.filterRecentBattles(campaign.getBattleRecords(), parent.getMapDate(), RECENT_DAYS);
        for (CampaignBattleRecord record : recentBattles)
        {
            if (record.getLocation() != null)
            {
                Point point = coordinateToPoint(record.getLocation());
                battleMarkers.add(new BattleMarker(record, point));
            }
        }
    }

    private void rebuildMarkersSafely()
    {
        try
        {
            rebuildMarkers();
        }
        catch (Exception e)
        {
            PWCGLogger.logException(e);
        }
    }

    private void drawBattleMarkers(Graphics g) throws PWCGException
    {
        for (BattleMarker marker : battleMarkers)
        {
            BufferedImage image = loadMarkerImage(marker.record);
            if (image == null)
            {
                continue;
            }

            int x = marker.point.x - (image.getWidth() / 2);
            int y = marker.point.y - (image.getHeight() / 2);
            g.drawImage(image, x, y, null);
        }
    }

    private BufferedImage loadMarkerImage(CampaignBattleRecord record) throws PWCGException
    {
        String imagePath = PWCGPath.join(PWCGContext.getInstance().getDirectoryManager().getPwcgImagesDir(), "Overlay/");
        if (isPlayerWin(record))
        {
            imagePath += "BlueArrow.png";
        }
        else
        {
            imagePath += "RedArrow.png";
        }

        return ImageCache.getInstance().getBufferedImage(imagePath);
    }

    private boolean isPlayerWin(CampaignBattleRecord record) throws PWCGException
    {
        Side playerSide = campaign.findReferencePlayer().determineCountry(parent.getMapDate()).getSide();
        return record.getWinningSide() == playerSide;
    }

    @Override
    public void mouseMovedCallback(MouseEvent e)
    {
        if (findMarker(e.getPoint()) != null)
        {
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        }
        else
        {
            setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
        }
    }

    @Override
    public String getToolTipText(MouseEvent event)
    {
        try
        {
            BattleMarker marker = findMarker(event.getPoint());
            if (marker == null)
            {
                return null;
            }
            return buildTooltip(marker.record);
        }
        catch (Exception e)
        {
            PWCGLogger.logException(e);
            return null;
        }
    }

    private String buildTooltip(CampaignBattleRecord record) throws PWCGException
    {
        Side playerSide = campaign.findReferencePlayer().determineCountry(parent.getMapDate()).getSide();
        BattleLossSummary friendlyLosses = playerSide == Side.ALLIED ? record.getAlliedLosses() : record.getAxisLosses();
        BattleLossSummary enemyLosses = playerSide == Side.ALLIED ? record.getAxisLosses() : record.getAlliedLosses();

        String resultText = isPlayerWin(record) ? "Victory" : "Defeat";
        return "<html>" +
                resultText + "<br>" +
                "Destroyed - Tanks: " + enemyLosses.getTanks() + ", Planes: " + enemyLosses.getPlanes() + ", Other: " + enemyLosses.getOther() + "<br>" +
                "Lost - Tanks: " + friendlyLosses.getTanks() + ", Planes: " + friendlyLosses.getPlanes() + ", Other: " + friendlyLosses.getOther() +
                "</html>";
    }

    @Override
    public void leftClickCallback(MouseEvent mouseEvent)
    {
        super.leftClickCallback(mouseEvent);
    }

    @Override
    public void rightClickCallback(MouseEvent e)
    {
    }

    @Override
    public void rightClickReleasedCallback(MouseEvent e)
    {
    }

    @Override
    public void centerClickCallback(MouseEvent e)
    {
    }

    @Override
    public Point upperLeft()
    {
        Point upperLeft = new Point(Integer.MAX_VALUE, Integer.MAX_VALUE);
        for (BattleMarker marker : battleMarkers)
        {
            if (marker.point.x < upperLeft.x)
            {
                upperLeft.x = marker.point.x;
            }
            if (marker.point.y < upperLeft.y)
            {
                upperLeft.y = marker.point.y;
            }
        }
        return upperLeft;
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
    }

    private BattleMarker findMarker(Point point)
    {
        for (BattleMarker marker : battleMarkers)
        {
            if (Math.abs(marker.point.x - point.x) <= MARKER_RADIUS && Math.abs(marker.point.y - point.y) <= MARKER_RADIUS)
            {
                return marker;
            }
        }
        return null;
    }

    private static class BattleMarker
    {
        private CampaignBattleRecord record;
        private Point point;

        private BattleMarker(CampaignBattleRecord record, Point point)
        {
            this.record = record;
            this.point = point;
        }
    }
}
