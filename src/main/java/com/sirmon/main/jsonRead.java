package com.sirmon.main;

import static com.google.common.base.Strings.isNullOrEmpty;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.ExecutionException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

/**
 *
 * @author vivek
 */
public class jsonRead {

    JSONParser parser;
    String version, clientid, clientname;
    String postresult, writelog;
    ArrayList<serverdtl> serveral = new ArrayList<serverdtl>();
    writelog logwriter = new writelog();
    String netcheck, inetsiteadd;
    String post_ser_add, post_ser_port;
    String dbuser, dbpass;
    String clientFilename = "config_1218.json";
    String[] dbenvqry = new String[8];

    public jsonRead() {
        //System.out.println("Starting json parser");
        int doresult = 0;
        try {
            parser = new JSONParser();
            ClassLoader classLoader = getClass().getClassLoader();
            //InputStream in = classLoader.getResourceAsStream("config_sha.json");
            InputStream in = classLoader.getResourceAsStream(clientFilename);
            //JSONObject data = (JSONObject) parser.parse(
            //      new FileReader(classLoader.getResourceAsStream("config.json")));//path to the JSON file.
            JSONObject data = (JSONObject) parser.parse(new InputStreamReader(in));
            JSONObject jsonObject = (JSONObject) data;
            version = (String) jsonObject.get("version");
            clientid = (String) jsonObject.get("clientid");
            clientname = (String) jsonObject.get("clientname");
            netcheck = (String) jsonObject.get("netcheck");
            postresult = (String) jsonObject.get("postresult");
            inetsiteadd = (String) jsonObject.get("netsite");
            writelog = (String) jsonObject.get("writelog");
            post_ser_add = (String) jsonObject.get("postseradd");
            post_ser_port = (String) jsonObject.get("postserport");
            dbuser = (String) jsonObject.get("dbuser");
            dbpass = (String) jsonObject.get("dbpwd");

            JSONObject envdetails = (JSONObject) jsonObject.get("envstats");
            //envstats declaration
            dbenvqry[0] = (String) envdetails.get("prcsfail");
            dbenvqry[1] = (String) envdetails.get("prcspass");
            dbenvqry[2] = (String) envdetails.get("outsucc");
            dbenvqry[3] = (String) envdetails.get("outpend");
            dbenvqry[4] = (String) envdetails.get("outfail");
            dbenvqry[5] = (String) envdetails.get("inbsucc");
            dbenvqry[6] = (String) envdetails.get("inbfail");
            dbenvqry[7] = (String) envdetails.get("inbpend");
            //working    System.out.println("inbpend is " + dbenvqry[7]);
            //read server list for running checks
            JSONArray servers = (JSONArray) jsonObject.get("server");
            for (Object s : servers) {
                JSONObject srvdtl = (JSONObject) s;
                serverdtl srv = new serverdtl((String) srvdtl.get("servdns"), (String) srvdtl.get("servname"), (String) srvdtl.get("servtype"), (String) srvdtl.get("servip"), (String) srvdtl.get("servport"), (String) srvdtl.get("servqry"), clientid);
                if (isNullOrEmpty(srv.server_dns) && isNullOrEmpty(srv.server_ip)) {
                    logwriter.logdata("No server address presnet in configuration for " + srv.server_name);
                } else {
                    String servertype = srv.server_type;
                    switch (servertype) {
                        case "webserver":
                            srv.pingServer();
                            serveral.add(srv);
                            doresult = 1;
                            break;

                        case "dbserver":
                            dbdetail dbdetails = new dbdetail(srv.server_name);
                            srv.pingdbServer(dbdetails.dbuser, dbdetails.dbpwd, dbdetails.dbtype, dbdetails.srvcname, dbdetails.dbsrvcname, dbenvqry, inetsiteadd);
                            serveral.add(srv);
                            doresult = 1;
                            break;

                        case "app":
                            dbdetail dbdetail2 = new dbdetail(srv.server_name);
                            srv.pingappServer(dbdetail2.domainpwd);
                            serveral.add(srv);
                            doresult = 1;
                            break;

                        default:
                            logwriter.logdata("jsonRead switch : Server type not defined");
                            doresult = 0;
                            break;
                    }//end of switch
                }
            }//end of reading server

            // --result of all pings are present now can do result post here
            if (postresult.equals("off")) {
                logwriter.logdata("Postresult is off in configuration.");
            }
            if (doresult == 1 && postresult.equals("on")) {
                postResult(serveral);
            }
        } catch (Exception ex) {
            logwriter.logdata("Json read Exception " + ex.getLocalizedMessage());
        }
    }//end of json read

    public void postResult(ArrayList<serverdtl> serverAL) throws IOException, InterruptedException, ExecutionException {
        checkInternet2 checknet = new checkInternet2();
        //  --check whether internet is working and then post
        //  -- if internet is not working log error
        if (checknet.testInet(inetsiteadd)) {
            //internet is working
            logwriter.logdata("Internet working trying to post result");
            //if (checknet.testInet(post_ser_add )) {
            //  logwriter.logdata("Server found posting result");
            //internet working posting result to server
            //compile result and post
            //sendMessage s = new sendMessage();
            sendMsgHB sendmsg = new sendMsgHB();
            Iterator it = serverAL.iterator();
            while (it.hasNext()) {
                // System.out.println("inside while "  );
                serverdtl s = (serverdtl) it.next();
                //System.out.println("Value of s is " + s.server_dns + s.server_name);
                sendmsg.postMessage(s, post_ser_add);
            }
            //} else {
            //   logwriter.logdata("Main server " + post_ser_add + "  not reachable");
        } else {
            logwriter.logdata("Internet not available cannot post result");
            //nothing else required
        }

    }//end of method postResult

}
