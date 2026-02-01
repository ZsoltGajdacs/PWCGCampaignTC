package pwcg.mission.options;

import pwcg.core.utils.RandomNumberGenerator;

public enum BattleMissionType
{
    ATTACK("Attack"),
    DEFENSE("Defense"),
    ATTACK_FORTIFICATION("Attack Fortification"),
    DEFEND_FORTIFICATION("Defend Fortification");

    private final String description;

    private BattleMissionType(String description)
    {
        this.description = description;
    }

    public String getDescription()
    {
        return description;
    }

    public static BattleMissionType fromDescription(String description)
    {
        if (description == null)
        {
            return null;
        }

        for (BattleMissionType missionType : values())
        {
            if (missionType.description.equalsIgnoreCase(description))
            {
                return missionType;
            }
        }

        return null;
    }

    public static BattleMissionType getRandomBattleType()
    {
        int roll = RandomNumberGenerator.getRandom(values().length);
        return values()[roll];
    }
}
