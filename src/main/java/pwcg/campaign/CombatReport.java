package pwcg.campaign;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CombatReport 
{
    private List<String> flightCrewMembers = new ArrayList<>();
    private Integer crewMemberSerialNumber = 0;
    private String reportCrewMemberName = "";
	private String company = "";
	private Date date;
	private String time = "";
	private String type = "";
	private String locality = "";
	private String duty = "";
	private String haReport = "";
	private String narrative = "";
		
	public CombatReport ()
	{
	}
	
	public void addFlightCrewMember(String crewMemberName)
	{
	    flightCrewMembers.add(crewMemberName);
	}
}
