/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sirmon.main;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author dr_wa
 */
public class firestoredb {
    
    Firestore db;
     writelog logwriter = new writelog();
     
    public firestoredb(){
        try {
            ClassLoader classLoader = getClass().getClassLoader();
            InputStream serviceAccount = classLoader.getResourceAsStream("demoApp-05b5006ef350.json");
            GoogleCredentials credentials = GoogleCredentials.fromStream(serviceAccount);
            FirebaseOptions options = new FirebaseOptions.Builder()
                    .setCredentials(credentials)
                    .build();
            FirebaseApp.initializeApp(options);

            this.db = FirestoreClient.getFirestore();
        } catch (IOException ex) {
          logwriter.logdata("sendmsgs exception " + ex.getLocalizedMessage());
        }
    }
    
    public Firestore getFirestore(){
        return this.db;
    }
}
