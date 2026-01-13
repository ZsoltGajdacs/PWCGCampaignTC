package pwcg.gui.utils;

import pwcg.campaign.Campaign;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGDirectoryUserManager;
import pwcg.campaign.crewmember.CrewMember;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.PWCGPath;

public class ContextSpecificImages
{
    public static String menuPathForNation(Campaign campaign) throws PWCGException 
    {
		CrewMember referencePlayer = campaign.findReferencePlayer();
        String nationality = referencePlayer.determineCompany().getCountry().getNationality();
        String picPath = PWCGContext.getInstance().getDirectoryManager().getPwcgImagesDir() + "Menus" + File.separator + nationality + File.separator;

        return PWCGPath.normalize(picPath);
    }

    public static String menuPathForMenus() 
    {
        String picPath = PWCGContext.getInstance().getDirectoryManager().getPwcgImagesDir() + "Menus" + File.separator;

        return PWCGPath.normalize(picPath);
    }


    public static String imagesMaps() 
    {
        String picPath = PWCGContext.getInstance().getDirectoryManager().getPwcgImagesDir() + "Maps" + File.separator;

        return PWCGPath.normalize(picPath);
    }

    public static String imagesPaperDoll() 
    {
        String picPath = PWCGContext.getInstance().getDirectoryManager().getPwcgImagesDir() + "PaperDoll" + File.separator;

        return PWCGPath.normalize(picPath);
    }

    public static String imagesPlayerPaperDoll() 
    {
        String picPath = PWCGDirectoryUserManager.getInstance().getPwcgUserDir() + "PaperDoll" + File.separator;

        return PWCGPath.normalize(picPath);
    }

    public static String imagesMedals() 
    {
        String picPath = PWCGContext.getInstance().getDirectoryManager().getPwcgImagesDir() + "Medals" + File.separator;

        return PWCGPath.normalize(picPath);
    }

    public static String imagesMisc() 
    {
        String picPath = PWCGContext.getInstance().getDirectoryManager().getPwcgImagesDir() + "Misc" + File.separator;

        return PWCGPath.normalize(picPath);
    }

    public static String imagesNational() 
    {
        String picPath = PWCGContext.getInstance().getDirectoryManager().getPwcgImagesDir() + "National" + File.separator;

        return PWCGPath.normalize(picPath);
    }

    public static String imagesNewspaper() 
    {
        String picPath = PWCGContext.getInstance().getDirectoryManager().getPwcgImagesDir() + "Newspaper" + File.separator;

        return PWCGPath.normalize(picPath);
    }

    public static String imagesCrewMemberPictures() 
    {
        String picPath = PWCGContext.getInstance().getDirectoryManager().getPwcgImagesDir() + "CrewMemberPictures" + File.separator;

        return PWCGPath.normalize(picPath);
    }

    public static String imagesPlanes() 
    {
        String picPath = PWCGContext.getInstance().getDirectoryManager().getPwcgImagesDir() + "Tanks" + File.separator;

        return PWCGPath.normalize(picPath);
    }

    public static String imagesProfiles() 
    {
        String picPath = PWCGContext.getInstance().getDirectoryManager().getPwcgImagesDir() + "Profiles" + File.separator;

        return PWCGPath.normalize(picPath);
    }

}
