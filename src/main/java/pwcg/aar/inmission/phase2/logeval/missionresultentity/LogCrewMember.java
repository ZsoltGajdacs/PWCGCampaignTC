package pwcg.aar.inmission.phase2.logeval.missionresultentity;

import java.io.BufferedWriter;
import java.io.IOException;

import lombok.Getter;
import lombok.Setter;
import pwcg.campaign.crewmember.CrewMemberStatus;
import pwcg.campaign.crewmember.SerialNumber;

@Getter
@Setter
public class LogCrewMember
{
    private int serialNumber = SerialNumber.NO_SERIAL_NUMBER;
    private String botId = "";
    private double damageLevel = 0.0;
    private int status = CrewMemberStatus.STATUS_ACTIVE;

    public LogCrewMember()
    {
    }

    public void dump(BufferedWriter writer) throws IOException
    {
        writer.write("        Bot   : " + getBotId());
        writer.newLine();
        writer.write("        Status: " + getStatus());
        writer.newLine();
        writer.newLine();
    }
}
