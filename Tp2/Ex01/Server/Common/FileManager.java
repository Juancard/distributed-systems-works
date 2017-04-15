package Tp2.Ex01.Server.Common;

import Tp2.Ex01.Common.TextFile;

import java.io.*;

/**
 * User: juan
 * Date: 10/04/17
 * Time: 16:33
 */
public class FileManager {

    private File filesPath;

    public FileManager(String filesPathString) throws IOException {
        this.filesPath = this.loadFilesPath(filesPathString);
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


    public TextFile get(String fileName) throws IOException {
        System.out.println("in File Manager: Get");
        System.out.println("With parameter: fileName=" + fileName);

        File[] filesFoundWithName = this.getFilesByName(fileName);
        if (filesFoundWithName.length <= 0)
            return new TextFile(fileName, "");
        return new TextFile(fileName, this.getFileContent(filesFoundWithName[0]));
    }

    public boolean post(TextFile fileToPost) {
        System.out.println("In File manager: Post");
        String toSaveIn = filesPath.getPath() + "/" + fileToPost.getName();
        return this.createFile(toSaveIn, fileToPost);
    }


    public boolean del(String fileName) {
        System.out.println("in File Manager: Del");
        File[] filesFound = this.getFilesByName(fileName);
        if (filesFound.length > 0)
            return filesFound[0].delete();
        return true;
    }

    private File loadFilesPath(String filesPathString) throws IOException {
        File f = new File(filesPathString);

        if (!(f.exists())) throw new IOException("Path does not exist in file system");
        if (!(f.isDirectory())) throw new IOException("Path is not a valid directory");

        return f;
    }

    private File[] getFilesByName(final String FILE_NAME) {
        return this.filesPath.listFiles(new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                return pathname.getName().equals(FILE_NAME);
            }
        });
    }

    private String getFileContent(File file) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(file.getPath()));
        return br.readLine();
    }

    private boolean createFile(String toSaveIn, TextFile fileToPost) {
        try{
            PrintWriter writer = new PrintWriter(toSaveIn, "UTF-8");
            writer.println(fileToPost.getContent());
            writer.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}
