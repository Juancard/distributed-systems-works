package Tp2.Ex01.Server;

import java.io.*;

/**
 * User: juan
 * Date: 10/04/17
 * Time: 16:33
 */
public class FileManager {

    private File filesPath;

    public FileManager(String filesPathString) throws IOException {
        System.out.println("Path to save files in: " + filesPathString);

        File f = new File(filesPathString);
        //System.out.println(f.exists() + " " + f.isDirectory() + " " + f.getPath() + " " + f.getAbsolutePath());
        if (!(f.exists())) throw new IOException("Path does not exist in file system");
        if (!(f.isDirectory())) throw new IOException("Path is not a valid directory");

        this.filesPath = f;
    }

    public String post() {
        return "In File manager: Post";
    }

    public String del() {
        return "in File Manager: Del";
    }

    public File get(String fileName) throws FileNotFoundException {
        System.out.println("in File Manager: Get");
        System.out.println("With parameter: fileName=" + fileName);

        final String TO_SEARCH = fileName;
        File[] filesFoundWithName = this.filesPath.listFiles(new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                return pathname.getName().equals(TO_SEARCH);
            }
        });

        if (filesFoundWithName.length <= 0)
            throw new FileNotFoundException("No file matches given file name");
        return filesFoundWithName[0];
    }

    public String[] dir() {
        System.out.println("in File Manager: Dir");
        File[] filesInPath = filesPath.listFiles();
        String[] filesAvailable = new String[filesInPath.length];
        for (int i=0; i<filesInPath.length; i++) {
            filesAvailable[i] = filesInPath[i].getName();
        }
        return filesAvailable;
    }
}
