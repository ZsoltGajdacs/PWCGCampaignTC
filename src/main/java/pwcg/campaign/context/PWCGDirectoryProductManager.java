package pwcg.campaign.context;

import pwcg.core.utils.PWCGPath;

public class PWCGDirectoryProductManager
{
    private String pwcgRootDir;
    private String pwcgDataDir;

    public PWCGDirectoryProductManager(PWCGProduct product)
    {
        createPwcgDataDir(product);
    }

    private void createPwcgDataDir(PWCGProduct product)
    {
        pwcgRootDir = PWCGPath.getRootDir();
        pwcgDataDir = PWCGPath.join(pwcgRootDir, "TCData/");
    }

    public String getPwcgRootDir()
    {
        return pwcgRootDir;
    }

    public String getPwcgProductDataDir()
    {
        return pwcgDataDir;
    }

    public String getPwcgInputDir()
    {
        return PWCGPath.join(pwcgDataDir, "Input/");
    }

    public String getPwcgReportDir()
    {
        return PWCGPath.join(pwcgRootDir, "Report/");
    }

    public String getPwcgAircraftInfoDir()
    {
        return PWCGPath.join(getPwcgInputDir(), "Aircraft/");
    }

    public String getPwcgTankInfoDir()
    {
        return PWCGPath.join(getPwcgInputDir(), "Tanks/");
    }

    public String getPwcgAirfieldHotSpotsDir()
    {
        return PWCGPath.join(getPwcgInputDir(), "AirfieldHotSpots/");
    }

    public String getPwcgConfigurationDir()
    {
        return PWCGPath.join(getPwcgInputDir(), "Configuration/");
    }

    public String getPwcgCompanyDir()
    {
        return PWCGPath.join(getPwcgInputDir(), "Company/");
    }

    public String getPwcgCompanyMovingFrontDir()
    {
        return PWCGPath.join(getPwcgInputDir(), "CompanyMovingFront/");
    }

    public String getPwcgImagesDir()
    {
        return PWCGPath.join(pwcgDataDir, "Images/");
    }

    public String getPwcgNamesDir()
    {
        return PWCGPath.join(pwcgDataDir, "Names/");
    }

    public String getPwcgSkinsDir()
    {
        return PWCGPath.join(getPwcgInputDir(), "Skins/");
    }

    public String getPwcgVehiclesDir()
    {
        return PWCGPath.join(getPwcgInputDir(), "Vehicles/");
    }

    public String getPwcgStaticObjectDir()
    {
        return PWCGPath.join(getPwcgInputDir(), "StaticObjects/");
    }

    public String getPwcgAcesDir()
    {
        return PWCGPath.join(getPwcgInputDir(), "Aces/");
    }

    public String getPwcgNewspaperDir()
    {
        return PWCGPath.join(getPwcgInputDir(), "Newspapers/");
    }

    public String getPwcgInternationalizationDir()
    {
        return PWCGPath.join(getPwcgInputDir(), "International/");
    }
}
