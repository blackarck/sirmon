package com.sirmon.main;

import bea.jolt.JoltSession;
import bea.jolt.JoltSessionAttributes;
import static com.google.common.base.Strings.isNullOrEmpty;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class serverdtl {

    public String server_dns;
    public String server_name;
    public String server_type;
    public String server_qry;
    public String server_status;
    public String server_msg;
    public String server_ip;
    public int server_port;
    public String clientid;
    public Date lastupddttm;

    public serverdtl() {
        //firebase requires a public no argument constructor
    }

    public serverdtl(String dns, String name, String type, String ip, String port, String qry, String clntid) {
        //constructor for server
        writelog logwriter = new writelog();
        logwriter.logdata("Reading- " + name + " ,server- " + dns + " details.");
        SimpleDateFormat f = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        String dateStr = f.format(new Date());
        this.server_dns = dns;
        this.server_name = name;
        this.server_type = type;
        this.server_ip = ip;
        this.server_port = Integer.parseInt(port);
        this.server_qry = qry;
        this.server_status = "unknown";
        this.server_msg = "";
        this.clientid = clntid;

        try {
            this.lastupddttm = f.parse(dateStr);
        } catch (ParseException ex) {
            System.out.println("Date parser error " + ex.getLocalizedMessage());
            logwriter.logdata("serverdtl exception " + ex.getLocalizedMessage());
        }
    }

    public void pingServer() {
        //will perform the actual ping
        writelog logwriter = new writelog();
        boolean available = true;
        String server_Add;
        //  -- check here whether serverdns and port are present if dns is not there use ip
        server_Add = server_dns;
        if (isNullOrEmpty(server_dns)) {
            server_Add = server_ip;
        }

        try {
            //System.out.println("Server " + server_Add);
            Socket s = new Socket(server_Add, (int) this.server_port);
            s.close();
        } catch (UnknownHostException e) { // unknown host 
            available = false;
            this.server_status = "Error";
            logwriter.logdata("Error-Unknown host ");
        } catch (IOException e) { // io exception, service probably not running 
            available = false;
            this.server_status = "Error";
            logwriter.logdata("Error-Server not running");
        } catch (NullPointerException e) {
            available = false;
            this.server_status = "Error";
            logwriter.logdata("Error-Null pointer");
        } catch (Exception e) {
            available = false;
            this.server_status = "Error";
            logwriter.logdata("Error-general");
        }
        if (available) {
            this.server_status = "Success-OK";
            System.out.println("Web Server working ok");
            logwriter.logdata("Web Server working ok");
        }
    }//end of pingserver

    public void pingdbServer(String dbusername,String dbpass,String dbtype, boolean dbbolsrvcname,String dbsrvcname, String[] envqrydtls, String inetsiteadd) {
        writelog logwriter = new writelog();
          int qryCount;
          qryCount=0;
          try {
            //step1 load the driver class  
            
            //step2 create  the connection object  
            String url="";
            switch (dbtype) {
                case "ORACLE":
                    Class.forName("oracle.jdbc.driver.OracleDriver");
                    if (dbbolsrvcname) {
                    url="jdbc:oracle:thin:@" + server_ip + ":" + server_port + "/" + dbsrvcname;
                    }else{
                    url="jdbc:oracle:thin:@" + server_ip + ":" + server_port + ":" + server_dns;
                    }
                    break;
                    
                case "MSSQL":
                    Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
                    url = "jdbc:sqlserver://" +server_ip + ";databaseName=" +server_dns;
                    break;
            }
            Connection con = DriverManager.getConnection(url ,dbusername,dbpass  );
            //step3 create the statement object  
            Statement stmt = con.createStatement();
            //step4 execute query  
            ResultSet rs = stmt.executeQuery(server_qry);
            while (rs.next()) {
               qryCount=rs.getInt(1);
            }
            //step5 close the connection object  
            //System.out.println("result is " + qryCount);
           
            if (qryCount==1){
              this.server_status = "Success-OK";
              this.server_msg = "Query Count " + qryCount;
                 //System.out.println("DB Server working ok");
                   logwriter.logdata("DB Server " + server_ip + ":"  + server_port + " working ok");
                   //this is where we will call rest of the queries and execute them. 04-02-2020
                   int qryresult[]=new int[8];
                    //run actual queries here  
              qryresult[0]=qryResultCount(rs,stmt,envqrydtls[0]);
              qryresult[1]=qryResultCount(rs,stmt,envqrydtls[1]);
              qryresult[2]=qryResultCount(rs,stmt,envqrydtls[2]);
              qryresult[3]=qryResultCount(rs,stmt,envqrydtls[3]);
              qryresult[4]=qryResultCount(rs,stmt,envqrydtls[4]);
              qryresult[5]=qryResultCount(rs,stmt,envqrydtls[5]);
              qryresult[6]=qryResultCount(rs,stmt,envqrydtls[6]);
              qryresult[7]=qryResultCount(rs,stmt,envqrydtls[7]);
         
                   callenvstats(qryresult,inetsiteadd);
                    con.close();
            }
        } catch (Exception ex) {
            logwriter.logdata("Serverdtl db server exception " + ex.getLocalizedMessage());
            this.server_status = "Error";
            logwriter.logdata("DB Error " + ex.getMessage());
        }
    }//end of pingdbserver
    
    public int qryResultCount(ResultSet rs, Statement stmt,String qry){
        int i=0;
          writelog logwriter = new writelog();
        try{
          rs = stmt.executeQuery(qry);
          while (rs.next()) {
              i=rs.getInt(1);
            }
        }catch(Exception ex){
             logwriter.logdata("Serverdtl db server exception " + ex.getLocalizedMessage());
        }
        return i;
    }//end of qryCount
    
    public void pingappServer(String domainpwd){
        writelog logwriter = new writelog();
         JoltSession session;
         JoltSessionAttributes sattr;
         boolean available;
         
            available=false;
         try{
         sattr = new JoltSessionAttributes(); 
        sattr.setString(sattr.APPADDRESS,"//" + server_ip + ":" + server_port );
        
        sattr.setInt(sattr.IDLETIMEOUT, 300);
             System.out.println("Setting app server session");
             //JoltSession(JoltSessionAttributes attr, String userName, String userRole, String userPassword, String appPassword) 
        session = new JoltSession(sattr,"","","",domainpwd);
        System.out.println("Sesison out " + session.toString());
        available=true;
         }catch(Exception ex){
             available = false;
            this.server_status = "Error";
             logwriter.logdata("App Error " + ex.getMessage());
             System.out.println("AppServer exception " + ex.getLocalizedMessage());
         }
         if(available){
             this.server_status = "Success-OK";
             logwriter.logdata("App server " + server_ip + ":"  + server_port + " working OK ");
         }
    }//ping app server
    
    public void callenvstats(int[] envqrydtls, String inetsiteadd){
        envstats e = new envstats();
        e.clientid = this.clientid;
        e.prcsfail=String.valueOf(envqrydtls[0]);
        e.prcspass=String.valueOf(envqrydtls[1]);
        e.outsucc=String.valueOf(envqrydtls[2]);
        e.outpend=String.valueOf(envqrydtls[3]);
        e.outfail= String.valueOf(envqrydtls[4]);
        e.inbsucc = String.valueOf(envqrydtls[5]);
        e.inbfail =String.valueOf(envqrydtls[6]);
        e.inbpend=String.valueOf(envqrydtls[7]); 
        
        checkInternet2 checknet = new checkInternet2();
            if (checknet.testInet(inetsiteadd)) {
                writelog logwriter = new writelog();
                    logwriter.logdata("Internet working trying to post env results");
                      sendMsgHB sendmsg=new sendMsgHB();
                      String post_ser_add="https://lotto.hangbyte.com:8444/sirmonenvs";
                        sendmsg.postenvMessage(e,post_ser_add );
            }
        
    }//end of callenvstats

}//end of class
