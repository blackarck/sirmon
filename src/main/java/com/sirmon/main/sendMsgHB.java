package com.sirmon.main;

import com.google.api.client.util.IOUtils;
import com.google.gson.Gson;
import javax.net.ssl.SSLContext;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;

public class sendMsgHB {

    writelog logwriter = new writelog();

    public sendMsgHB() {
        System.out.println("Initiate sending message");
        logwriter.logdata("Sending message to hang byte ");
    }

    public void postMessage(serverdtl srv, String postUrl) {
        try {
            //code to convert class to json object and post message to url
            Gson gson = new Gson();
            StringEntity postingString = new StringEntity(gson.toJson(srv), ContentType.APPLICATION_JSON);
              postAny(postUrl, postingString);
            /*
            HttpPost post = new HttpPost(postUrl);

            TrustStrategy acceptingTrustStrategy = new TrustSelfSignedStrategy();
            SSLContext sslContext = org.apache.http.ssl.SSLContexts.custom().loadTrustMaterial(null, acceptingTrustStrategy)
                    .build();
            SSLConnectionSocketFactory scsf = new SSLConnectionSocketFactory(sslContext, NoopHostnameVerifier.INSTANCE);
            HttpClient httpClient = HttpClients.custom().setSSLSocketFactory(scsf).build();

            // HttpClient   httpClient    = HttpClientBuilder.create().build();
            post.addHeader("accept", "application/json");
            post.setHeader("Content-type", "application/json");
            post.setEntity(postingString);
            HttpResponse response = httpClient.execute(post);

            System.out.println("Response is " + response.toString());
*/
        } catch (Exception e) {
            logwriter.logdata("Post message exception " + e.getLocalizedMessage());
        }
    }//end f postmessage

    public void postenvMessage(envstats env, String postUrl) {
        try {
            Gson gson = new Gson();
            StringEntity postingString = new StringEntity(gson.toJson(env), ContentType.APPLICATION_JSON);
            postAny(postUrl, postingString);
        } catch (Exception e) {
            logwriter.logdata("Post message exception " + e.getLocalizedMessage());
        }
    }//end of postenv message

    public void postAny(String postUrl, StringEntity postingString) {
        try {
            HttpPost post = new HttpPost(postUrl);
            TrustStrategy acceptingTrustStrategy = new TrustSelfSignedStrategy();
            SSLContext sslContext = org.apache.http.ssl.SSLContexts.custom().loadTrustMaterial(null, acceptingTrustStrategy)
                    .build();
            SSLConnectionSocketFactory scsf = new SSLConnectionSocketFactory(sslContext, NoopHostnameVerifier.INSTANCE);
            HttpClient httpClient = HttpClients.custom().setSSLSocketFactory(scsf).build();
            post.addHeader("accept", "application/json");
            post.setHeader("Content-type", "application/json");
            post.setEntity(postingString);
            HttpResponse response = httpClient.execute(post);

            System.out.println("Response is " + response.toString());
        } catch (Exception e) {
            System.out.println("Exception " + e);
        }
    }//end of postAny

}
