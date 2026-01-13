package pwcg.core.utils;

import java.io.File;

public class PWCGPath
{
    public static final String ROOT_PROPERTY = "pwcg.root";
    public static final String ROOT_ENV = "PWCG_ROOT";

    private PWCGPath()
    {
    }

    public static void applyRootFromArgs(String[] args)
    {
        if (args == null)
        {
            return;
        }

        for (int i = 0; i < args.length; ++i)
        {
            String arg = args[i];
            if (arg == null)
            {
                continue;
            }

            if (arg.equals("--root") && (i + 1) < args.length)
            {
                setRootDir(args[i + 1]);
                return;
            }

            if (arg.startsWith("--root=") && arg.length() > "--root=".length())
            {
                setRootDir(arg.substring("--root=".length()));
                return;
            }
        }
    }

    public static void setRootDir(String rootDir)
    {
        if (rootDir == null)
        {
            return;
        }

        String trimmed = rootDir.trim();
        if (trimmed.isEmpty())
        {
            return;
        }

        System.setProperty(ROOT_PROPERTY, trimmed);
    }

    public static String getRootDir()
    {
        String rootDir = System.getProperty(ROOT_PROPERTY);
        if (rootDir == null || rootDir.trim().isEmpty())
        {
            rootDir = System.getenv(ROOT_ENV);
        }
        if (rootDir == null || rootDir.trim().isEmpty())
        {
            rootDir = System.getProperty("user.dir");
        }

        File rootFile = new File(rootDir);
        String absoluteRoot = rootFile.getAbsolutePath();
        return ensureTrailingSlash(normalize(absoluteRoot));
    }

    public static String getRootDirNoTrailingSlash()
    {
        String rootDir = getRootDir();
        if (rootDir.endsWith("/"))
        {
            return rootDir.substring(0, rootDir.length() - 1);
        }
        return rootDir;
    }

    public static String normalize(String path)
    {
        if (path == null)
        {
            return null;
        }
        return path.replace('\\', System.getProperty("file.separator").charAt(0))
                   .replace('/', System.getProperty("file.separator").charAt(0));
    }

    public static String ensureTrailingSlash(String path)
    {
        if (path == null || path.isEmpty())
        {
            return path;
        }

        String normalized = normalize(path);
        if (normalized.endsWith("/"))
        {
            return normalized;
        }

        return normalized + "/";
    }

    public static String join(String baseDirWithOrWithoutTrailingSlash, String child)
    {
        if (baseDirWithOrWithoutTrailingSlash == null)
        {
            return normalize(child);
        }

        String base = ensureTrailingSlash(baseDirWithOrWithoutTrailingSlash);
        if (child == null || child.isEmpty())
        {
            return base;
        }

        String c = normalize(child);
        while (c.startsWith("/"))
        {
            c = c.substring(1);
        }

        return base + c;
    }
}
