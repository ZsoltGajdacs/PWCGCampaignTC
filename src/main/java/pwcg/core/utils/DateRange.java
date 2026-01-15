package pwcg.core.utils;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class DateRange
{
    private Date startDate;
    private Date endDate;
    
    public boolean isInDateRange(Date date)
    {
        if (date.before(startDate))
        {
            return false;
        }
       if (date.after(endDate))
       {
           return false;
        }
        
        return true;
    }
}
