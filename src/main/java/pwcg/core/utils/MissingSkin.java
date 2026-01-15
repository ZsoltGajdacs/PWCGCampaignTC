package pwcg.core.utils;

import lombok.Getter;
import lombok.Setter;

public class MissingSkin
{
    private String planeType = "";
    @Getter
    @Setter
    private String category = "";
    @Getter
    @Setter
    private String skinName = "";

    public String getTankType()
    {
        return planeType;
    }

    public void setTankType(String planeType)
    {
        this.planeType = planeType;
    }

}
