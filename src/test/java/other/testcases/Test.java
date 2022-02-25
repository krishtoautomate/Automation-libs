package other.testcases;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class Test {
  private static final Logger logger = LoggerFactory.getLogger(Test.class);

  public static void main(String[] args) {
    // assume SLF4J is bound to logback in the current environment


    logger.info("Entering application.");

    logger.info("Exiting application.");
  }
}
