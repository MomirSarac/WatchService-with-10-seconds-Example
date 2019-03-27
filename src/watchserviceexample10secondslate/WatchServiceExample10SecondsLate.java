/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package watchserviceexample10secondslate;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import static java.nio.file.StandardWatchEventKinds.ENTRY_CREATE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_DELETE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_MODIFY;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Simple example of WatchService feature tracking events of change over certian directory.
 * @author Momir Sarac
 */
public class WatchServiceExample10SecondsLate {

    // get it before anything, just a little info what this code is about
    static {

        System.out.println("Monitoring activity over folder: /home/ubuntu-2018/NetBeansProjects/WatchService Example");
    }
    // variables
    private Path path = null;
    private WatchService watchService = null;

    /**
     * Method used for registering watch service for a given directory
     */
    public void initialization() {
        path = Paths.get("/home/ubuntu-2018/NetBeansProjects/WatchService Example");
        try {
            watchService = FileSystems.getDefault().newWatchService();
            path.register(watchService, ENTRY_CREATE, ENTRY_DELETE, ENTRY_MODIFY);
        } catch (IOException ioE) {
            System.out.println("Warning! Error message: " + ioE);
        }
    }
    /**
     * Method used for tracking corresponding events( create,delete, modify )  
     * for a given directory every 10 seconds.
     * @throws InterruptedException 
     */
    private void monitoringWithTenSecondsLate() throws InterruptedException {
        WatchKey watchKey = null;
        while (true) {
            try {
                //poll everything every 10 seconds
                watchKey = watchService.poll(10, TimeUnit.SECONDS);
                for (WatchEvent<?> watchEvent : watchKey.pollEvents()) {
                    WatchEvent.Kind<?> kind = watchEvent.kind();
                    System.out.println("Event occured for " + watchEvent.context().toString() + " " + kind.toString());
                }
            } catch (InterruptedException ex) {
                Logger.getLogger(WatchServiceExample10SecondsLate.class.getName()).log(Level.SEVERE, null, ex);
            }
            //don't reset watch key for 10 seconds
            Thread.sleep(10000);
            boolean reset = watchKey.reset();
            if (!reset) {
                break;
            }
        }
    }
    /**
     * @param args the command line arguments
     * @throws java.lang.InterruptedException
     */
    public static void main(String[] args) throws InterruptedException {
        WatchServiceExample10SecondsLate watchServiceExample = new WatchServiceExample10SecondsLate();
        watchServiceExample.initialization();
        watchServiceExample.monitoringWithTenSecondsLate();

    }

}
