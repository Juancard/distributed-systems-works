package Tp2.Ex01.Server.MainServer;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.nio.file.StandardWatchEventKinds.*;

/**
 * User: juan
 * Date: 15/04/17
 * Time: 14:50
 */



public class FilesWatcher {
    private static final String LOCAL_FILES_PATH = "distributed-systems-works/Tp2/Ex01/Server/MainServer/Resources/Files/";
    private static final String BACKUP_FILES_PATH = "distributed-systems-works/Tp2/Ex01/Server/BackupServer/Resources/Files/";

    private WatchService watcher;
    private final Map<WatchKey,Path> keys;

    public static void main(String[] args) throws IOException {
        new FilesWatcher();
    }

    public FilesWatcher() throws IOException {
        Path localFilesPath = Paths.get(LOCAL_FILES_PATH);
        Path backupFilesPath = Paths.get(BACKUP_FILES_PATH);

        this.keys = new HashMap<WatchKey,Path>();
        this.watcher = FileSystems.getDefault().newWatchService();

        WatchKey localKey = localFilesPath.register(watcher, ENTRY_DELETE, ENTRY_MODIFY);
        WatchKey backupKey = backupFilesPath.register(watcher, ENTRY_DELETE, ENTRY_MODIFY);

        this.keys.put(localKey, localFilesPath);
        this.keys.put(backupKey, backupFilesPath);

        this.processEvents();
    }

    /**
     * Process all events for the key queued to the watcher.
     */
    void processEvents() {
        for (;;) {

            // wait for key to be signaled
            WatchKey key;
            try {
                key = watcher.take();
            } catch (InterruptedException x) {
                return;
            }

            List<WatchEvent<?>> eventsOnWatchedKey = key.pollEvents();
            System.out.println("Number of events: " + eventsOnWatchedKey.size());
            for (WatchEvent<?> event: eventsOnWatchedKey) {
                WatchEvent.Kind kind = event.kind();
                //The filename is the context of the event.
                WatchEvent<Path> ev = (WatchEvent<Path>)event;
                Path filename = ev.context();

                Path dirAffected = keys.get(key);
                System.out.println(dirAffected + " - " + kind + " - " + filename);
                Path fileChanged = dirAffected.resolve(filename);
                if (kind.name().equals(ENTRY_MODIFY)) {

                } else if (kind.name().equals(ENTRY_DELETE)) {

                }
            }

            //Reset the key -- this step is critical if you want to receive
            //further watch events. If the key is no longer valid, the directory
            //is inaccessible so exit the loop.
            boolean valid = key.reset();
            if (!valid) {
                break;
            }
        }
    }
}
