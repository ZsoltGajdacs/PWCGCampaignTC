package pwcg.coop.model;

import lombok.Getter;
import lombok.Setter;

public class CoopPersonaOlde
{
    private String coopUser;
    @Getter
    @Setter
    private String campaignName;
    @Getter
    @Setter
    private int serialNumber;

    public String getCoopUsername()
    {
        return coopUser;
    }

    public void setCoopUsername(String coopUser)
    {
        this.coopUser = coopUser;
    }
}
