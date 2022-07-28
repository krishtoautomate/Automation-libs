package com.ReportManager;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;

public class LogFormatter extends Formatter{

  private static final DateFormat df = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss.SSS");

  // ANSI escape code
  private static final String ANSI_RESET = "\u001B[0m";
  private static final String ANSI_BLACK = "\u001B[30m";
  private static final String ANSI_RED = "\u001B[31m";
  private static final String ANSI_GREEN = "\u001B[32m";
  private static final String ANSI_YELLOW = "\u001B[33m";
  private static final String ANSI_BLUE = "\u001B[34m";
  private static final String ANSI_PURPLE = "\u001B[35m";
  private static final String ANSI_CYAN = "\u001B[36m";
  private static final String ANSI_WHITE = "\u001B[37m";

  @Override
  public String format(LogRecord record) {

    String ANSI = ANSI_GREEN;
    if(record.getLevel().equals(Level.WARNING))
      ANSI = ANSI_YELLOW;
    else if(record.getLevel().equals(Level.SEVERE))
      ANSI = ANSI_RED;

    StringBuilder builder = new StringBuilder(1000);
    builder.append(ANSI);
    builder.append(df.format(new Date(record.getMillis()))).append(" - ");
    //className
    builder.append("(").append(record.getSourceClassName()).append(".");
    //methodName
    builder.append(record.getSourceMethodName()).append(") - ");
    //LEVEL
    builder.append("[").append(record.getLevel()).append("] - ");

    builder.append(formatMessage(record));

    builder.append(ANSI_RESET);

    builder.append("\n");
    return builder.toString();
  }

  public String getHead(Handler h) {
    return super.getHead(h);
  }

  public String getTail(Handler h) {
    return super.getTail(h);
  }
}
