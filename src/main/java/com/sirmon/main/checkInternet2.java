/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sirmon.main;

import java.io.IOException;
import java.net.Socket;
import static com.google.common.base.Strings.isNullOrEmpty;
import java.net.InetAddress;

/**
 *
 * @author dr_wa
 */
public class checkInternet2 {

    String sitename;
    writelog logwriter = new writelog();

    public checkInternet2() {
        //constructor
        //by default site is google
        sitename = "google.com";
    }

    public boolean testInet(String site) {
        Socket sock = new Socket();
        if (isNullOrEmpty(site)) {
            site = sitename;
        }
        try {
            InetAddress addr = InetAddress.getByName(site);
            addr.isReachable(3000);
            return true;
        } catch (IOException e) {
            logwriter.logdata("Check internet error IOexception " + e.getStackTrace());
            e.printStackTrace();
            return false;
        } finally {
            try {
                sock.close();
            } catch (IOException e) {
                logwriter.logdata("Check internet error finally" + e.getLocalizedMessage());
            }
        }
    }//end of internet tester
}
