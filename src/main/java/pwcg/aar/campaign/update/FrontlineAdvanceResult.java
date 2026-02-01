package pwcg.aar.campaign.update;

import pwcg.campaign.api.Side;

public class FrontlineAdvanceResult
{
    private final Side advancingSide;
    private final double advanceDistanceMeters;
    private final double killLossRatio;
    private final double wipeRatio;

    public FrontlineAdvanceResult(Side advancingSide, double advanceDistanceMeters, double killLossRatio, double wipeRatio)
    {
        this.advancingSide = advancingSide;
        this.advanceDistanceMeters = advanceDistanceMeters;
        this.killLossRatio = killLossRatio;
        this.wipeRatio = wipeRatio;
    }

    public static FrontlineAdvanceResult noAdvance()
    {
        return new FrontlineAdvanceResult(null, 0.0, 0.0, 0.0);
    }

    public Side getAdvancingSide()
    {
        return advancingSide;
    }

    public double getAdvanceDistanceMeters()
    {
        return advanceDistanceMeters;
    }

    public double getKillLossRatio()
    {
        return killLossRatio;
    }

    public double getWipeRatio()
    {
        return wipeRatio;
    }

    public boolean shouldAdvance()
    {
        return advancingSide != null && advanceDistanceMeters > 0.0;
    }
}