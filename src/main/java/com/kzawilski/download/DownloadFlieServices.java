package com.kzawilski.download;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DownloadFlieServices {

    public static final String UTF8_BOM = "\uFEFF";

    private List<String> fileList;



    public DownloadFlieServices(){
        fileList = new ArrayList<>();
        try {
            URL oracle = new URL("http://www.nbp.pl/kursy/xml/dir.txt");
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(oracle.openStream()));

            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                // delete problematic char
                if (inputLine.startsWith(UTF8_BOM)) {
                    inputLine = inputLine.substring(1);
                }
                // Including only average exchange rate
                if (!inputLine.startsWith("a")){
                    continue;
                }
                fileList.add(inputLine);

            }
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public List<String> getFileList(){
        return fileList;
    }

    public Integer downloadFile(String fileName){
        String fromFile = "http://www.nbp.pl/kursy/xml/" + fileName + ".xml";
        String toFile = "nbp/" + fileName + ".xml";

        try {

            URL website = new URL(fromFile);
            ReadableByteChannel rbc = Channels.newChannel(website.openStream());
            FileOutputStream fos = new FileOutputStream(toFile);
            fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
            fos.close();
            rbc.close();

        } catch (IOException e) {
            e.printStackTrace();
            return 0;
        }
        return 1;
    }
}
