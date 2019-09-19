/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sirmon.main;

/**
 *
 * @author dr_wa
 */
public class dbdetail {
    public String dbuser;
    public String dbpwd;
    public String dbtype;
    public String dbsrvcname;
    public boolean srvcname=false;
    
    writelog logwriter = new writelog();
    
    dbdetail(String dbname){
        dbuser="";
        dbpwd="";
        dbtype="";
        dbsrvcname="";
        
          switch (dbname) {
                case "PSHCMDB":
                    dbuser="SYSADM";
                    dbpwd="SYSADM";
                    dbtype = "ORACLE";
                    dbsrvcname = "PSHCMDB";
                    srvcname=true;
                    break;
                 case "ERPPRDRP\\SQL2012DEV1"   :
                    dbuser="1218_MONITOR";
                    dbpwd="Monitor555";
                    dbtype = "MSSQL";
                     break;
                     default:                            
                            logwriter.logdata("DB Details not found");
                        break;
          }//end of switch

    }//end of constructor
}
