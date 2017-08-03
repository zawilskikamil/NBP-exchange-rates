package com.kzawilski.common;

import javafx.concurrent.Task;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;

import static java.nio.file.StandardWatchEventKinds.*;

public class FolderWatcher extends Task<Void> {

    private Path checkDir() {
        File theDir = new File("nbp");
        if (!theDir.exists()) {
            System.out.println("creating directory: " + theDir.getName());
            try {
                theDir.mkdir();
            } catch (SecurityException se) {
                return null;
            }
        }
        return FileSystems.getDefault().getPath(theDir.getAbsolutePath());
    }

    private void watch(Path dir) {
        try {
            WatchService watcher = FileSystems.getDefault().newWatchService();
            WatchKey key = dir.register(watcher, ENTRY_CREATE);
            while (!isCancelled()) {
                for (WatchEvent<?> event : key.pollEvents()) {
                    WatchEvent.Kind<?> kind = event.kind();
                    if (kind != ENTRY_CREATE) {
                        continue;
                    }
                    WatchEvent<Path> ev = (WatchEvent<Path>) event;
                    Path filename = ev.context();
                    try {
                        Path child = dir.resolve(filename);
                        if (!Files.probeContentType(child).equals("application/xml")) {
                            System.err.format("New file '%s'" +
                                    " is not a xml file.%n", filename);
                            continue;
                        }
                    } catch (IOException x) {
                        System.err.println(x);
                        continue;
                    }

                    // TODO read and save to database
                    System.out.format("Found new file %s%n", filename);
                }
                boolean valid = key.reset();
                if (!valid) {
                    break;
                }
            }
        } catch (IOException x) {
            System.err.println(x);

        }
    }

    @Override
    protected Void call() throws Exception {
        Path dir = checkDir();
        if (dir != null) {
            this.watch(dir);
        }
        return null;
    }

}
