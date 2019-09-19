/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sirmon.main;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.Firestore;

/**
 *
 * @author dr_wa
 */
public class sendMessage {

    writelog logwriter = new writelog();
    Firestore db ;
    
    public sendMessage() {
            //constructor lets see what we need to do
            firestoredb f = new firestoredb();
            db = f.getFirestore();
    }//end of constructor
    

    public void postMessage(serverdtl srv, String postUrl)   {
        try {
            //Gson gson = new Gson();
            //System.out.println("sm Posting data out");
            //HttpClient httpClient = HttpClientBuilder.create().build();
            //HttpPost post = new HttpPost(postUrl);
            //StringEntity postingString = new StringEntity(gson.toJson(srv));//gson.tojson() converts your pojo to json
            // post.setEntity(postingString);
            //firebase code
           
            //DocumentReference docRef = db.collection(srv.clientid).document("ld0RwbLoxN8IDIaNVrVN");
            serverdtl servdata=srv;
            //System.out.println("Doing actual post");
            String client_id=srv.clientid;
            ApiFuture<DocumentReference> result = db.collection(""+srv.clientid).add(srv);
             // System.out.println("Doing actual post2");
            logwriter.logdata("Updated firestore : " + result.get().getId());
            /*
        post.setHeader("Content-type", "application/json");
        HttpResponse response = httpClient.execute(post);
        if (response != null) {
          InputStream in = response.getEntity().getContent();
          String result = CharStreams.toString(new InputStreamReader(in, Charsets.UTF_8));
            System.out.println("Result "+result);
        }
             */
        } catch (Exception e) {
            logwriter.logdata("Post message exception " + e.getLocalizedMessage());
        }
    }//end of post message
}
