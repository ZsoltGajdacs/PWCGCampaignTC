package pwcg.campaign.tank;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CompanyTankAssignment implements Cloneable
{
    protected String archType = "";
    protected Date introduction;
    protected Date withdrawal;

    public CompanyTankAssignment()
    {
    }

    public CompanyTankAssignment copy()
    {
        CompanyTankAssignment planeAssignment = new CompanyTankAssignment();
        
        planeAssignment.archType = this.archType;
        planeAssignment.introduction = this.introduction;
        planeAssignment.withdrawal = this.withdrawal;

        return planeAssignment;
    }

    public Date getCompanyIntroduction()
    {
        return introduction;
    }

    public void setCompanyIntroduction(Date introduction)
    {
        this.introduction = introduction;
    }

    public Date getCompanyWithdrawal()
    {
        return withdrawal;
    }

    public void setCompanyWithdrawal(Date withdrawal)
    {
        this.withdrawal = withdrawal;
    }
}
