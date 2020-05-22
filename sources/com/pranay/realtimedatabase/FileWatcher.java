package com.pranay.realtimedatabase;
/**
 * FileWatcher
 */

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;




public class FileWatcher extends Thread {
    FileContentsChangeListener changeListener;
    final File logFile;
    final RandomAccessFile rFile;
    long lastModifiedTime;

    public FileWatcher(File file) throws FileNotFoundException {
        this.logFile = file;
        rFile = new RandomAccessFile(logFile, "r");
    }

    public FileWatcher(String file) throws FileNotFoundException {
        System.out.println("Out file is " + file);
        this.logFile = new File(file);
        rFile = new RandomAccessFile(logFile, "r");
    }

    public void setFileContentsChangedListener(FileContentsChangeListener listener) {
        changeListener = listener;
    }

    @Override
    public void start() {
        lastModifiedTime = logFile.lastModified();
        System.out.println("Starting LogWatcher service");
        super.start();
    }

    private String readLastLine() throws IOException {
        int lines = 0;
        StringBuilder builder = new StringBuilder();
        RandomAccessFile randomAccessFile = rFile;
        File file = logFile;
         long fileLength = file.length() - 2;
         // Set the pointer at the last of the file
         randomAccessFile.seek(fileLength);
         for(long pointer = fileLength; pointer >= 0; pointer--){
           randomAccessFile.seek(pointer);
           char c;
           c = (char)randomAccessFile.read(); 
           if(c == '\n'){
              break;
           }
           builder.append(c);
         }
         builder.reverse();
         return builder.toString();
    }
    @Override
    public void run() {
        System.out.println("Started File Watcher Service");
        while (true) {
            if (lastModifiedTime != logFile.lastModified()) {
                
                lastModifiedTime = logFile.lastModified();
                try {
                    String line = readLastLine();
                    changeListener.onContentsChanged(line);
                    // System.out.println(line);
                    // if (Pattern.matches(".+(?)Query\tselect.+", line)) {
                    //     databaseEventListener.onSelect(line);
                    // }
                    // if (Pattern.matches(".+(?)Query\tupdate.+", line)) {
                    //     databaseEventListener.onUpdate(line);
                    // }
                    // if (Pattern.matches(".+(?)Query\talter.+", line)) {
                    //     databaseEventListener.onAlter(line);
                    // }
                    // if (Pattern.matches(".+(?)Query\tdelete.+", line)) {
                    //     databaseEventListener.onSelect(line);
                    // }
                }
                catch (IOException e) {
                    System.out.println("Error Reading in File");
                }
            }
        }
    }

}