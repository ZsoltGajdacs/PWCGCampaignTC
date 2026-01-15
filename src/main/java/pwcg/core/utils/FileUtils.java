package pwcg.core.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.util.ArrayList;
import java.util.List;

import pwcg.core.exception.PWCGException;

public class FileUtils
{

    private static String normalizePath(String path)
    {
        return PWCGPath.normalize(path);
    }

    public static boolean createDirIfNeeded(String campaignConfigDir)
    {
        boolean exists = true;
        File dir = new File(normalizePath(campaignConfigDir));
        if (!dir.exists())
        {
            exists = dir.mkdirs();
        }
        return exists;
    }

    public static void deleteRecursive(String deleteDir)
    {
        File deleteRoot = new File(normalizePath(deleteDir));
        if (deleteRoot.exists())
        {
            deleteFile(deleteRoot.getAbsolutePath());
        }
    }

    public static void deleteFilesInDirectory(String directoryName) throws PWCGException
    {
        deleteRecursive(directoryName);
        File directory = new File(normalizePath(directoryName));
        directory.mkdir();
    }

    public static boolean findInDirectory(String directoryName, String lookForFileName) throws PWCGException
    {
        File directory = new File(normalizePath(directoryName));
        if(!directory.exists())
        {
            return false;
        }
        
        lookForFileName = lookForFileName.toLowerCase();
        for (String fileName : directory.list())
        {
            fileName = fileName.toLowerCase();
            if (fileName.contains(lookForFileName))
            {
                return true;
            }
        }
        return false;
    }

    public static boolean fileExists(String filePath)
    {
        File file = new File(normalizePath(filePath));
        if (file.exists())
        {
            return true;
        }

        return false;
    }

    public static Path resolvePathCaseInsensitive(Path baseDir, String relativePath)
    {
        if (baseDir == null)
        {
            return null;
        }

        if (relativePath == null || relativePath.trim().isEmpty())
        {
            return baseDir;
        }

        Path relative = Paths.get(PWCGPath.normalize(relativePath));
        return resolvePathCaseInsensitive(baseDir, relative);
    }

    public static Path resolvePathCaseInsensitive(Path baseDir, Path relativePath)
    {
        if (baseDir == null)
        {
            return null;
        }

        if (relativePath == null)
        {
            return baseDir;
        }

        Path direct = baseDir.resolve(relativePath);
        if (Files.exists(direct))
        {
            return direct;
        }

        Path current = baseDir;
        for (Path nameElement : relativePath)
        {
            String expectedName = nameElement.toString();
            Path next = current.resolve(expectedName);
            if (Files.exists(next))
            {
                current = next;
                continue;
            }

            if (!Files.isDirectory(current))
            {
                return baseDir.resolve(relativePath);
            }

            Path matched = null;
            try (var stream = Files.list(current))
            {
                matched = stream
                    .filter(p -> p.getFileName() != null)
                    .filter(p -> p.getFileName().toString().equalsIgnoreCase(expectedName))
                    .findFirst()
                    .orElse(null);
            }
            catch (Exception e)
            {
                return baseDir.resolve(relativePath);
            }

            if (matched == null)
            {
                return baseDir.resolve(relativePath);
            }

            current = matched;
        }

        return current;
    }

    public static void deleteFile(String sFilePath)
    {
        File oFile = new File(normalizePath(sFilePath));
        if (oFile.isDirectory())
        {
            File[] aFiles = oFile.listFiles();
            for (File oFileCur : aFiles)
            {
                deleteFile(oFileCur.getAbsolutePath());
            }
        }
        oFile.delete();

    }

    public static void deleteFilesByFileName(List<String> filesToDelete)
    {
        for (String pathname : filesToDelete)
        {
            File file = new File(normalizePath(pathname));
            if (file.exists())
            {
                file.delete();
            }
        }
    }

    public static List<File> getFilesInDirectory(String directory) throws PWCGException
    {
        List<File> filesInDirectory = new ArrayList<>();
        File directoryFile = new File(normalizePath(directory));
        if (directoryFile.exists())
        {
            if (directoryFile.isDirectory())
            {
                for (File file : directoryFile.listFiles())
                {
                    if (!file.isDirectory())
                    {
                        filesInDirectory.add(file);
                    }
                }
            }
        }

        return filesInDirectory;
    }

    public static List<File> getFilesWithFilter(String directory, String filterString) throws PWCGException
    {
        List<File> matchingFiles = new ArrayList<>();
        File directoryFile = new File(normalizePath(directory));
        if (directoryFile.exists())
        {
            if (directoryFile.isDirectory())
            {
                for (File file : directoryFile.listFiles())
                {
                    FilenameFilter filter = new PwcgFileNameFilter(filterString);
                    if (filter.accept(directoryFile, file.getName()))
                    {
                        matchingFiles.add(file);
                    }
                }
            }
        }

        return matchingFiles;
    }

    public static List<File> getDirectories(String directory) throws PWCGException
    {
        List<File> directoriesFound = new ArrayList<>();
        File directoryFile = new File(normalizePath(directory));
        if (directoryFile.exists())
        {
            if (directoryFile.isDirectory())
            {
                for (File file : directoryFile.listFiles())
                {
                    if (file.isDirectory())
                    {
                        directoriesFound.add(file);
                    }
                }
            }
        }

        return directoriesFound;
    }

    public static File retrieveFile(String directory, String filename)
    {
        File file = new File(normalizePath(directory + filename));
        return file;
    }

    public static String stripFileExtension(String filename)
    {
        if (filename.contains("."))
        {
            return filename.substring(0, filename.lastIndexOf('.'));
        }
        else
        {
            return filename;
        }
    }

    public static long ageOfFilesInMillis(String pathname) throws PWCGException
    {
        File file = new File(normalizePath(pathname));
        if (file.exists())
        {
            try
            {
                Path path = FileSystems.getDefault().getPath(normalizePath(pathname));
                BasicFileAttributes attr = Files.readAttributes(path, BasicFileAttributes.class);
                FileTime fileTime = attr.creationTime();
                return fileTime.toMillis();
            }
            catch (IOException e)
            {
                PWCGLogger.logException(e);
                throw new PWCGException("Could not get  file time for file " + pathname);
            }
        }
        else
        {
            throw new PWCGException("Could not get  file time.  File does not exist " + pathname);
        }
    }

    public static void copyFile(File source, File destination) throws IOException
    {
        if (destination.isDirectory())
        {
            destination = new File(destination, source.getName());
        }

        FileInputStream input = new FileInputStream(source);
        copyFile(input, destination);
    }

    public static void copyFile(InputStream input, File destination) throws IOException
    {
        OutputStream output = null;

        output = new FileOutputStream(destination);

        byte[] buffer = new byte[1024];

        int bytesRead = input.read(buffer);

        while (bytesRead >= 0)
        {
            output.write(buffer, 0, bytesRead);
            bytesRead = input.read(buffer);
        }

        input.close();

        output.close();
    }

    public static void copyDirectory(File sourceDir, File targetDir) throws IOException
    {
        if (!sourceDir.exists())
        {
            return;
        }
        
        if (sourceDir.isDirectory())
        {
            copyDirectoryRecursively(sourceDir, targetDir);
        }
        else
        {
            Files.copy(sourceDir.toPath(), targetDir.toPath());
        }
    }

    private static void copyDirectoryRecursively(File source, File target) throws IOException
    {
        if (!target.exists())
        {
            target.mkdir();
        }

        for (String child : source.list())
        {
            copyDirectory(new File(source, child), new File(target, child));
        }
    }
}

