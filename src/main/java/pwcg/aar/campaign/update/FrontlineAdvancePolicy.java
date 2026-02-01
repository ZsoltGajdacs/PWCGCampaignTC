package pwcg.aar.campaign.update;

public class FrontlineAdvancePolicy
{
    public static final int TANK_WEIGHT = 3;
    public static final int PLANE_WEIGHT = 2;
    public static final int OTHER_WEIGHT = 1;

    public static final double BREAKTHROUGH_RATIO = 3.0;
    public static final double ADVANCE_RATIO = 2.0;

    public static final double BREAKTHROUGH_DISTANCE_METERS = 20000.0;
    public static final double ADVANCE_DISTANCE_METERS = 10000.0;

    public static final double BREAKTHROUGH_WIPE_THRESHOLD = 0.70;

    private FrontlineAdvancePolicy()
    {
    }
}