/*
 * This will write log files after reading from the configuration
 */
package com.sirmon.main;


import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class writelog {
    Logger logger = Logger.getLogger("sirmon");  
    FileHandler fh; 
    SimpleDateFormat sformat;
    
   public writelog(){
 try{  
      // This block configure the logger with handler and formatter  
        sformat= new SimpleDateFormat("M-d");
        File dir=new File("log");
       if(dir.exists()){
          // System.out.println("log dir exists:");
       }else{
           dir.mkdir();
       }
       
        //for java 8 Path path = Paths.get("log/logfile" + sformat.format(new Date()) +".txt");
        String path = new File("log/logfile" + sformat.format(new Date()) +".txt").getPath();
        //OutputStream output=   new BufferedOutputStream(Files.newOutputStream(path,CREATE, APPEND));
        fh = new FileHandler( path.toString(),true);  
        logger.addHandler(fh);
        SimpleFormatter formatter = new SimpleFormatter();  
        fh.setFormatter(formatter);  
    } catch (Exception e) {  
          e.printStackTrace();  
       }    
    }//end of method writelog
   
   public void logdata(String msg){
       logger.info(msg);
   }
}
