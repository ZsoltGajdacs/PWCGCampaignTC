
package pwcg.mission.mcu;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.PWCGLogger;

@Getter
@Setter
public class McuMedia extends BaseFlightMcu
{
	public static final int MEDIA_TYPE_UNKNOWN = -1;
	public static final int MEDIA_TYPE_START = 2;
	public static final int MEDIA_TYPE_STOP = 4;
	
	private int enabled = 1;
	private String config = "swf\\photorecon.gfx";
	private int totalTime = 10000;
	private int expandTime = 1;
	private int fadeTime = 1;
	private int opacity = 100;
	private int rColor = 255;
	private int gColor = 255;
	private int bColor = 255;
	private int mediaType = MEDIA_TYPE_UNKNOWN;
	private List<McuEvent> eventList = new ArrayList<McuEvent>();

    public McuMedia ()
    {
    }

    public McuMedia (int mediaType)
    {
        super();
        this.mediaType = mediaType;
    }

	public void write(BufferedWriter writer) throws PWCGException
	{
		try
        {
            writer.write("MCU_TR_Media");
            writer.newLine();
            writer.write("{");
            writer.newLine();
            
            super.write(writer);

            writer.write("  Enabled = " + enabled + ";");
            writer.newLine();
            writer.write("  Config = \"" + config + "\";");
            writer.newLine();
            writer.write("  TotalTime = " + totalTime + ";");
            writer.newLine();
            writer.write("  ExpandTime = " + expandTime + ";");
            writer.newLine();
            writer.write("  FadeTime = " + fadeTime + ";");
            writer.newLine();
            writer.write("  Opacity = " + opacity + ";");
            writer.newLine();
            writer.write("  RColor = " + rColor + ";");
            writer.newLine();
            writer.write("  GColor = " + gColor + ";");
            writer.newLine();
            writer.write("  BColor = " + bColor + ";");
            writer.newLine();
            
            writer.write("  MediaType = " + mediaType + ";");
            writer.newLine();

            if (eventList.size() > 0)
            {
            	writer.write("  OnEvents");
            	writer.newLine();
            	writer.write("  {");
            	writer.newLine();
            	for (McuEvent event : eventList)
            	{
            		writer.write("      OnEvent");
            		writer.newLine();
            		writer.write("      {");
            		writer.newLine();
            		writer.write("          Type = " + event.getType() + ";");
            		writer.newLine();
            		writer.write("          TarId = " + event.getTarId() + ";");
            		writer.newLine();
            		writer.write("      }");
            		writer.newLine();
            		
            	}
            	writer.write("  }");
            	writer.newLine();
            }
            
            writer.write("}");
            writer.newLine();
            writer.newLine();
            writer.newLine();
        }
        catch (IOException e)
        {
            PWCGLogger.logException(e);
            throw new PWCGException(e.getMessage());
        }
	}

	public void addEvent(McuEvent event) {
		this.eventList.add(event);
	}
	
}
