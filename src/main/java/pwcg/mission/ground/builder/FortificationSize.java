package pwcg.mission.ground.builder;

public enum FortificationSize
{
    SMALL(4, 0, 2, 1),
    MEDIUM(6, 1, 5, 2),
    LARGE(8, 4, 10, 4);

    private final int machineGuns;
    private final int aaGuns;
    private final int antiTankGuns;
    private final int artillery;

    FortificationSize(int machineGuns, int aaGuns, int antiTankGuns, int artillery)
    {
        this.machineGuns = machineGuns;
        this.aaGuns = aaGuns;
        this.antiTankGuns = antiTankGuns;
        this.artillery = artillery;
    }

    public int getMachineGuns()
    {
        return machineGuns;
    }

    public int getAaGuns()
    {
        return aaGuns;
    }

    public int getAntiTankGuns()
    {
        return antiTankGuns;
    }

    public int getArtillery()
    {
        return artillery;
    }
}
