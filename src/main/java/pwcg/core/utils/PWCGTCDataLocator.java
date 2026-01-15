package pwcg.core.utils;

import java.io.IOException;
import java.io.InputStream;
import java.net.JarURLConnection;
import java.net.URI;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class PWCGTCDataLocator
{
    private static final String TCDATA_DIR = "TCData";
    private static final String MARKER_RESOURCE = "TCData/Input/Blocks.json";

    private PWCGTCDataLocator()
    {
    }

    public static String resolveTCDataDir()
    {
        URL markerUrl = getResource(MARKER_RESOURCE);
        if (markerUrl == null)
        {
            return null;
        }

        String protocol = markerUrl.getProtocol();
        if ("file".equalsIgnoreCase(protocol))
        {
            try
            {
                Path markerPath = Paths.get(markerUrl.toURI());
                Path tcDataDir = markerPath.getParent().getParent();
                return PWCGPath.ensureTrailingSlash(PWCGPath.normalize(tcDataDir.toAbsolutePath().toString()));
            }
            catch (Exception e)
            {
                return null;
            }
        }

        if ("jar".equalsIgnoreCase(protocol))
        {
            try
            {
                return extractFromJar(markerUrl);
            }
            catch (Exception e)
            {
                return null;
            }
        }

        return null;
    }

    private static URL getResource(String resource)
    {
        ClassLoader cl = Thread.currentThread().getContextClassLoader();
        if (cl == null)
        {
            cl = PWCGTCDataLocator.class.getClassLoader();
        }
        return cl.getResource(resource);
    }

    private static String extractFromJar(URL markerUrl) throws IOException
    {
        JarURLConnection connection = (JarURLConnection) markerUrl.openConnection();
        URL jarUrl = connection.getJarFileURL();

        Path extractDir = defaultExtractDir();
        Path markerOnDisk = extractDir.resolve("Input").resolve("Blocks.json");
        if (Files.isRegularFile(markerOnDisk))
        {
            return PWCGPath.ensureTrailingSlash(PWCGPath.normalize(extractDir.toAbsolutePath().toString()));
        }

        Files.createDirectories(extractDir);

        URI jarUri;
        try
        {
            jarUri = jarUrl.toURI();
        }
        catch (Exception e)
        {
            jarUri = URI.create(jarUrl.toString());
        }

        Path jarPath = Paths.get(jarUri);
        try (JarFile jarFile = new JarFile(jarPath.toFile()))
        {
            Enumeration<JarEntry> entries = jarFile.entries();
            while (entries.hasMoreElements())
            {
                JarEntry entry = entries.nextElement();
                String name = entry.getName();
                if (!name.startsWith(TCDATA_DIR + "/"))
                {
                    continue;
                }

                String relative = name.substring((TCDATA_DIR + "/").length());
                if (relative.isEmpty())
                {
                    continue;
                }

                Path outPath = extractDir.resolve(relative).normalize();
                if (!outPath.startsWith(extractDir))
                {
                    continue;
                }

                if (entry.isDirectory())
                {
                    Files.createDirectories(outPath);
                    continue;
                }

                Files.createDirectories(outPath.getParent());
                try (InputStream in = jarFile.getInputStream(entry))
                {
                    Files.copy(in, outPath, StandardCopyOption.REPLACE_EXISTING);
                }
            }
        }

        return PWCGPath.ensureTrailingSlash(PWCGPath.normalize(extractDir.toAbsolutePath().toString()));
    }

    private static Path defaultExtractDir()
    {
        String home = System.getProperty("user.home");
        if (home == null || home.trim().isEmpty())
        {
            home = System.getProperty("java.io.tmpdir");
        }

        return Paths.get(home, ".pwcg", "PWCGTC", TCDATA_DIR);
    }
}
