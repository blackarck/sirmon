/*
 * Server monitor program. Licensed version to run only.
 * This is the client version no use of running this until registered with server.
 * File should be pasted on app server. App server will have access to both web and DB server.
 * Should have access to internet. 
 * Need to be part of cron job to run every 3-4 hrs. More if desired.
 */
package com.sirmon.main;

/**
 *
 * @author vivek sharma date : 20-dec-2017
 */

public class main {

    public static void main(String[] args) {

        writelog logwriter = new writelog();
        logwriter.logdata("Reading json file");
        jsonRead jsonrd = new jsonRead();
        logwriter.logdata("Closing call");
    }

}
