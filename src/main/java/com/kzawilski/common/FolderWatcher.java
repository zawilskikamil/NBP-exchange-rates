package com.kzawilski.common;

import java.io.IOException;
import java.nio.file.*;

import static java.nio.file.StandardWatchEventKinds.*;

public class FolderWatcher {
    public Integer watch(Path dir){
        try {
            System.out.println(dir);
            WatchService watcher = FileSystems.getDefault().newWatchService();

            WatchKey key = dir.register(watcher,
                    ENTRY_CREATE);

            for (;;) {
                for (WatchEvent<?> event: key.pollEvents()) {
                    WatchEvent.Kind<?> kind = event.kind();

                    if (kind != ENTRY_CREATE) {
                        continue;
                    }

                    WatchEvent<Path> ev = (WatchEvent<Path>)event;
                    Path filename = ev.context();

                    try {
                        Path child = dir.resolve(filename);
                        /*
                        TODO Changing the file causes a program crash
                        (because a temporary file is generated)
                        .goutputstream-GTTT4Y
                         */
                        if (!Files.probeContentType(child).equals("text/plain")) {
                            System.err.format("New file '%s'" +
                                    " is not a plain text file.%n", filename);
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
        return 1;
    }
}
