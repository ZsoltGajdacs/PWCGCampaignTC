package pwcg.gui;

import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import pwcg.core.exception.PWCGException;
import pwcg.gui.utils.ContextSpecificImages;

public class UiImageResolver
{

    public static String getImage(ScreenIdentifier screenIdentifier) throws PWCGException
    {
        String override = ScreenIdentifierOverrideManager.getInstance().getOverride(screenIdentifier);
        String imageFilePath = resolveImagePath(override);
        if (!imageExists(imageFilePath))
        {
            imageFilePath = resolveImagePath(screenIdentifier.getDefaultImageName());
            if (!imageExists(imageFilePath))
            {
                throw new PWCGException("Failed to locate image " + screenIdentifier.getDefaultImageName());
            }
            else
            {
                return imageFilePath;
            }
        }
        else
        {
            return imageFilePath;
        }
    }

    private static String resolveImagePath(String filename) throws PWCGException
    {
        if (filename == null || filename.isBlank())
        {
            return "NOT_FOUND";
        }

        // Overrides sometimes include a full/relative path. Prefer that if it exists.
        String directCandidate = resolveDirectPathCandidate(filename);
        if (imageExists(directCandidate))
        {
            return directCandidate;
        }

        String basename = Paths.get(filename).getFileName().toString();

        // Try exact matches first in a preferred order.
        String[] searchDirectories = getSearchDirectoriesInPriorityOrder();
        for (String directory : searchDirectories)
        {
            String candidate = resolveInDirectory(directory, filename);
            if (imageExists(candidate))
            {
                return candidate;
            }
        }

        // Then try case-insensitive lookup by basename as a Linux-friendly fallback.
        for (String directory : searchDirectories)
        {
            String candidate = findExistingPathIgnoringCase(directory, basename);
            if (imageExists(candidate))
            {
                return candidate;
            }
        }

        return "NOT_FOUND";
    }

    private static String[] getSearchDirectoriesInPriorityOrder() throws PWCGException
    {
        // Keep original semantics: first Menus and Misc.
        return new String[] {
                ContextSpecificImages.menuPathForMenus(),
                ContextSpecificImages.imagesMisc(),
                ContextSpecificImages.imagesNational(),
                ContextSpecificImages.imagesNewspaper(),
                ContextSpecificImages.imagesMedals(),
                ContextSpecificImages.imagesProfiles(),
                ContextSpecificImages.imagesMaps(),
                ContextSpecificImages.imagesCrewMemberPictures(),
                ContextSpecificImages.imagesPaperDoll(),
                ContextSpecificImages.imagesPlanes()
        };
    }

    private static String resolveInDirectory(String directory, String filename)
    {
        if (directory == null || directory.isBlank() || filename == null || filename.isBlank())
        {
            return "NOT_FOUND";
        }

        try
        {
            return Paths.get(directory).resolve(filename).toString();
        }
        catch (Exception e)
        {
            return "NOT_FOUND";
        }
    }

    private static String resolveDirectPathCandidate(String filename)
    {
        try
        {
            Path path = Paths.get(filename);
            return path.toString();
        }
        catch (Exception e)
        {
            // If the override is malformed for the platform, fall back to directory-based lookup.
            return "NOT_FOUND";
        }
    }

    private static String findExistingPathIgnoringCase(String directory, String expectedFileName)
    {
        if (directory == null || directory.isBlank() || expectedFileName == null || expectedFileName.isBlank())
        {
            return "NOT_FOUND";
        }

        Path dirPath;
        try
        {
            dirPath = Paths.get(directory);
        }
        catch (Exception e)
        {
            return "NOT_FOUND";
        }

        if (!Files.isDirectory(dirPath))
        {
            return "NOT_FOUND";
        }

        // Only scan the immediate directory (no recursion) to avoid surprising matches and keep it fast.
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(dirPath))
        {
            for (Path candidate : stream)
            {
                Path fileName = candidate.getFileName();
                if (fileName != null && fileName.toString().equalsIgnoreCase(expectedFileName) && Files.isRegularFile(candidate))
                {
                    return candidate.toString();
                }
            }
        }
        catch (IOException e)
        {
            // Treat I/O errors as not-found; caller will handle fallback/exception.
            return "NOT_FOUND";
        }

        return "NOT_FOUND";
    }

    private static boolean imageExists(String imagePath)
    {
        if (imagePath == null || imagePath.isBlank() || imagePath.equals("NOT_FOUND"))
        {
            return false;
        }

        try
        {
            // Prefer nio for correctness/cross-platform behavior.
            return Files.exists(Paths.get(imagePath));
        }
        catch (Exception e)
        {
            // Fall back for any odd path strings.
            File file = new File(imagePath);
            return file.exists();
        }
    }

}
