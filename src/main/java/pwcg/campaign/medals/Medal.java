package pwcg.campaign.medals;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Medal implements Comparable<Medal>
{
	private Date medalDate = null;
	private String medalName = "";
	private String medalImage = "";

	public Medal()
	{
	}

	public Medal(String medal, String image)
	{
		this.medalName = medal;
		this.medalImage = image;
	}

	@Override
	public int compareTo(Medal otherMedal)
	{
		if (this.medalDate.before(otherMedal.medalDate))
		{
			return -1;
		} else if (this.medalDate.after(otherMedal.medalDate))
		{
			return 1;
		}
		return 0;
	}
}
