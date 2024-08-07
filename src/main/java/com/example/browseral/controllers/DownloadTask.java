package com.example.browseral.controllers;

import javafx.concurrent.Task;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Paths;

//handle download tasks
public class DownloadTask extends Task<Void> {

    private String url;

    public DownloadTask(String url) {
        this.url = url;
    }

    @Override
    protected Void call() throws Exception {
        String ext = url.substring(url.lastIndexOf("."), url.length());
        URLConnection connection = new URL(url).openConnection();
        long fileLength = connection.getContentLengthLong();

        try (InputStream is = connection.getInputStream();
             OutputStream os = Files.newOutputStream(Paths.get("downloadedfile" + ext))) {

            long nread = 0L;
            byte[] buf = new byte[8192];
            int n;
            while ((n = is.read(buf)) > 0) {
                os.write(buf, 0, n);
                nread += n;
                updateProgress(nread, fileLength);
            }
        }

        return null;
    }

//    @Override
//    protected void failed() {
//        System.out.println("failed");
//        MainController.downloadStatusLabel.setText("Download failed!");
//    }
//
//    @Override
//    protected void succeeded() {
//        System.out.println("downloaded");
//        MainController.downloadStatusLabel.setText("File download complete");
//    }
}
