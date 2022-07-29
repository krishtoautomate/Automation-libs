package com.ReportManager;

import com.aventstack.extentreports.ExtentTest;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.*;

public class LoggerManager {

  static Map<Integer, Logger> LoggerMap = new HashMap<>();

  static Logger logger = Logger.getGlobal();

 public static Logger startLogger(String name){
   logger =  Logger.getLogger(name);

   logger.setUseParentHandlers(false);

   LogFormatter formatter = new LogFormatter();
   ConsoleHandler handler = new ConsoleHandler();
   handler.setFormatter(formatter);
   logger.addHandler(handler);

   LoggerMap.put((int) Thread.currentThread().getId(), logger);

   return logger;
 }

  public static synchronized Logger getLogger() {
    return LoggerMap.get((int) Thread.currentThread().getId());
  }

  public static void main(String[] args) {

    logger.setUseParentHandlers(false);

    LogFormatter formatter = new LogFormatter();
    ConsoleHandler handler = new ConsoleHandler();
    handler.setFormatter(formatter);
    logger.addHandler(handler);

    logger.info("Example of creating custom formatter.");
    logger.warning("A warning message.");
    logger.severe("A severe message.");
  }

}
