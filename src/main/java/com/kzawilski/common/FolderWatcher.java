package com.kzawilski.common;

import com.kzawilski.database.DataManager;
import com.kzawilski.database.domain.ExchangeRate;
import javafx.concurrent.Task;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

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
            WatchKey key = dir.register(watcher,
                    ENTRY_CREATE,
                    ENTRY_DELETE,
                    ENTRY_MODIFY);
            while (!isCancelled()) {
                Path filename = null;
                List<WatchEvent<?>> events = key.pollEvents();
                for (WatchEvent<?> event : events) {
                    WatchEvent.Kind<?> kind = event.kind();
                    if (kind != ENTRY_MODIFY) {
                        continue;
                    }
                    WatchEvent<Path> ev = (WatchEvent<Path>) event;
                    filename = ev.context();
                    try {
                        Path child = dir.resolve(filename);
                        if (!Files.probeContentType(child).equals("application/xml")) {
                            System.err.format("New file '%s'" +
                                    " is not a xml file.%n", filename);
                            filename = null;
                            continue;
                        }
                    } catch (IOException x) {
                        System.err.println(x);
                        continue;
                    }

                    System.out.format("Found new file %s%n", filename);
                }
                // when loop end, file should been written
                if (filename != null) {
                    saveToDatabase("nbp/" + filename);
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

    public void saveToDatabase(String fileName) {
        System.out.println("Try save " + fileName);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        File file = new File(fileName);
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(file);
            String dateString = doc.getElementsByTagName("data_publikacji").item(0).getTextContent();
            NodeList nl = doc.getElementsByTagName("pozycja");
            DateFormat format = new SimpleDateFormat("yyyy-MM-dd", new Locale("pl", "PL"));
            Date date = format.parse(dateString);
            DataManager dm = new DataManager();
            for (int i = 0; i < nl.getLength(); i++) {
                String currencyName = doc.getElementsByTagName("nazwa_waluty").item(i).getTextContent();
                String converter = doc.getElementsByTagName("przelicznik").item(i).getTextContent();
                String currencyCode = doc.getElementsByTagName("kod_waluty").item(i).getTextContent();
                String rate = doc.getElementsByTagName("kurs_sredni").item(i).getTextContent();
                ExchangeRate e = new ExchangeRate();
                e.setCurrencyName(currencyName);
                e.setConverter(new Integer(converter));
                e.setCurrencyCode(currencyCode);
                e.setDate(date);
                e.setRate(new Double(rate.replaceAll(",", ".")));
                dm.saveExchangeRate(e);
            }
            System.out.println("save is complete");
            dm.stop();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

}
